package ua.ithillel.roadhaulage.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainPagesController {
    private OrderService orderService;

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/aboutUs")
    public String aboutUs() {
        return "aboutUs";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("attentionMessage") String attentionMessage,
                        @ModelAttribute("successMessage") String successMessage,
                        Model model) {
        model.addAttribute("attentionMessage", attentionMessage);
        model.addAttribute("successMessage", successMessage);
        return "login";
    }

    @GetMapping("/orders")
    public String orders(@AuthenticationPrincipal User user, Model model) {
        List<Order> allOrders = orderService.findAll();
        List<Order> customerOrders = orderService.findOrdersByCustomerId(user.getId());
        List<Order> orders = allOrders.stream()
                .filter(order -> customerOrders.stream()
                        .noneMatch(customerOrder -> customerOrder.getId().equals(order.getId())))
                .filter(order -> order.getStatus().equals("PUBLISHED"))
                .toList();
        if(!orders.isEmpty()) {
            model.addAttribute("orders", orders);
        }
        return "orders";
    }
}
