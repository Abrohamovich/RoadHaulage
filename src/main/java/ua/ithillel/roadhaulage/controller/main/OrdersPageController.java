package ua.ithillel.roadhaulage.controller.main;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.roadhaulage.dto.OrderCategoryDto;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.List;
import java.util.Set;


@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersPageController {
    private final OrderService orderService;
    private final OrderCategoryService orderCategoryService;

    @GetMapping
    public String ordersPage(@AuthenticationPrincipal UserDto userDto,
                             Model model) {
        List<OrderDto> ordersDto = orderService.returnOtherPublishedOrders(userDto.getId());
        ordersDto.forEach(OrderDto::defineView);
        if(!ordersDto.isEmpty()) addModels(model, ordersDto);
        model.addAttribute("categories", orderCategoryService.findAll());
        return "orders";
    }

    @GetMapping("/filter")
    public String filter(@AuthenticationPrincipal UserDto userDto,
                         @RequestParam(name = "currency") String currency,
                         @RequestParam(name = "categoriesString") String categoriesString,
                         @RequestParam(name = "max-cost") String maxCost,
                         @RequestParam(name = "min-cost") String minCost,
                         Model model) {
        List<OrderDto> ordersDto = orderService.returnOtherPublishedOrders(userDto.getId());
        ordersDto.forEach(OrderDto::defineView);
        if(!currency.equals("ALL")) {
            ordersDto = ordersDto.stream()
                    .filter(order -> order.getCurrency().equals(currency))
                    .toList();
        }
        if(!categoriesString.isEmpty()) {
            Set<OrderCategoryDto> setCategories = orderCategoryService.createOrderCategorySet(categoriesString);
            ordersDto = ordersDto.stream()
                    .filter(order -> order.getCategories().contains(setCategories.iterator().next()))
                    .toList();
        }
        ordersDto = ordersDto.stream()
                .filter(order ->Double.parseDouble(order.getCost()) <= Double.parseDouble(maxCost))
                .filter(order -> Double.parseDouble(order.getCost()) >= Double.parseDouble(minCost))
                .toList();
        if(!ordersDto.isEmpty()) addModels(model, ordersDto);
        model.addAttribute("categories", orderCategoryService.findAll());
        model.addAttribute("categoriesString", categoriesString);
        return "orders";
    }


    private void addModels(Model model, List<OrderDto> ordersDto) {
        double maxCost = ordersDto.stream()
                .map(OrderDto::getCost)
                .mapToDouble(Double::parseDouble)
                .max().getAsDouble();
        double minCost = ordersDto.stream()
                .map(OrderDto::getCost)
                .mapToDouble(Double::parseDouble)
                .min().getAsDouble();
        model.addAttribute("maxCost", maxCost);
        model.addAttribute("minCost", minCost);
        model.addAttribute("orders", ordersDto);
    }
}