package ua.ithillel.roadhaulage.controller.account.customer;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.service.interfaces.AddressService;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.sql.Date;
import java.util.*;

@Controller
@RequestMapping("/account/my-orders/create")
@AllArgsConstructor
public class CreateNewOrderController {
    private UserService userService;
    private OrderService orderService;
    private OrderCategoryService orderCategoryService;
    private AddressService addressService;

    @GetMapping
    public String createPage(@AuthenticationPrincipal User user,
                             Model model) {
        Date creationDate = new Date(System.currentTimeMillis());
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("acceptDate", creationDate);
        return "account/customer-orders/create";
    }

    @PostMapping("/create")
    public String createOrder(@AuthenticationPrincipal User user,
                              @RequestParam String categoryNames,
                              @RequestParam String deliveryAddressString,
                              @RequestParam String departureAddressString,
                              @RequestParam String additionalInfo,
                              @RequestParam String weight,
                              @RequestParam String dimensions,
                              @RequestParam String cost,
                              @RequestParam(name = "weight-unit") String weightUnit,
                              @RequestParam(name = "dimensions-unit") String dimensionsUnit,
                              @RequestParam(name = "currency") String currency) {

        Set<OrderCategory> orderCategories = orderCategoryService.createOrderCategorySet(categoryNames);
        orderCategories.forEach(orderCategoryService::save);

        Optional<Address> deliveryAddressOptional = addressService.createAddress(deliveryAddressString);
        Optional<Address> departureAddressOptional = addressService.createAddress(departureAddressString);

        if (deliveryAddressOptional.isPresent() && departureAddressOptional.isPresent()) {
            Address deliveryAddress = deliveryAddressOptional.get();
            Address departureAddress = departureAddressOptional.get();

            addressService.save(departureAddress);
            addressService.save(deliveryAddress);

            Order orderr = orderService.createOrder(user, OrderStatus.CREATED, deliveryAddress,
                    departureAddress, additionalInfo, weight, weightUnit, dimensions, dimensionsUnit,
                    cost, currency, new Date(System.currentTimeMillis()), orderCategories);
            orderService.save(orderr);
        }

        return "redirect:/account/my-orders/create";
    }

}
