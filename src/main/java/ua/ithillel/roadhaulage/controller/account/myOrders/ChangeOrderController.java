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

@Controller
@RequestMapping("/account/my-orders/change")
@AllArgsConstructor
public class ChangeOrderController {
    private OrderService orderService;

    @PostMapping()
    public String changePage(@RequestParam("id") long id, Model model) {
        Order order = orderService.findById(id);
        model.addAttribute("id", id);
        model.addAttribute("category", order.getCategory());
        model.addAttribute("departureAddress", order.getDepartureAddress());
        model.addAttribute("deliveryAddress", order.getDeliveryAddress());
        model.addAttribute("additionalInfo", order.getAdditionalInfo());
        model.addAttribute("weight", order.getWeight());
        model.addAttribute("dimensions", order.getDimensions());
        model.addAttribute("cost", order.getCost());
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
        Order order = orderService.findById(id);
        order.setCategory(category);
        order.setCost(cost);
        order.setDepartureAddress(departureAddress);
        order.setDeliveryAddress(deliveryAddress);
        order.setAdditionalInfo(additionalInfo);
        order.setWeight(weight);
        order.setDimensions(dimensions);
        order.setAmendmentDate(new Date(System.currentTimeMillis()));
        order.setStatus("CHANGED");
        orderService.save(order);
        return "redirect:/account/my-orders/created";
    }
}
