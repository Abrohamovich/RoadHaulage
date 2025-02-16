package ua.ithillel.roadhaulage.controller.account;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.util.ReportGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/generate-report")
public class ReportGeneratorController {
    private final OrderService orderService;
    private final ReportGenerator reportGenerator;

    @GetMapping
    public void generateReport(@AuthenticationPrincipal UserDto userDto, HttpServletResponse response) {
        try {
            List<OrderDto> customerOrderList = orderService.findOrdersByCustomerId(userDto.getId());
            List<OrderDto> courierOrderList = orderService.findOrdersByCourierId(userDto.getId());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            reportGenerator.generateReport(userDto, customerOrderList, courierOrderList, outputStream);

            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.setHeader("Content-Disposition", "attachment; filename=\"report.docx\"");
            response.getOutputStream().write(outputStream.toByteArray());
            response.flushBuffer();
        } catch (IOException _) {

        }
    }
}
