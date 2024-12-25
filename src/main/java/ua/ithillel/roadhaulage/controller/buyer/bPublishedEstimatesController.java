package ua.ithillel.roadhaulage.controller.buyer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.EstimateService;

@Controller
@RequestMapping("/buyer/account/estimates/published")
@AllArgsConstructor
public class bPublishedEstimatesController {
    private EstimateService estimateService;

    @GetMapping
    public String publishedEstimatesPage(@AuthenticationPrincipal User user,
                                         Model model) {

        return "buyer/estimates/published";
    }

}
