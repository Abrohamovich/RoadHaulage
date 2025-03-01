package ua.ithillel.roadhaulage.controller.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Optional<UserRatingDto> userRatingDto = userRatingService.findByUserEmail(email);
        if(userDB.isPresent() && userRatingDto.isPresent()) {
            UserDto userDto = userDB.get();
            Map<String, String> map = new HashMap<>();
            map.put("firstName", userDto.getFirstName());
            map.put("lastName", userDto.getLastName());
            map.put("email", userDto.getEmail());
            map.put("phone", userDto.getPhoneCode() + userDto.getPhone());
            map.put("rating", String.valueOf(userRatingDto.get().getAverage()));
            map.put("count", String.valueOf(userRatingDto.get().getCount()));
            model.addAllAttributes(map);
            return "profile/info";
        }
        return "redirect:/error";
    }

    @GetMapping("/{email}/orders/page={page}")
    public String getUserProfileOrders(@PathVariable String email, @PathVariable int page, Model model) {
        Optional<UserDto> userDB = userService.findByEmail(email);
        if(userDB.isPresent()) {
            UserDto userDto = userDB.get();
            Page<OrderDto> ordersPage = orderService.findOrdersByCustomerIdAndStatus(userDto.getId(), OrderStatus.PUBLISHED, page, 10);
            List<OrderDto> orders = ordersPage.getContent();
            orders = orders.stream()
                    .peek(OrderDto::defineView)
                    .toList();
            model.addAttribute("email", email);
            model.addAttribute("orders", orders);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", ordersPage.getTotalPages());
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
