package ua.ithillel.roadhaulage.controller.main;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class ProfileViewController {
    private UserService userService;
    private OrderService orderService;

    @GetMapping("personal-info/id/{id}")
    public String userInfoPage(@PathVariable long id, Model model) {
        Optional<User> user = userService.findById(id);
        model.addAttribute("user", user);
        return "profile/userInfo";
    }

    @GetMapping("orders/id/{id}")
    public String userOrdersPage(@PathVariable long id, Model model) {
        Optional<User> user = userService.findById(id);
        List<Order> orders = orderService.findOrdersByCustomerId(id);
        orders = orders.stream().filter(order -> order.getStatus().equals("PUBLISHED")).toList();
        model.addAttribute("orders", orders);
        model.addAttribute("user", user);
        return "profile/userOrders";
    }
}
