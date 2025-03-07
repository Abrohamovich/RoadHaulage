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
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/account/my-orders/create")
@RequiredArgsConstructor
public class CreateNewOrderController {
    private final UserService userService;
    private final OrderService orderService;
    private final OrderCategoryService orderCategoryService;
    private final AddressService addressService;

    @GetMapping
    public String createPage(@AuthenticationPrincipal AuthUserDto authUserDto,
                             @ModelAttribute("errorMessage") String errorMessage,
                             Model model) {
        Optional<UserDto> userDtoOptional = userService.findById(authUserDto.getId());
        UserDto userDto = userDtoOptional.get();
        Date creationDate = new Date(System.currentTimeMillis());
        Map<String, String> map = new HashMap<>();
        map.put("firstName", userDto.getFirstName());
        map.put("lastName", userDto.getLastName());
        map.put("acceptDate", creationDate.toString());
        map.put("errorMessage", errorMessage);
        model.addAllAttributes(map);
        return "account/customer-orders/create";
    }

    @PostMapping("/create")
    public String createOrder(@AuthenticationPrincipal AuthUserDto authUserDto,
                              RedirectAttributes redirectAttributes,
                              @RequestParam String categoryNames,
                              @RequestParam String deliveryAddressString,
                              @RequestParam String departureAddressString,
                              @RequestParam String additionalInfo,
                              @RequestParam String weight,
                              @RequestParam String dimensions,
                              @RequestParam String cost,
                              @RequestParam(name = "weight-unit") String weightUnit,
                              @RequestParam(name = "dimensions-unit") String dimensionsUnit,
                              @RequestParam String currency) {
        Optional<UserDto> userDtoOptional = userService.findById(authUserDto.getId());
        UserDto userDto = userDtoOptional.get();

        Set<OrderCategoryDto> orderCategories = orderCategoryService.createOrderCategorySet(categoryNames);
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

        OrderDto orderDto = new OrderDto();
        orderDto.setCustomer(userDto);
        orderDto.setStatus(OrderStatus.CREATED);
        orderDto.setDeliveryAddress(deliveryAddressDto);
        orderDto.setDepartureAddress(departureAddressDto);
        orderDto.setAdditionalInfo(additionalInfo);
        orderDto.setWeight(weight);
        orderDto.setWeightUnit(weightUnit);
        orderDto.setDimensions(dimensions);
        orderDto.setDimensionsUnit(dimensionsUnit);
        orderDto.setCost(cost);
        orderDto.setCurrency(currency);
        orderDto.setCreationDate(new Date(System.currentTimeMillis()));
        orderDto.setCategories(orderCategories);

        orderService.save(orderDto);

        return "redirect:/account/my-orders/create";
    }

}
