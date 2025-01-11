package ua.ithillel.roadhaulage.controller.account.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.ithillel.roadhaulage.entity.Address;
import ua.ithillel.roadhaulage.entity.OrderCategory;
import ua.ithillel.roadhaulage.service.interfaces.AddressService;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.entity.Order;

import java.sql.Date;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/account/my-orders/change")
@AllArgsConstructor
public class ChangeOrderController {
    private OrderService orderService;
    private OrderCategoryService orderCategoryService;
    private AddressService addressService;

    @PostMapping()
    public String changePage(@RequestParam("id") long id, Model model) {
        Optional<Order> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setCategoriesString();
            order.setDepartureAddressString();
            order.setDeliveryAddressString();
            model.addAttribute("order", order);
            return "account/customerOrders/change";
        }
        return "redirect:/error";
    }

    @PostMapping("/edit")
    public String changeOrder(@RequestParam long id,
                              @RequestParam(required = false) String categoriesString,
                              @RequestParam(required = false) String cost,
                              @RequestParam(required = false) String deliveryAddressString,
                              @RequestParam(required = false) String departureAddressString,
                              @RequestParam(required = false) String additionalInfo,
                              @RequestParam(required = false) String weight,
                              @RequestParam(required = false) String dimensions) {
        Optional<Order> orderOptional = orderService.findById(id);
        Set<OrderCategory> orderCategories = orderCategoryService.createOrderCategorySet(categoriesString);

        Optional<Address> deliveryAddress = addressService.createAddress(deliveryAddressString);
        Optional<Address> departureAddress = addressService.createAddress(departureAddressString);

        if (orderOptional.isPresent() && deliveryAddress.isPresent() && departureAddress.isPresent()) {
            addressService.save(departureAddress.get());
            addressService.save(deliveryAddress.get());
            Order order = orderOptional.get();
            order.setCategories(orderCategories);
            order.setCost(cost);
            order.setDepartureAddress(departureAddress.get());
            order.setDeliveryAddress(deliveryAddress.get());
            order.setAdditionalInfo(additionalInfo);
            order.setWeight(weight);
            order.setDimensions(dimensions);
            order.setAmendmentDate(new Date(System.currentTimeMillis()));
            order.setStatus("CHANGED");
            orderService.save(order);
        }
        return "redirect:/account/my-orders/created";
    }
}
