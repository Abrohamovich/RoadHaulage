package ua.ithillel.roadhaulage.controller.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.OrderStatus;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.UserRating;
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
        Optional<User> userDB = userService.findByEmail(email);
        if(userDB.isPresent()) {
            User user = userDB.get();
            Optional<UserRating> userRating = userRatingService.findById(user.getId());
            Map<String, String> map = new HashMap<>();
            map.put("firstName", user.getFirstName());
            map.put("lastName", user.getLastName());
            map.put("email", user.getEmail());
            map.put("phone", "+" + user.getPhoneCode() + user.getPhone());
            map.put("rating", String.valueOf(userRating.get().getAverage()));
            map.put("count", String.valueOf(userRating.get().getCount()));
            model.addAllAttributes(map);
            return "profile/info";
        }
        return "redirect:/error";
    }

    @GetMapping("/{email}/orders")
    public String getUserProfileOrders(@PathVariable String email, Model model) {
        Optional<User> userDB = userService.findByEmail(email);
        if(userDB.isPresent()) {
            User user = userDB.get();
            List<Order> orders = orderService.findOrdersByCustomerId(user.getId());
            orders = orders.stream()
                    .filter(order -> order.getStatus().equals(OrderStatus.PUBLISHED))
                    .toList();
            orders.forEach(Order::defineTransient);
            model.addAttribute("email", email);
            model.addAttribute("orders", orders);
            return "profile/orders";
        }
        return "redirect:/error";
    }

    @GetMapping("/{email}/order/{id}")
    public String getUserProfileOrder(@PathVariable String email,
                                      @PathVariable long id,  Model model) {
        Optional<Order> orderDB = orderService.findById(id);
        if(orderDB.isPresent()) {
            Order order = orderDB.get();
            model.addAttribute("email", email);
            order.defineTransient();
            model.addAttribute("order", order);
            return "profile/order";
        }

        return "redirect:/error";
    }
}
