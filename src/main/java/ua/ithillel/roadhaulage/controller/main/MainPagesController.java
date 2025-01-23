package ua.ithillel.roadhaulage.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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

    @GetMapping("/login")
    public String login(@ModelAttribute("attentionMessage") String attentionMessage,
                        @ModelAttribute("successMessage") String successMessage,
                        Model model) {
        model.addAttribute("attentionMessage", attentionMessage);
        model.addAttribute("successMessage", successMessage);
        return "login";
    }
}