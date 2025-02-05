package ua.ithillel.roadhaulage.controller.account.customer;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.service.interfaces.AddressService;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/account/my-orders/change")
@AllArgsConstructor
public class ChangeOrderController {
    private final UserService userService;
    private OrderService orderService;
    private OrderCategoryService orderCategoryService;
    private AddressService addressService;

    @PostMapping()
    public String changePage(@RequestParam("id") long id, Model model) {
        Optional<Order> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.defineTransient();
            model.addAttribute("order", order);
            return "account/customer-orders/change";
        }
        return "redirect:/error";
    }

    @PostMapping("/edit")
    public String changeOrder(@AuthenticationPrincipal User user,
                              @RequestParam long id,
                              @RequestParam(required = false) String categoriesString,
                              @RequestParam(required = false) String cost,
                              @RequestParam(required = false) String deliveryAddressString,
                              @RequestParam(required = false) String departureAddressString,
                              @RequestParam(required = false) String additionalInfo,
                              @RequestParam(required = false) String weight,
                              @RequestParam(required = false) String dimensions,
                              @RequestParam(name = "weight-unit") String weightUnit,
                              @RequestParam(name = "dimensions-unit") String dimensionsUnit,
                              @RequestParam(name = "currency") String currency) {
        Optional<Order> orderOptional = orderService.findById(id);
        Set<OrderCategory> orderCategories = orderCategoryService.createOrderCategorySet(categoriesString);

        Optional<Address> deliveryAddressOptional = addressService.createAddress(deliveryAddressString);
        Optional<Address> departureAddressOptional = addressService.createAddress(departureAddressString);

        if (orderOptional.isPresent() && deliveryAddressOptional.isPresent() && departureAddressOptional.isPresent()) {
            Address deliveryAddress = deliveryAddressOptional.get();
            Address departureAddress = departureAddressOptional.get();

            addressService.save(departureAddress);
            addressService.save(deliveryAddress);

            Order order = orderOptional.get();
            order.setCategories(orderCategories);
            order.setCost(cost);
            order.setCurrency(currency);
            order.setDepartureAddress(departureAddress);
            order.setDeliveryAddress(deliveryAddress);
            order.setAdditionalInfo(additionalInfo);
            order.setWeight(weight);
            order.setWeightUnit(weightUnit);
            order.setDimensions(dimensions);
            order.setDimensionsUnit(dimensionsUnit);
            order.setAmendmentDate(new Date(System.currentTimeMillis()));
            order.setStatus(OrderStatus.CHANGED);
            orderService.update(order);
        }
        return "redirect:/account/my-orders/created";
    }
}
