package ua.ithillel.roadhaulage.controller.account.myOrders;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.entity.Order;

import java.sql.Date;
import java.util.Optional;

@Controller
@RequestMapping("/account/my-orders/change")
@AllArgsConstructor
public class ChangeOrderController {
    private OrderService orderService;

    @PostMapping()
    public String changePage(@RequestParam("id") long id, Model model) {
        Optional<Order> order = orderService.findById(id);
        model.addAttribute("id", id);
        model.addAttribute("category", order.get().getCategory());
        model.addAttribute("departureAddress", order.get().getDepartureAddress());
        model.addAttribute("deliveryAddress", order.get().getDeliveryAddress());
        model.addAttribute("additionalInfo", order.get().getAdditionalInfo());
        model.addAttribute("weight", order.get().getWeight());
        model.addAttribute("dimensions", order.get().getDimensions());
        model.addAttribute("cost", order.get().getCost());
        return "account/myOrders/change";
    }

    @PostMapping("/edit")
    public String changeOrder(@RequestParam long id,
                              @RequestParam(required = false) String category,
                              @RequestParam(required = false) String cost,
                              @RequestParam(required = false) String departureAddress,
                              @RequestParam(required = false) String deliveryAddress,
                              @RequestParam(required = false) String additionalInfo,
                              @RequestParam(required = false) String weight,
                              @RequestParam(required = false) String dimensions) {
        Optional<Order> order = orderService.findById(id);
        order.get().setCategory(category);
        order.get().setCost(cost);
        order.get().setDepartureAddress(departureAddress);
        order.get().setDeliveryAddress(deliveryAddress);
        order.get().setAdditionalInfo(additionalInfo);
        order.get().setWeight(weight);
        order.get().setDimensions(dimensions);
        order.get().setAmendmentDate(new Date(System.currentTimeMillis()));
        order.get().setStatus("CHANGED");
        orderService.save(order.get());
        return "redirect:/account/my-orders/created";
    }
}
