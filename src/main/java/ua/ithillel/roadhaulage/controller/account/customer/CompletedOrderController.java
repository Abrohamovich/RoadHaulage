package ua.ithillel.roadhaulage.controller.account.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.OrderStatus;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.List;

@Controller
@RequestMapping("/account/my-orders/completed")
@RequiredArgsConstructor
public class CompletedOrderController {
    private final OrderService orderService;

    @GetMapping
    public String completedOrdersPage(@AuthenticationPrincipal User user,
                                         Model model) {
        List<Order> orders = orderService.findOrdersByCustomerId(user.getId());
        orders = orders.stream().filter(order -> order.getStatus().equals(OrderStatus.COMPLETED)).toList();
        orders.forEach(Order::defineTransient);
        model.addAttribute("orders", orders);
        return "account/customer-orders/completed";
    }
}
