package ua.ithillel.roadhaulage.controller.main;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.VerificationToken;
import ua.ithillel.roadhaulage.service.interfaces.EmailService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;
import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;

import java.util.UUID;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/register")
@AllArgsConstructor
public class RegistrationController {
    private UserService userService;
    private VerificationTokenService verificationTokenService;
    private EmailService emailService;

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
        if(userService.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("emailExists", "Email already exists");
            i++;
        }
        if(!firstName.chars().allMatch(Character::isAlphabetic) ||
                !lastName.chars().allMatch(Character::isAlphabetic)) {
            redirectAttributes.addFlashAttribute("invalidNameContent", "Name can`t contain non-alphabets");
            i++;
        }
        if (!isValidPassword(password)) {
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
        user.setEnabled(false);
        user.setRole("ROLE_USER");
        userService.save(user);

        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = verificationTokenService.create(user, token);
        verificationTokenService.save(verificationToken);

        emailService.sendEmailConfirmation(email, token, user);
        redirectAttributes.addFlashAttribute(
                "attentionMessage",
                "Please check your email to confirm registration. The link is valid for 20 minutes");
        return "redirect:/login";
    }

    private boolean isValidPassword(String password) {
        return Pattern.compile("\\d").matcher(password).find()
                && Pattern.compile("[A-Z]").matcher(password).find();
    }
}
