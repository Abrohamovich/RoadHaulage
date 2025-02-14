package ua.ithillel.roadhaulage.controller.account.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.UserRatingDto;
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
    public String createdOrdersPage(@AuthenticationPrincipal UserDto userDto,
                                         Model model) {
        List<OrderDto> orders = orderService.findOrdersByCustomerId(userDto.getId());
        orders = orders.stream().filter(order -> !order.getStatus().equals(OrderStatus.COMPLETED)).toList();
        orders.forEach(OrderDto::defineView);
        model.addAttribute("orders", orders);
        return "account/customer-orders/created";
    }
    @PostMapping("/publish")
    public String publishOrder(@RequestParam long id){
        Optional<OrderDto> orderOptional = orderService.findById(id);
        if(orderOptional.isPresent()){
            OrderDto orderDto = orderOptional.get();
            orderDto.setStatus(OrderStatus.PUBLISHED);
            orderService.update(orderDto);
        }
        return "redirect:/account/my-orders/created";
    }

    @PostMapping("/close")
    public String closeOrder(@RequestParam long id, @RequestParam double rating){
        Optional<OrderDto> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent()){
            OrderDto orderDto = orderOptional.get();
            orderDto.setStatus(OrderStatus.COMPLETED);
            orderDto.setCompletionDate(new Date(System.currentTimeMillis()));
            Optional<UserRatingDto> userRatingOptional = userRatingService.findById(orderDto.getCourier().getId());
            userRatingOptional.ifPresent(userRating -> userRatingService.update(userRating, rating));
            orderService.update(orderDto);
        }
        return "redirect:/account/my-orders/completed";
    }

    @GetMapping("/delete")
    public String deleteOrder(@RequestParam long id){
        orderService.delete(id);
        return "redirect:/account/my-orders/created";
    }
}
