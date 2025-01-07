package ua.ithillel.roadhaulage.controller.account.deliveredOrders;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/account/delivered-orders/accepted")
@AllArgsConstructor
public class AcceptedOrdersController {
    private OrderService orderService;

    @GetMapping
    public String acceptedOrdersPage(@AuthenticationPrincipal User user,
                                     Model model) {
        List<Order> orders = orderService.findOrdersByCourierId(user.getId());
        orders = orders.stream().filter(order -> order.getStatus().equals("ACCEPTED")).toList();
        model.addAttribute("orders", orders);
        return "account/acceptedOrders/accepted";
    }

    @PostMapping("/accept")
    public String acceptOrder(@AuthenticationPrincipal User user,
                              @RequestParam long id){
        Optional<Order> order = orderService.findById(id);
        order.get().setAcceptDate(new Date(System.currentTimeMillis()));
        order.get().setStatus("ACCEPTED");
        order.get().setCourier(user);
        orderService.save(order.get());
        return "redirect:/account/delivered-orders/accepted";
    }

    @PostMapping("/decline")
    public String declineOrder(@RequestParam long id){
        Optional<Order> order = orderService.findById(id);
        order.get().setCourier(null);
        order.get().setStatus("PUBLISHED");
        order.get().setAcceptDate(null);
        orderService.save(order.get());
        return "redirect:/account/delivered-orders/accepted";
    }
}
