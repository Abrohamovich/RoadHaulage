package ua.ithillel.roadhaulage.controller.buyer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/buyer/account/estimates/completed")
@AllArgsConstructor
public class bCompletedEstimatesController {

    @GetMapping
    public String completedEstimatesPage() {
        return "buyer/estimates/completed";
    }
}
