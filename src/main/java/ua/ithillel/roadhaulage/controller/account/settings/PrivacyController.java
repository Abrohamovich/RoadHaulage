package ua.ithillel.roadhaulage.controller.account.settings;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.VerificationToken;
import ua.ithillel.roadhaulage.service.interfaces.EmailService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;
import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/account/settings/privacy")
@AllArgsConstructor
public class PrivacyController {
    private UserService userService;
    private VerificationTokenService verificationTokenService;
    private EmailService emailService;

    @GetMapping
    public String privacyPage(@AuthenticationPrincipal User user,
                              @ModelAttribute("passwordError") String passwordError,
                              Model model) {
        model.addAttribute("password", user.getPassword());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("passwordError", passwordError);
        return "account/settings/privacy";
    }

    @PostMapping("/update")
    public String updateAccount(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email,
            RedirectAttributes redirectAttributes) {

        if (email.isEmpty() && password.isEmpty()) {
            return "redirect:/account/settings/privacy";
        }

        boolean hasErrors = false;

        if (!password.isEmpty() && !isValidPassword(password)) {
            redirectAttributes.addFlashAttribute("passwordError",
                    "Password must contain at least one digit and one uppercase letter.");
            hasErrors = true;
        }

        if (!password.isEmpty() && !password.equals(user.getPassword())) {
            user.setPassword(password);
            userService.update(user);
        }

        if (!email.isEmpty() && !email.equals(user.getEmail())) {
            user.setEmail(email);
            user.setEnabled(false);
            userService.update(user);

            String token = UUID.randomUUID().toString();
            VerificationToken verificationToken = verificationTokenService.create(user, token);
            verificationTokenService.save(verificationToken);
            emailService.sendEmailConfirmation(email, token, user);
            return "redirect:/logout";
        }

        return "redirect:/account/settings/privacy";
    }

    private boolean isValidPassword(String password) {
        return Pattern.compile("\\d").matcher(password).find()
                && Pattern.compile("[A-Z]").matcher(password).find();
    }

}
