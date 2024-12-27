package ua.ithillel.roadhaulage.controller.account.estimate;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account/estimates/in-progress")
@AllArgsConstructor
public class InProgressEstimatesController {

    @GetMapping
    public String inProgressEstimatesPage() {
        return "account/estimates/inProgress";
    }
}
