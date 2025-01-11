package ua.ithillel.roadhaulage.controller.account.customer;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.ithillel.roadhaulage.entity.Address;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.OrderCategory;
import ua.ithillel.roadhaulage.entity.User;
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
                             Model model){
        Date creationDate = new Date(System.currentTimeMillis());
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("acceptDate", creationDate);
        return "account/customer-orders/create";
    }

    @PostMapping("/create")
    public String createEstimate(@AuthenticationPrincipal User user,
                                 @RequestParam(required = true) String categoryNames,
                                 @RequestParam(required = true) String deliveryAddressString,
                                 @RequestParam(required = true) String departureAddressString,
                                 @RequestParam(required = true) String additionalInfo,
                                 @RequestParam(required = true) String weight,
                                 @RequestParam(required = true) String dimensions,
                                 @RequestParam(required = true) String cost,
                                 @RequestParam(required = true, name = "weight-unit") String weightUnit,
                                 @RequestParam(required = true, name = "dimensions-unit") String dimensionsUnit,
                                 @RequestParam(required = true, name = "currency") String currency){

        Set<OrderCategory> orderCategories = orderCategoryService.createOrderCategorySet(categoryNames);
        orderCategories.forEach(o -> orderCategoryService.save(o));

        Optional<Address> deliveryAddress = addressService.createAddress(deliveryAddressString);
        Optional<Address> departureAddress = addressService.createAddress(departureAddressString);

        if(deliveryAddress.isPresent() && departureAddress.isPresent()){
            addressService.save(deliveryAddress.get());
            addressService.save(departureAddress.get());
            Order order = new Order();
            order.setCustomer(user);
            order.setStatus("CREATED");
            order.setDeliveryAddress(deliveryAddress.get());
            order.setDepartureAddress(departureAddress.get());
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
