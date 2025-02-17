package ua.ithillel.roadhaulage.controller.main;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.roadhaulage.dto.AuthUserDto;
import ua.ithillel.roadhaulage.dto.OrderCategoryDto;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersPageController {
    private final OrderService orderService;
    private final OrderCategoryService orderCategoryService;
    private final UserService userService;

    @GetMapping
    public String ordersPage(@AuthenticationPrincipal AuthUserDto authUserDto,
                             Model model) {
        List<OrderDto> ordersDto = getUserOrders(authUserDto);
        if(!ordersDto.isEmpty()) addModels(model, ordersDto);
        model.addAttribute("categories", orderCategoryService.findAll());
        return "orders";
    }

    @GetMapping("/filter")
    public String filter(@AuthenticationPrincipal AuthUserDto authUserDto,
                         @RequestParam String currency,
                         @RequestParam String categoriesString,
                         @RequestParam(name = "max-cost") String maxCost,
                         @RequestParam(name = "min-cost") String minCost,
                         @RequestParam String comparisonType,
                         Model model) {
        List<OrderDto> ordersDto = getUserOrders(authUserDto);
        ordersDto.forEach(OrderDto::defineView);

        Set<OrderCategoryDto> categoryDtoSet = categoriesString.isEmpty()
                ? Set.of()
                : orderCategoryService.createOrderCategorySet(categoriesString);

        ordersDto = ordersDto.stream()
                .filter(orderDto -> "ALL".equals(currency) || orderDto.getCurrency().equals(currency))
                .filter(orderDto -> {
                    double cost = Double.parseDouble(orderDto.getCost());
                    return cost >= Double.parseDouble(minCost) && cost <= Double.parseDouble(maxCost);
                })
                .filter(orderDto -> {
                    if (categoryDtoSet.isEmpty()) return true;
                    else if (comparisonType.equals("loose")){
                        return orderDto.getCategories().stream().anyMatch(categoryDtoSet::contains);
                    } else if (comparisonType.equals("strict")){
                        return orderDto.getCategories().containsAll(categoryDtoSet);
                    }
                    return false;
                })
                .toList();

        if(!ordersDto.isEmpty()) addModels(model, ordersDto);

        model.addAttribute("categories", orderCategoryService.findAll());
        model.addAttribute("categoriesString", categoriesString);

        return "orders";
    }

    private void addModels(Model model, List<OrderDto> ordersDto) {
        DoubleSummaryStatistics statistics = ordersDto.stream()
                .mapToDouble(orderDto -> Double.parseDouble(orderDto.getCost()))
                .summaryStatistics();
        model.addAttribute("maxCost", statistics.getMax());
        model.addAttribute("minCost", statistics.getMin());
        model.addAttribute("orders", ordersDto);
    }

    private List<OrderDto> getUserOrders(AuthUserDto authUserDto) {
        Optional<UserDto> userDto = userService.findById(authUserDto.getId());
        List<OrderDto> ordersDto = orderService.returnOtherPublishedOrders(userDto.get().getId());
        ordersDto.forEach(OrderDto::defineView);
        return ordersDto;
    }
}