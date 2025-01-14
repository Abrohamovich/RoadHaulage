package ua.ithillel.roadhaulage.controller.account.courier;

import lombok.AllArgsConstructor;
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
@RequestMapping("/account/delivered-orders/delivered")
@AllArgsConstructor
public class DeliveredOrdersController {
    private OrderService orderService;

    @GetMapping
    public String acceptedOrdersPage(@AuthenticationPrincipal User user,
                                     Model model) {
        List<Order> orders = orderService.findOrdersByCourierId(user.getId());
        orders = orders.stream().filter(order -> order.getStatus().equals(OrderStatus.COMPLETED)).toList();
        orders.forEach(Order::defineAllTransactional);
        model.addAttribute("orders", orders);
        return "account/courier-orders/delivered";
    }

}
