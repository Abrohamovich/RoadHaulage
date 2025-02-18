package ua.ithillel.roadhaulage.controller.account.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.dto.*;
import ua.ithillel.roadhaulage.entity.OrderStatus;
import ua.ithillel.roadhaulage.exception.AddressCreateException;
import ua.ithillel.roadhaulage.service.interfaces.AddressService;
import ua.ithillel.roadhaulage.service.interfaces.OrderCategoryService;
import ua.ithillel.roadhaulage.service.interfaces.OrderService;

import java.sql.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/account/my-orders/change")
@RequiredArgsConstructor
public class ChangeOrderController {
    private final OrderService orderService;
    private final OrderCategoryService orderCategoryService;
    private final AddressService addressService;

    @GetMapping
    public String changePage(@AuthenticationPrincipal AuthUserDto authUserDto,
                             @RequestParam long id,
                             @ModelAttribute("errorMessage") String errorMessage,
                             Model model) {
        Optional<OrderDto> orderOptional = orderService.findById(id);
        if (orderOptional.isPresent()) {
            OrderDto orderDto = orderOptional.get();
            if (!orderDto.getCustomer().getId().equals(authUserDto.getId())) {
                return "redirect:/error";
            }
            orderDto.defineView();
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("order", orderDto);
            return "account/customer-orders/change";
        }
        return "redirect:/error";
    }

    @PostMapping("/edit")
    public String changeOrder(@AuthenticationPrincipal AuthUserDto authUserDto,
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
        Optional<OrderDto> orderDtoOptional = orderService.findById(id);
        OrderDto orderDto = orderDtoOptional.get();

        if (!orderDto.getCustomer().getId().equals(authUserDto.getId())) {
            return "redirect:/error";
        }

        Set<OrderCategoryDto> orderCategories = orderCategoryService.createOrderCategorySet(categoriesString);
        orderCategories = orderCategories.stream()
                .map(orderCategoryService::save)
                .collect(Collectors.toSet());

        AddressDto deliveryAddressDto;
        AddressDto departureAddressDto;

        try {
            deliveryAddressDto = addressService.createAddress(deliveryAddressString);
            departureAddressDto = addressService.createAddress(departureAddressString);
        } catch (AddressCreateException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/account/my-orders/create";
        }

        departureAddressDto = addressService.save(departureAddressDto);
        deliveryAddressDto = addressService.save(deliveryAddressDto);


        orderDto.setCategories(orderCategories);
        orderDto.setCost(cost);
        orderDto.setCurrency(currency);
        orderDto.setDepartureAddress(departureAddressDto);
        orderDto.setDeliveryAddress(deliveryAddressDto);
        orderDto.setAdditionalInfo(additionalInfo);
        orderDto.setWeight(weight);
        orderDto.setWeightUnit(weightUnit);
        orderDto.setDimensions(dimensions);
        orderDto.setDimensionsUnit(dimensionsUnit);
        orderDto.setAmendmentDate(new Date(System.currentTimeMillis()));
        orderDto.setStatus(OrderStatus.CHANGED);
        orderService.save(orderDto);

        return "redirect:/account/my-orders/created";
    }
}
