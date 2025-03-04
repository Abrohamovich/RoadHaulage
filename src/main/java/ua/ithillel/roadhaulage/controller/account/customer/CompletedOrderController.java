package ua.ithillel.roadhaulage.controller.account.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.ithillel.roadhaulage.dto.AuthUserDto;
import ua.ithillel.roadhaulage.dto.OrderDto;
import ua.ithillel.roadhaulage.entity.OrderStatus;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.util.List;

@Controller
@RequestMapping("/account/my-orders/completed")
@RequiredArgsConstructor
public class CompletedOrderController {
    private final OrderService orderService;

    @GetMapping("/page={page}")
    public String completedOrdersPage(@AuthenticationPrincipal AuthUserDto authUserDto,
                                      @PathVariable int page, Model model) {
        Page<OrderDto> ordersPage = orderService.findOrdersByCustomerIdAndStatus(authUserDto.getId(), OrderStatus.COMPLETED, page, 10);
        List<OrderDto> orders = ordersPage.getContent()
                .stream()
                .peek(OrderDto::defineView)
                .toList();
        model.addAttribute("orders", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        return "account/customer-orders/completed";
    }
}