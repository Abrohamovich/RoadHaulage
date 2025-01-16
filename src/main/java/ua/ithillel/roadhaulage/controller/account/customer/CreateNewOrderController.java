package ua.ithillel.roadhaulage.controller.account.customer;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.service.interfaces.AddressService;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.sql.Date;
import java.util.*;

@Controller
@RequestMapping("/account/my-orders/create")
@AllArgsConstructor
public class CreateNewOrderController {
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
                              @RequestParam(required = true) String categoryNames,
                              @RequestParam(required = true) String deliveryAddressString,
                              @RequestParam(required = true) String departureAddressString,
                              @RequestParam(required = true) String additionalInfo,
                              @RequestParam(required = true) String weight,
                              @RequestParam(required = true) String dimensions,
                              @RequestParam(required = true) String cost,
                              @RequestParam(required = true, name = "weight-unit") String weightUnit,
                              @RequestParam(required = true, name = "dimensions-unit") String dimensionsUnit,
                              @RequestParam(required = true, name = "currency") String currency) {

        Set<OrderCategory> orderCategories = orderCategoryService.createOrderCategorySet(categoryNames);
        orderCategories.forEach(o -> orderCategoryService.save(o));

        Optional<Address> deliveryAddressOptional = addressService.createAddress(deliveryAddressString);
        Optional<Address> departureAddressOptional = addressService.createAddress(departureAddressString);

        if (deliveryAddressOptional.isPresent() && departureAddressOptional.isPresent()) {
            Address deliveryAddress = deliveryAddressOptional.get();
            Address departureAddress = departureAddressOptional.get();

            addressService.save(departureAddress);
            addressService.save(deliveryAddress);

            Order order = new Order();
            order.setCustomer(user);
            order.setStatus(OrderStatus.CREATED);
            order.setDeliveryAddress(deliveryAddress);
            order.setDepartureAddress(departureAddress);
            order.setAdditionalInfo(additionalInfo);
            order.setWeight(weight);
            order.setWeightUnit(weightUnit);
            order.setDimensions(dimensions);
            order.setDimensionsUnit(dimensionsUnit);
            order.setCost(cost);
            order.setCurrency(currency);
            order.setCreationDate(new Date(System.currentTimeMillis()));
            order.setCategories(orderCategories);
            orderService.save(order);
        }

        return "redirect:/account/my-orders/create";
    }

}
