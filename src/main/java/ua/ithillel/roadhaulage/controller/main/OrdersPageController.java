package ua.ithillel.roadhaulage.controller.main;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.ithillel.roadhaulage.dto.AuthUserDto;
import ua.ithillel.roadhaulage.dto.OrderCategoryDto;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.entity.OrderStatus;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersPageController {
    private final OrderService orderService;
    private final OrderCategoryService orderCategoryService;

    @GetMapping("/page={page}")
    public String ordersPage(@AuthenticationPrincipal AuthUserDto authUserDto,
                             @PathVariable int page, Model model) {
        Page<OrderDto> ordersPage = orderService.findOrdersByCustomerIdNotAndStatus(authUserDto.getId(), OrderStatus.PUBLISHED, page, 15);
        List<OrderDto> orders = ordersPage.getContent().stream()
                .peek(OrderDto::defineView)
                .toList();
        if(!orders.isEmpty()) addModels(model, orders);
        model.addAttribute("categories", orderCategoryService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        return "orders";
    }

//    @GetMapping("page={page}/filter")
//    public String filter(@AuthenticationPrincipal AuthUserDto authUserDto,
//                         @PathVariable int page,
//                         @RequestParam String currency,
//                         @RequestParam String categoriesString,
//                         @RequestParam(name = "max-cost") String maxCost,
//                         @RequestParam(name = "min-cost") String minCost,
//                         @RequestParam String comparisonType,
//                         Model model) {
//        List<OrderDto> orders = orderService.findOrdersByCustomerIdNotAndStatus(authUserDto.getId(), OrderStatus.PUBLISHED);
//
//        Set<OrderCategoryDto> categoryDtoSet = categoriesString.isEmpty()
//                ? Set.of()
//                : orderCategoryService.createOrderCategorySet(categoriesString);
//
//        orders = orders.stream()
//                .filter(orderDto -> "ALL".equals(currency) || orderDto.getCurrency().equals(currency))
//                .filter(orderDto -> {
//                    double cost = Double.parseDouble(orderDto.getCost());
//                    return cost >= Double.parseDouble(minCost) && cost <= Double.parseDouble(maxCost);
//                })
//                .filter(orderDto -> {
//                    if (categoryDtoSet.isEmpty()) return true;
//                    else if (comparisonType.equals("loose")){
//                        return orderDto.getCategories().stream().anyMatch(categoryDtoSet::contains);
//                    } else if (comparisonType.equals("strict")){
//                        return categoryDtoSet.containsAll(orderDto.getCategories());
//                    }
//                    return false;
//                })
//                .toList();
//
//        if(!orders.isEmpty()) addModels(model, orders);
//
//        model.addAttribute("categories", orderCategoryService.findAll());
//        model.addAttribute("categoriesString", categoriesString);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", ordersPage.getTotalPages());
//        return "orders";
//    }

    private void addModels(Model model, List<OrderDto> orders) {
        DoubleSummaryStatistics statistics = orders.stream()
                .mapToDouble(order -> Double.parseDouble(order.getCost()))
                .summaryStatistics();
        model.addAttribute("maxCost", statistics.getMax());
        model.addAttribute("minCost", statistics.getMin());
        model.addAttribute("orders", orders);
    }
}