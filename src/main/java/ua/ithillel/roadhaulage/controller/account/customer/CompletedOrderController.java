package ua.ithillel.roadhaulage.controller.account.customer;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.List;

@Controller
@RequestMapping("/account/my-orders/completed")
@AllArgsConstructor
public class CompletedOrderController {
    private OrderService orderService;

    @GetMapping
    public String completedEstimatesPage(@AuthenticationPrincipal User user,
                                         Model model) {
        List<Order> orders = orderService.findOrdersByCustomerId(user.getId());
        orders = orders.stream().filter(order -> order.getStatus().equals("COMPLETED")).toList();
        orders.forEach(order -> {
            order.setCategoriesString();
            order.setDeliveryAddressString();
            order.setDepartureAddressString();
        });
        model.addAttribute("orders", orders);
        return "account/customerOrders/completed";
    }
}
