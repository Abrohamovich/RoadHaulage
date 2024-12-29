package ua.ithillel.roadhaulage.controller.account.order;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.List;

@Controller
@RequestMapping("/account/my-orders/created")
@AllArgsConstructor
public class CreatedOrderController {
    private OrderService orderService;

    @GetMapping
    public String createdOrdersPage(@AuthenticationPrincipal User user,
                                         Model model) {
        List<Order> orders = orderService.findOrdersByCustomerId(user.getId());
        model.addAttribute("orders", orders);
        return "account/myOrders/created";
    }

    @PostMapping("/publish")
    public String publishOrder(@RequestParam long id){
        Order order = orderService.findById(id);
        order.setStatus("PUBLISHED");
        orderService.update(order);
        return "redirect:/account/my-orders/created";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable(name = "id") long id){
        orderService.delete(id);
        return "redirect:/account/my-orders/created";
    }

}
