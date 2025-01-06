package ua.ithillel.roadhaulage.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.util.UUID;

@Controller
@RequestMapping("/register")
@AllArgsConstructor
public class RegistrationController {
    private UserService userService;

    @GetMapping
    public String register() {
        return "register";
    }

    @PostMapping
    public String register(@RequestParam(required = true) String email,
                           @RequestParam (required = true) String password,
                           @RequestParam (required = true) String firstName,
                           @RequestParam (required = true) String lastName, RedirectAttributes redirectAttributes) {
        if (!(email.isEmpty() & password.isEmpty())) {
            System.out.println(email + " " + password);
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setRole("ROLE_USER");
            user.setVerificationToken(UUID.randomUUID().toString());
            userService.save(user);
            redirectAttributes.addFlashAttribute("attentionMessage", "Please check your email to confirm your registration");
        }
        return "redirect:/login";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        String result = userService.validateVerificationToken(token);
        String validMessage = "You have been successfully verified";
        String invalidMessage = "The verification token is invalid";
        if (result.equals("valid")) {
            redirectAttributes.addFlashAttribute("successMessage", validMessage);
        } else {
            redirectAttributes.addFlashAttribute("successMessage", invalidMessage);
        }
        return "redirect:/login";
    }
}
