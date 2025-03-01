package ua.ithillel.roadhaulage.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPagesController {

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/about-us")
    public String aboutUs() {
        return "about-us";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}