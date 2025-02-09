package ua.ithillel.roadhaulage.controller.account.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.service.interfaces.UserRatingService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/account/my-orders/created")
@RequiredArgsConstructor
public class CreatedOrderController {
    private final OrderService orderService;
    private final UserRatingService userRatingService;

    @GetMapping
    public String createdOrdersPage(@AuthenticationPrincipal User user,
                                         Model model) {
        List<Order> orders = orderService.findOrdersByCustomerId(user.getId());
        orders = orders.stream().filter(order -> !order.getStatus().equals(OrderStatus.COMPLETED)).toList();
        orders.forEach(Order::defineTransient);
        model.addAttribute("orders", orders);
        return "account/customer-orders/created";
    }
    @PostMapping("/publish")
    public String publishOrder(@RequestParam long id){
        Optional<Order> orderOptional = orderService.findById(id);
        if(orderOptional.isPresent()){
            Order order = orderOptional.get();
            order.setStatus(OrderStatus.PUBLISHED);
            orderService.update(order);
        }
        return "redirect:/account/my-orders/created";
    }

    @PostMapping("/close")
    public String closeOrder(@RequestParam long id, @RequestParam double rating){
        Optional<Order> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent()){
            Order order = orderOptional.get();
            order.setStatus(OrderStatus.COMPLETED);
            order.setCompletionDate(new Date(System.currentTimeMillis()));
            Optional<UserRating> userRatingOptional = userRatingService.findById(order.getCourier().getId());
            userRatingOptional.ifPresent(userRating -> userRatingService.update(userRating, rating));
            orderService.update(order);
        }
        return "redirect:/account/my-orders/completed";
    }

    @GetMapping("/delete")
    public String deleteOrder(@RequestParam long id){
        orderService.delete(id);
        return "redirect:/account/my-orders/created";
    }
}
