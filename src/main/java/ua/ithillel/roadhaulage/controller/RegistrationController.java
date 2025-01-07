package ua.ithillel.roadhaulage.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.util.UUID;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/register")
@AllArgsConstructor
public class RegistrationController {
    private UserService userService;

    @GetMapping
    public String register(@ModelAttribute("emailExists") String emailExists,
                           @ModelAttribute("invalidNameContent") String invalidNameContent,
                           @ModelAttribute("passwordContain") String passwordContain,
                           @ModelAttribute("phoneError") String phoneError,
                           Model model) {
        model.addAttribute("emailExists", emailExists);
        model.addAttribute("invalidNameContent", invalidNameContent);
        model.addAttribute("passwordContain", passwordContain);
        model.addAttribute("phoneError", phoneError);
        return "register";
    }

    @PostMapping
    public String register(@RequestParam(required = true) String email,
                           @RequestParam(required = true) String password,
                           @RequestParam(required = true) String phone,
                           @RequestParam(required = true) String firstName,
                           @RequestParam(required = true) String lastName, RedirectAttributes redirectAttributes) {
        byte i = 0;
        if(userService.findByEmail(email) != null) {
            redirectAttributes.addFlashAttribute("emailExists", "Email already exists");
            i++;
        }
        if(!firstName.chars().allMatch(Character::isAlphabetic) ||
                !lastName.chars().allMatch(Character::isAlphabetic)) {
            redirectAttributes.addFlashAttribute("invalidNameContent", "Name can`t contain non-alphabets");
            i++;
        }
        if (!Pattern.compile("\\d").matcher(password).find() ||
                !Pattern.compile("[A-Z]").matcher(password).find()) {
            redirectAttributes.addFlashAttribute("passwordContain", "Password must contain at least one digit and one uppercase");
            i++;
        }
        if(!phone.chars().allMatch(Character::isDigit)){
            redirectAttributes.addFlashAttribute("phoneError", "Write full phone number");
            i++;
        }
        if(i>0) return "redirect:/register";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole("ROLE_USER");
        user.setVerificationToken(UUID.randomUUID().toString());
        userService.save(user);
        redirectAttributes.addFlashAttribute("attentionMessage", "Please check your email to confirm your registration");
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
