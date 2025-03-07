package ua.ithillel.roadhaulage.util;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.springframework.stereotype.Component;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.entity.OrderStatus;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ReportGenerator {

    private final static String templatePath = "document/reportTemplate.docx";

    private static void replacePlaceholdersInParagraph(XWPFParagraph paragraph, UserDto userDto, String[] data) {
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs != null) {
            StringBuilder paragraphText = new StringBuilder();
            for (XWPFRun run : runs) {
                paragraphText.append(run.getText(0));
            }

            String replacedText = paragraphText.toString()
                    .replace("{{date}}", new Date(System.currentTimeMillis()).toString())
                    .replace("{{firstName}}", userDto.getFirstName())
                    .replace("{{lastName}}", userDto.getLastName())
                    .replace("{{email}}", userDto.getEmail())
                    .replace("{{phone}}", userDto.getLocalPhone())
                    .replace("{{createdOrderNum}}", data[0])
                    .replace("{{deliveredOrderNum}}", data[1])
                    .replace("{{moneyEarned}}", data[3])
                    .replace("{{moneySpent}}", data[2]);

            if (!replacedText.contentEquals(paragraphText)) {
                for (int i = runs.size() - 1; i >= 0; i--) {
                    paragraph.removeRun(i);
                }
                XWPFRun newRun = paragraph.createRun();
                newRun.setText(replacedText, 0);
            }
        }
    }

    private static void generateOrderTables(XWPFDocument document, List<OrderDto> orderList, List<OrderStatus> statusArray, int indexFirst, int indexLast) {
        for (int tableIndex = indexFirst; tableIndex <= indexLast; tableIndex++) {
            XWPFTable table = document.getTables().get(tableIndex);

            OrderStatus targetStatus = switch (tableIndex) {
                case 0, 4 -> statusArray.get(0);
                case 1, 5 -> statusArray.get(1);
                case 2 -> statusArray.get(2);
                case 3 -> statusArray.get(3);
                default -> null;
            };

            if (targetStatus == null) continue;

            int rowIndex = 1;
            for (OrderDto order : orderList) {
                if (order.getStatus().equals(targetStatus)) {
                    XWPFTableRow row = table.createRow();

                    row.getCell(0).setText(order.getCategories().toString());
                    row.getCell(1).setText(order.getDeliveryAddress().toString());
                    row.getCell(2).setText(order.getDepartureAddress().toString());
                    row.getCell(3).setText(order.getWeight() + " " + order.getWeightUnit());
                    row.getCell(4).setText(order.getDimensions() + " " + order.getDimensionsUnit());
                    row.getCell(5).setText(order.getCost() + " " + order.getCurrency());
                    row.getCell(6).setText(order.getCreationDate().toString());
                    if (targetStatus == OrderStatus.ACCEPTED) row.getCell(7).setText(order.getAcceptDate().toString());
                    if (targetStatus == OrderStatus.COMPLETED)
                        row.getCell(7).setText(order.getCompletionDate().toString());

                    rowIndex++;
                }
            }

            List<OrderDto> orders = orderList.stream()
                    .filter(order -> order.getStatus().equals(targetStatus)).toList();

            String str = generateFullPriceString(orders);

            XWPFTableRow additionalRow = table.insertNewTableRow(rowIndex);

            short k = 7;
            if (targetStatus == OrderStatus.ACCEPTED || targetStatus == OrderStatus.COMPLETED) k = 8;

            for (int i = 0; i < k; i++) {
                XWPFTableCell cell = additionalRow.addNewTableCell();
                CTTcBorders borders = getCtTcBorders(cell);
                borders.getBottom().setColor("ffffff");
                borders.getLeft().setColor("ffffff");
                borders.getRight().setColor("ffffff");
                if (i == 5) {
                    cell.setText(str);
                }
            }
            XWPFTableCell cell = additionalRow.getCell(5);
            CTTcBorders borders = getCtTcBorders(cell);
            borders.getBottom().setColor("000000");
            borders.getLeft().setColor("000000");
            borders.getRight().setColor("000000");
        }
    }

    private static CTTcBorders getCtTcBorders(XWPFTableCell cell) {
        CTTc ctCell = cell.getCTTc();
        CTTcPr cellProperties = ctCell.getTcPr();
        if (cellProperties == null) {
            cellProperties = ctCell.addNewTcPr();
        }
        CTTcBorders borders = cellProperties.getTcBorders();
        if (borders == null) {
            borders = cellProperties.addNewTcBorders();
        }
        borders.addNewBottom();
        borders.addNewLeft();
        borders.addNewRight();
        return borders;
    }

    private static String generateFullPriceString(List<OrderDto> orderList) {
        Map<String, Double> courierOrderCostMap = orderList.stream()
                .collect(Collectors.groupingBy(
                        OrderDto::getCurrency,
                        Collectors.summingDouble(order -> Double.parseDouble(order.getCost()))
                ));
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Double> entry : courierOrderCostMap.entrySet()) {
            sb.append(entry.getValue()).append(" ").append(entry.getKey()).append(" ");
        }
        return sb.toString();
    }

    public void generateReport(UserDto userDto, List<OrderDto> customerOrderList, List<OrderDto> courierOrderList, ByteArrayOutputStream outputStream) {
        try (FileInputStream fis = new FileInputStream(templatePath)
        ) {
            XWPFDocument document = new XWPFDocument(fis);

            String[] data = new String[4];

            data[0] = String.valueOf(customerOrderList.size());
            data[1] = String.valueOf(courierOrderList.size());
            data[2] = generateFullPriceString(customerOrderList);
            data[3] = generateFullPriceString(courierOrderList);

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                replacePlaceholdersInParagraph(paragraph, userDto, data);
            }

            List<OrderStatus> statusCustomerArray = List.of(OrderStatus.CREATED, OrderStatus.PUBLISHED, OrderStatus.ACCEPTED, OrderStatus.COMPLETED);
            List<OrderStatus> statusCourierArray = List.of(OrderStatus.ACCEPTED, OrderStatus.COMPLETED);

            generateOrderTables(document, customerOrderList, statusCustomerArray, 0, 3);
            generateOrderTables(document, courierOrderList, statusCourierArray, 4, 5);

            document.write(outputStream);

        } catch (IOException e) {
            System.err.println("Error while generating" + e.getMessage());
        }
    }
}
