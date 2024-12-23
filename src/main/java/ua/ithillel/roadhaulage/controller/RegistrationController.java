package ua.ithillel.roadhaulage.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

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
                           @RequestParam (required = true, defaultValue = "!@") String lastName,
                           @RequestParam (required = true, defaultValue = "!@") String userRole) {
        if (!(email.equals("!@") & password.equals("!@"))) {
            System.out.println(email + " " + password);
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setRole(userRole);
            if (userService.save(user))
                System.out.println("GOOD");
            else
                System.out.println("BAD");
        }
        return "redirect:/login";
    }
}
