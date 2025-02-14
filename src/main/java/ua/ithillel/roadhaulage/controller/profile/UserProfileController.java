package ua.ithillel.roadhaulage.controller.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.UserRatingDto;
import ua.ithillel.roadhaulage.entity.OrderStatus;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.service.interfaces.UserRatingService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.util.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserService userService;
    private final UserRatingService userRatingService;
    private final OrderService orderService;

    @GetMapping("/{email}/info")
    public String getUserProfileInfo(@PathVariable String email, Model model) {
        Optional<UserDto> userDB = userService.findByEmail(email);
        if(userDB.isPresent()) {
            UserDto userDto = userDB.get();
            Optional<UserRatingDto> userRatingDto = userRatingService.findById(userDto.getId());
            Map<String, String> map = new HashMap<>();
            map.put("firstName", userDto.getFirstName());
            map.put("lastName", userDto.getLastName());
            map.put("email", userDto.getEmail());
            map.put("phone", "+" + userDto.getPhoneCode() + userDto.getPhone());
            map.put("rating", String.valueOf(userRatingDto.get().getAverage()));
            map.put("count", String.valueOf(userRatingDto.get().getCount()));
            model.addAllAttributes(map);
            return "profile/info";
        }
        return "redirect:/error";
    }

    @GetMapping("/{email}/orders")
    public String getUserProfileOrders(@PathVariable String email, Model model) {
        Optional<UserDto> userDB = userService.findByEmail(email);
        if(userDB.isPresent()) {
            UserDto userDto = userDB.get();
            List<OrderDto> ordersDto = orderService.findOrdersByCustomerId(userDto.getId());
            ordersDto = ordersDto.stream()
                    .filter(orderDto -> orderDto.getStatus().equals(OrderStatus.PUBLISHED))
                    .toList();
            ordersDto.forEach(OrderDto::defineView);
            model.addAttribute("email", email);
            model.addAttribute("orders", ordersDto);
            return "profile/orders";
        }
        return "redirect:/error";
    }

    @GetMapping("/{email}/order/{id}")
    public String getUserProfileOrder(@PathVariable String email,
                                      @PathVariable long id,  Model model) {
        Optional<OrderDto> orderDB = orderService.findById(id);
        if(orderDB.isPresent()) {
            OrderDto orderDto = orderDB.get();
            orderDto.defineView();
            model.addAttribute("email", email);
            model.addAttribute("order", orderDto);
            return "profile/order";
        }

        return "redirect:/error";
    }
}
