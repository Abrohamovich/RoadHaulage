package ua.ithillel.roadhaulage.controller.account.myOrders;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/account/my-orders/created")
@AllArgsConstructor
public class CreatedOrderController {
    private OrderService orderService;

    @GetMapping
    public String createdOrdersPage(@AuthenticationPrincipal User user,
                                         Model model) {
        List<Order> orders = orderService.findOrdersByCustomerId(user.getId());
        orders = orders.stream().filter(order -> !order.getStatus().equals("COMPLETED")).toList();
        model.addAttribute("orders", orders);
        return "account/myOrders/created";
    }

    @PostMapping("/publish")
    public String publishOrder(@RequestParam long id){
        Optional<Order> order = orderService.findById(id);
        order.get().setStatus("PUBLISHED");
        orderService.update(order.get());
        return "redirect:/account/my-orders/created";
    }

    @PostMapping("/close")
    public String closeOrder(@RequestParam long id){
        Optional<Order> order = orderService.findById(id);
        order.get().setStatus("COMPLETED");
        order.get().setCompletionDate(new Date(System.currentTimeMillis()));
        orderService.update(order.get());
        return "redirect:/account/my-orders/completed";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable(name = "id") long id){
        orderService.delete(id);
        return "redirect:/account/my-orders/created";
    }

}
