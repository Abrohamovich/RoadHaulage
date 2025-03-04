package ua.ithillel.roadhaulage.controller.account.courier;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.roadhaulage.dto.AuthUserDto;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.entity.OrderStatus;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/account/delivered-orders/accepted")
@RequiredArgsConstructor
public class AcceptedOrdersController {
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("page={page}")
    public String acceptedOrdersPage(@AuthenticationPrincipal AuthUserDto authUserDto,
                                     @PathVariable int page, Model model) {
        Page<OrderDto> ordersPage = orderService.findOrdersByCourierIdAndStatus(authUserDto.getId(), OrderStatus.ACCEPTED, page, 10);
        List<OrderDto> orders = ordersPage.getContent()
                .stream()
                .peek(OrderDto::defineView)
                .toList();
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        return "account/courier-orders/accepted";
    }

    //Sets the current user as the courier of an order with the status PUBLISHED
    @PostMapping("/accept")
    public String acceptOrder(@AuthenticationPrincipal AuthUserDto authUserDto,
                              @RequestParam long id) {
        Optional<UserDto> userOptional = userService.findById(authUserDto.getId());
        Optional<OrderDto> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent() && userOptional.isPresent()) {
            UserDto userDto = userOptional.get();
            OrderDto orderDto = orderOptional.get();
            if (orderDto.getStatus().equals(OrderStatus.PUBLISHED)) {
                orderDto.setAcceptDate(new Date(System.currentTimeMillis()));
                orderDto.setStatus(OrderStatus.ACCEPTED);
                orderDto.setCourier(userDto);
                orderService.save(orderDto);
            } else return "redirect:/error";
        }
        return "redirect:/account/delivered-orders/accepted/page=0";
    }

    //Removes the current courier user from the order with the status ACCEPTED
    @PostMapping("/decline")
    public String declineOrder(@AuthenticationPrincipal AuthUserDto authUserDto,
                               @RequestParam long id) {
        Optional<OrderDto> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent()) {
            OrderDto orderDto = orderOptional.get();
            if (!orderDto.getCourier().getId().equals(authUserDto.getId()) ||
                    !orderDto.getStatus().equals(OrderStatus.ACCEPTED)) {
                return "redirect:/error";
            }
            orderDto.setCourier(null);
            orderDto.setStatus(OrderStatus.PUBLISHED);
            orderDto.setAcceptDate(null);
            orderService.save(orderDto);
        }
        return "redirect:/account/delivered-orders/accepted/page=0";
    }
}
