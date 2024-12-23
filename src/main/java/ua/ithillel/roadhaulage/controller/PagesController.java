package ua.ithillel.roadhaulage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/haulier/account")
    public String haulierAccount() {
        return "haulier/haulierAccount";
    }

    @GetMapping("/buyer/account")
    public String buyerAccount() {
        return "buyer/buyerAccount";
    }
}
