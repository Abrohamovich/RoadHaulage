package ua.ithillel.roadhaulage.controller.main;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.List;

@Controller
@AllArgsConstructor
public class OrdersPageController {
    private OrderService orderService;


    @GetMapping("/orders")
    public String orders(@AuthenticationPrincipal User user, Model model) {
        List<Order> allOrders = orderService.findAll();
        List<Order> customerOrders = orderService.findOrdersByCustomerId(user.getId());
        List<Order> orders = allOrders.stream()
                .filter(order -> customerOrders.stream()
                        .noneMatch(customerOrder -> customerOrder.getId().equals(order.getId())))
                .filter(order -> order.getStatus().equals("PUBLISHED"))
                .toList();
        orders.forEach(o -> {
            o.defineCategoryNames();
            o.setDeliveryAddressString();
            o.setDepartureAddressString();
        });
        if(!orders.isEmpty()) {
            model.addAttribute("orders", orders);
        }
        return "orders";
    }
}
