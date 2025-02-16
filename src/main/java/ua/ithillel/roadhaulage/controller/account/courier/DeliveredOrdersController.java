package ua.ithillel.roadhaulage.controller.account.courier;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.ithillel.roadhaulage.dto.AuthUserDto;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.entity.OrderStatus;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/account/delivered-orders/delivered")
@RequiredArgsConstructor
public class DeliveredOrdersController {
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public String acceptedOrdersPage(@AuthenticationPrincipal AuthUserDto authUserDto,
                                     Model model) {
        Optional<UserDto> userDtoOptional = userService.findById(authUserDto.getId());
        UserDto userDto = userDtoOptional.get();
        List<OrderDto> orders = orderService.findOrdersByCourierId(userDto.getId());
        orders = orders.stream().filter(order -> order.getStatus().equals(OrderStatus.COMPLETED)).toList();
        orders.forEach(OrderDto::defineView);
        model.addAttribute("orders", orders);
        return "account/courier-orders/delivered";
    }

}
