package ua.ithillel.roadhaulage.controller.account;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.util.ReportGenerator;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/generate-report")
public class ReportGeneratorController {
    private OrderService orderService;

    @GetMapping
    public String generateReport(@AuthenticationPrincipal User user) {
        ReportGenerator reportGenerator = new ReportGenerator();
        List<Order> customerOrderList = orderService.findOrdersByCustomerId(user.getId());
        List<Order> courierOrderList = orderService.findOrdersByCourierId(user.getId());
        customerOrderList.forEach(Order::defineAllTransactional);
        courierOrderList.forEach(Order::defineAllTransactional);
        reportGenerator.generateReport(user, customerOrderList, courierOrderList);
        return "redirect:/account/settings/personal-information";
    }
}
