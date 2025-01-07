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
            saveVerificationToken(user, token);
            sendEmailConfirmation(email, token, user);

            redirectAttributes.addFlashAttribute("attentionMessage",
                    "Please check your email to confirm your registration.");
            return "redirect:/logout";
        }

        return "redirect:/account/settings/privacy";
    }

    private boolean isValidPassword(String password) {
        return Pattern.compile("\\d").matcher(password).find()
                && Pattern.compile("[A-Z]").matcher(password).find();
    }

    private void saveVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiresAt(LocalDateTime.now().plusHours(24)); // Set token expiry
        verificationTokenService.save(verificationToken);
    }

    private void sendEmailConfirmation(String email, String token, User user) {
        String confirmUrl = "http://localhost:8080/register/verify-email?token=" + token;
        String emailBody = """
        Hello, %s!
        
        You have provided this email address to register or update your details on our website. 
        To complete the registration process and confirm your address, please click on the link below:
        
        %s
        
        If you have not requested a confirmation email, simply PASS this message. 
        Your account will remain secure and no changes will be made.
        
        If you have any questions or concerns, please contact our support team.
        
        Thank you for using our service!
        
        Regards,
        RoadHaulage Team
        """;
        emailService.sendEmail(email, "Confirmation of email address",
                String.format(emailBody, user.getFirstName() + " " + user.getLastName(), confirmUrl));
    }

}
