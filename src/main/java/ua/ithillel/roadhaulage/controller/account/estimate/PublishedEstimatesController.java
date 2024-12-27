package ua.ithillel.roadhaulage.controller.account.estimate;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.ithillel.roadhaulage.entity.Estimate;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.EstimateService;

import java.util.List;

@Controller
@RequestMapping("/account/estimates/published")
@AllArgsConstructor
public class PublishedEstimatesController {
    private EstimateService estimateService;

    @GetMapping
    public String publishedEstimatesPage(@AuthenticationPrincipal User user,
                                         Model model) {
        List<Estimate> estimates = estimateService.findEstimatesByCustomerId(user.getId());
        model.addAttribute("estimates", estimates);
        return "account/estimates/published";
    }

}
