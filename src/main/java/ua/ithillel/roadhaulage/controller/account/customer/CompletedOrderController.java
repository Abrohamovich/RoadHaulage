package ua.ithillel.roadhaulage.controller.account.customer;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.OrderCategory;
import ua.ithillel.roadhaulage.entity.OrderStatus;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/account/my-orders/completed")
@AllArgsConstructor
public class CompletedOrderController {
    private OrderService orderService;
    private OrderCategoryService orderCategoryService;

    @GetMapping
    public String completedEstimatesPage(@AuthenticationPrincipal User user,
                                         Model model) {
        List<Order> orders = orderService.findOrdersByCustomerId(user.getId());
        orders = orders.stream().filter(order -> order.getStatus().equals(OrderStatus.COMPLETED)).toList();
        orders.forEach(Order::defineAllTransactional);
        model.addAttribute("orders", orders);
        return "account/customer-orders/completed";
    }
}
