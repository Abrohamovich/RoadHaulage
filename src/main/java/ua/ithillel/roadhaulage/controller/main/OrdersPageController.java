package ua.ithillel.roadhaulage.controller.main;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.roadhaulage.entity.Order;
import ua.ithillel.roadhaulage.entity.OrderCategory;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.List;
import java.util.Set;


@Controller
@AllArgsConstructor
@RequestMapping("/orders")
public class OrdersPageController {
    private OrderService orderService;
    private OrderCategoryService orderCategoryService;

    private List<OrderCategory> allOrderCategories;

    @PostConstruct
    public void init(){
        this.allOrderCategories  = orderCategoryService.findAll();
    }

    @GetMapping
    public String ordersPage(@AuthenticationPrincipal User user,
                             Model model) {
        List<Order> orders = orderService.returnOtherPublishedOrders(user.getId());
        if(!orders.isEmpty()) addModels(model, orders);
        model.addAttribute("categories", allOrderCategories);
        return "orders";
    }

    @GetMapping("/filter")
    public String filter(@AuthenticationPrincipal User user,
                         @RequestParam(name = "currency") String currency,
                         @RequestParam(name = "categoriesString") String categoriesString,
                         @RequestParam(name = "max-cost") String maxCost,
                         @RequestParam(name = "min-cost") String minCost,
                         Model model) {
        List<Order> orders = orderService.returnOtherPublishedOrders(user.getId());
        if(!currency.equals("ALL")) {
            orders = orders.stream()
                    .filter(order -> order.getCurrency().equals(currency))
                    .toList();
        }
        if(!categoriesString.isEmpty()) {
            Set<OrderCategory> setCategories = orderCategoryService.createOrderCategorySet(categoriesString);
            orders = orders.stream()
                    .filter(order -> order.getCategories().contains(setCategories.iterator().next()))
                    .toList();
        }
        orders = orders.stream()
                .filter(order ->Double.parseDouble(order.getCost()) <= Double.parseDouble(maxCost))
                .filter(order -> Double.parseDouble(order.getCost()) >= Double.parseDouble(minCost))
                .toList();
        if(!orders.isEmpty()) addModels(model, orders);
        model.addAttribute("categories", allOrderCategories);
        model.addAttribute("categoriesString", categoriesString);
        return "orders";
    }

    private void addModels(Model model, List<Order> orders) {
        double maxCost = orders.stream()
                .map(Order::getCost)
                .mapToDouble(Double::parseDouble)
                .max().getAsDouble();
        double minCost = orders.stream()
                .map(Order::getCost)
                .mapToDouble(Double::parseDouble)
                .min().getAsDouble();
        model.addAttribute("maxCost", maxCost);
        model.addAttribute("minCost", minCost);
        model.addAttribute("orders", orders);
    }
}