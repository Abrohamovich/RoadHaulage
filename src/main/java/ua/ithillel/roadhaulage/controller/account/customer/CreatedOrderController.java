package ua.ithillel.roadhaulage.controller.account.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.roadhaulage.dto.AuthUserDto;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.dto.UserRatingDto;
import ua.ithillel.roadhaulage.entity.OrderStatus;
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

    @GetMapping("/page={page}")
    public String createdOrdersPage(@AuthenticationPrincipal AuthUserDto authUserDto,
                                    @PathVariable int page, Model model) {
        Page<OrderDto> ordersPage = orderService.findOrdersByCustomerIdAndStatusNot(authUserDto.getId(), OrderStatus.COMPLETED, page, 10);
        List<OrderDto> orders = ordersPage.getContent()
                .stream()
                .peek(OrderDto::defineView)
                .toList();
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        return "account/customer-orders/created";
    }

    //Sets the order status to PUBLISHED
    @PostMapping("/publish")
    public String publishOrder(@AuthenticationPrincipal AuthUserDto authUserDto,
                               @RequestParam long id){
        Optional<OrderDto> orderOptional = orderService.findById(id);
        if(orderOptional.isPresent()){
            OrderDto orderDto = orderOptional.get();
            if (!orderDto.getCustomer().getId().equals(authUserDto.getId()) ||
                    !(orderDto.getStatus().equals(OrderStatus.CREATED) ||
                    orderDto.getStatus().equals(OrderStatus.CHANGED))) {
                return "redirect:/error";
            }
            orderDto.setStatus(OrderStatus.PUBLISHED);
            orderService.save(orderDto);
        }
        return "redirect:/account/my-orders/created/page=0";
    }

    //Sets the order status to DELIVERED
    @PostMapping("/close")
    public String closeOrder(@AuthenticationPrincipal AuthUserDto authUserDto,
                             @RequestParam long id,
                             @RequestParam double rating){
        Optional<OrderDto> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent()){
            OrderDto orderDto = orderOptional.get();
            if (!orderDto.getCustomer().getId().equals(authUserDto.getId()) ||
                    !orderDto.getStatus().equals(OrderStatus.ACCEPTED)) {
                return "redirect:/error";
            }
            orderDto.setStatus(OrderStatus.COMPLETED);
            orderDto.setCompletionDate(new Date(System.currentTimeMillis()));
            Optional<UserRatingDto> userRatingOptional = userRatingService.findById(orderDto.getCourier().getId());
            userRatingOptional.ifPresent(userRating -> userRatingService.update(userRating, rating));
            orderService.save(orderDto);
        }
        return "redirect:/account/my-orders/completed/page=0";
    }

    @GetMapping("/delete")
    public String deleteOrder(@AuthenticationPrincipal AuthUserDto authUserDto,
                              @RequestParam long id){
        Optional<OrderDto> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent()){
            OrderDto orderDto = orderOptional.get();
            if (!orderDto.getCustomer().getId().equals(authUserDto.getId())) {
                return "redirect:/error";
            }
            orderService.delete(id);
        }

        return "redirect:/account/my-orders/created/page=0";
    }
}
