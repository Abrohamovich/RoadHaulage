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
        Order order = orderService.findById(id);
        order.setAcceptDate(new Date(System.currentTimeMillis()));
        order.setStatus("ACCEPTED");
        order.setCourier(user);
        orderService.save(order);
        return "redirect:/account/delivered-orders/accepted";
    }

    @PostMapping("/decline")
    public String declineOrder(@RequestParam long id){
        Order order = orderService.findById(id);
        order.setCourier(null);
        order.setStatus("PUBLISHED");
        order.setAcceptDate(null);
        orderService.save(order);
        return "redirect:/account/delivered-orders/accepted";
    }
}
