package ua.ithillel.roadhaulage.controller.account;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.ithillel.roadhaulage.dto.AuthUserDto;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;
import ua.ithillel.roadhaulage.util.ReportGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/generate-report")
public class ReportGeneratorController {
    private final OrderService orderService;
    private final ReportGenerator reportGenerator;
    private final UserService userService;

    @GetMapping
    public void generateReport(@AuthenticationPrincipal AuthUserDto authUserDto, HttpServletResponse response) {
        try {
            Optional<UserDto> userDto = userService.findById(authUserDto.getId());
            List<OrderDto> customerOrderList = orderService.findOrdersByCustomerId(authUserDto.getId());
            List<OrderDto> courierOrderList = orderService.findOrdersByCourierId(authUserDto.getId());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            reportGenerator.generateReport(userDto.get(), customerOrderList, courierOrderList, outputStream);

            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.setHeader("Content-Disposition", "attachment; filename=\"report.docx\"");
            response.getOutputStream().write(outputStream.toByteArray());
            response.flushBuffer();
        } catch (IOException _) {

        }
    }
}
