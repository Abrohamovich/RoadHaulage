package ua.ithillel.roadhaulage.controller.account.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.service.interfaces.AddressService;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.sql.Date;
import java.util.*;

@Controller
@RequestMapping("/account/my-orders/create")
@RequiredArgsConstructor
public class CreateNewOrderController {
    private final OrderService orderService;
    private final OrderCategoryService orderCategoryService;
    private final AddressService addressService;

    @GetMapping
    public String createPage(@AuthenticationPrincipal User user,
                             Model model) {
        Date creationDate = new Date(System.currentTimeMillis());
        Map<String, String> map = new HashMap<>();
        map.put("firstName", user.getFirstName());
        map.put("lastName", user.getLastName());
        map.put("acceptDate", creationDate.toString());
        model.addAllAttributes(map);
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
                              @RequestParam String currency) {

        Set<OrderCategory> orderCategories = orderCategoryService.createOrderCategorySet(categoryNames);
        orderCategories.forEach(orderCategoryService::save);

        Optional<Address> deliveryAddressOptional = addressService.createAddress(deliveryAddressString);
        Optional<Address> departureAddressOptional = addressService.createAddress(departureAddressString);

        if (deliveryAddressOptional.isEmpty() &&
                departureAddressOptional.isEmpty()) {
            return "redirect:/error";
        }

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

        return "redirect:/account/my-orders/create";
    }

}
