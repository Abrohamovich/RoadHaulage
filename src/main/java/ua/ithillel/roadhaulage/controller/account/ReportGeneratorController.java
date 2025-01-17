package ua.ithillel.roadhaulage.controller.account;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.util.ReportGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/generate-report")
public class ReportGeneratorController {
    private OrderService orderService;

    @GetMapping
    public void generateReport(@AuthenticationPrincipal User user, HttpServletResponse response) {
        try {
            ReportGenerator reportGenerator = new ReportGenerator();

            List<Order> customerOrderList = orderService.findOrdersByCustomerId(user.getId());
            List<Order> courierOrderList = orderService.findOrdersByCourierId(user.getId());
            customerOrderList.forEach(Order::defineAllTransactional);
            courierOrderList.forEach(Order::defineAllTransactional);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            reportGenerator.generateReport(user, customerOrderList, courierOrderList, outputStream);

            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.setHeader("Content-Disposition", "attachment; filename=\"report.docx\"");
            response.getOutputStream().write(outputStream.toByteArray());
            response.flushBuffer();
        } catch (IOException e) {
            System.err.println("Error generating report: " + e.getMessage());
        }
    }
}
