package ua.ithillel.roadhaulage.controller.account.order;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account/my-orders/completed")
@AllArgsConstructor
public class CompletedOrderController {

    @GetMapping
    public String completedEstimatesPage() {
        return "account/myOrders/completed";
    }
}
