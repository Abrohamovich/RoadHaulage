package ua.ithillel.roadhaulage.controller.account.courier;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.OrderStatus;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/account/delivered-orders/accepted")
@RequiredArgsConstructor
public class AcceptedOrdersController {
    private final OrderService orderService;

    @GetMapping
    public String acceptedOrdersPage(@AuthenticationPrincipal UserDto userDto,
                                     Model model) {
        List<OrderDto> orders = orderService.findOrdersByCourierId(userDto.getId());
        orders = orders.stream().filter(order -> order.getStatus().equals(OrderStatus.ACCEPTED)).toList();
        orders.forEach(OrderDto::defineView);
        model.addAttribute("orders", orders);
        return "account/courier-orders/accepted";
    }

    @PostMapping("/accept")
    public String acceptOrder(@AuthenticationPrincipal UserDto userDto,
                              @RequestParam long id){
        Optional<OrderDto> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent()) {
            OrderDto orderDto = orderOptional.get();
            orderDto.setAcceptDate(new Date(System.currentTimeMillis()));
            orderDto.setStatus(OrderStatus.ACCEPTED);
            orderDto.setCourier(userDto);
            orderService.save(orderDto);
        }

        return "redirect:/account/delivered-orders/accepted";
    }

    @PostMapping("/decline")
    public String declineOrder(@RequestParam long id){
        Optional<OrderDto> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent()) {
            OrderDto orderDto = orderOptional.get();
            orderDto.setCourier(null);
            orderDto.setStatus(OrderStatus.PUBLISHED);
            orderDto.setAcceptDate(null);
            orderService.save(orderDto);
        }
        return "redirect:/account/delivered-orders/accepted";
    }
}
