package ua.ithillel.roadhaulage.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.EmailService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.util.UUID;

@Controller
@RequestMapping("/register")
@AllArgsConstructor
public class RegistrationController {
    private UserService userService;

    @GetMapping
    public String regPage() {
        return "register";
    }

    @PostMapping
    public String register(@RequestParam(required = true, defaultValue = "!@") String email,
                           @RequestParam (required = true, defaultValue = "!@") String password,
                           @RequestParam (required = true, defaultValue = "!@") String firstName,
                           @RequestParam (required = true, defaultValue = "!@") String lastName) {
        if (!(email.equals("!@") & password.equals("!@"))) {
            System.out.println(email + " " + password);
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setRole("ROLE_USER");
            user.setVerificationToken(UUID.randomUUID().toString());
            userService.save(user);

        }
        return "redirect:/verify-email";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);
        if (result.equals("valid")) {
            return "redirect:/login";
        } else {
            return "redirect:/home";
        }
    }
}
