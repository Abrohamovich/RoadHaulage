package ua.ithillel.roadhaulage.controller.account.settings;

import lombok.RequiredArgsConstructor;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/account/settings/privacy")
@RequiredArgsConstructor
public class PrivacyController {
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    @GetMapping
    public String privacyPage(@AuthenticationPrincipal User user,
                              @ModelAttribute("passwordError") String passwordError,
                              @ModelAttribute("emailError") String emailError,
                              Model model) {
        Map<String, String> map = new HashMap<>();
        map.put("passwordError", passwordError);
        map.put("emailError", emailError);
        map.put("email", user.getEmail());
        model.addAllAttributes(map);
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

        short i = 0;

        if (userService.findByEmail(email).isPresent() && !userService.findByEmail(email).get().getEmail().equals(user.getEmail())) {
            redirectAttributes.addFlashAttribute("emailError", "User with this email already exists");
            i++;
        }

        if (!isValidPassword(password)) {
            redirectAttributes.addFlashAttribute("passwordError",
                    "Password must contain at least one digit and one uppercase letter.");
            i++;
        }

        if (i > 0) return "redirect:/account/settings/privacy";

        if (!password.isEmpty()) {
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
