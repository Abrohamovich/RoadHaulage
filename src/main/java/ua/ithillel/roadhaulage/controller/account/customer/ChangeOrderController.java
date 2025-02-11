package ua.ithillel.roadhaulage.controller.account.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.entity.*;
import ua.ithillel.roadhaulage.exception.AddressCreateException;
import ua.ithillel.roadhaulage.service.interfaces.AddressService;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.sql.Date;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/account/my-orders/change")
@RequiredArgsConstructor
public class ChangeOrderController {
    private final OrderService orderService;
    private final OrderCategoryService orderCategoryService;
    private final AddressService addressService;

    @GetMapping
    public String changePage(@RequestParam long id,
                             @ModelAttribute("errorMessage") String errorMessage,
                             Model model) {
        Optional<Order> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.defineTransient();
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("order", order);
            return "account/customer-orders/change";
        }
        return "redirect:/error";
    }

    @PostMapping("/edit")
    public String changeOrder(@AuthenticationPrincipal User user,
                              @RequestParam long id,
                              RedirectAttributes redirectAttributes,
                              @RequestParam(required = false) String categoriesString,
                              @RequestParam(required = false) String cost,
                              @RequestParam(required = false) String deliveryAddressString,
                              @RequestParam(required = false) String departureAddressString,
                              @RequestParam(required = false) String additionalInfo,
                              @RequestParam(required = false) String weight,
                              @RequestParam(required = false) String dimensions,
                              @RequestParam(name = "weight-unit") String weightUnit,
                              @RequestParam(name = "dimensions-unit") String dimensionsUnit,
                              @RequestParam String currency) {
        Optional<Order> orderOptional = orderService.findById(id);
        Set<OrderCategory> orderCategories = orderCategoryService.createOrderCategorySet(categoriesString);

        Address deliveryAddress;
        Address departureAddress;

        try {
            deliveryAddress = addressService.createAddress(deliveryAddressString);
            departureAddress = addressService.createAddress(departureAddressString);
        } catch (AddressCreateException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/account/my-orders/create";
        }

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

        return "redirect:/account/my-orders/created";
    }
}
