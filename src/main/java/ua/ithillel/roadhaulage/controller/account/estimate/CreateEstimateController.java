package ua.ithillel.roadhaulage.controller.account.estimate;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.ithillel.roadhaulage.entity.Estimate;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.EstimateService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.sql.Date;

@Controller
@RequestMapping("/account/estimates/create")
@AllArgsConstructor
public class CreateEstimateController {
    private EstimateService estimateService;
    private UserService userService;


    @GetMapping
    public String createPage(@AuthenticationPrincipal User loggedUser,
                             Model model){
        Date creationDate = new Date(System.currentTimeMillis());
        model.addAttribute("firstName", loggedUser.getFirstName());
        model.addAttribute("lastName", loggedUser.getLastName());
        model.addAttribute("acceptDate", creationDate);
        return "account/estimates/create";
    }

    @PostMapping("/create")
    public String createEstimate(@AuthenticationPrincipal User user,
                                 @RequestParam(required = true) String category,
                                 @RequestParam(required = true) String deliveryAddress,
                                 @RequestParam(required = true) String departureAddress,
                                 @RequestParam(required = true) String additionalInfo,
                                 @RequestParam(required = true) String weight,
                                 @RequestParam(required = true) String dimensions,
                                 @RequestParam(required = true) String cost){
        Estimate estimate = new Estimate();
        estimate.setCustomer(user);
        estimate.setStatus("PUBLISHED");
        estimate.setCategory(category);
        estimate.setDeliveryAddress(deliveryAddress);
        estimate.setDepartureAddress(departureAddress);
        estimate.setAdditionalInfo(additionalInfo);
        estimate.setWeight(weight);
        estimate.setDimensions(dimensions);
        estimate.setCost(cost);
        estimate.setCreationDate(new Date(System.currentTimeMillis()));
        estimateService.save(estimate);
        return "redirect:/account/estimates/created";
    }

}
