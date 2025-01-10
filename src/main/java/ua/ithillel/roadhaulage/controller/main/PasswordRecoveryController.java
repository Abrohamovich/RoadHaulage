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
import ua.ithillel.roadhaulage.util.PasswordGenerator;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/password-recovery")
@AllArgsConstructor
public class PasswordRecoveryController {
    private UserService userService;
    private VerificationTokenService verificationTokenService;
    private EmailService emailService;

    private static final String password = PasswordGenerator.generatePassword(10);

    @GetMapping
    public String getPasswordRecoveryPage(@ModelAttribute("attentionMessage") String attentionMessage, Model model) {
        model.addAttribute("attentionMessage", attentionMessage);
        return "passwordRecovery";
    }

    @PostMapping("/confirm")
    public String recoverPasswordQues(@RequestParam(required = true) String email, RedirectAttributes redirectAttributes){
        Optional<User> user = userService.findByEmail(email);
        if(user.isEmpty()){
            redirectAttributes.addFlashAttribute(
                    "attentionMessage",
                    "User with email " + email + " not found");
            return "redirect:/password-recovery";
        }

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = verificationTokenService.create(user.get(), token);
        verificationTokenService.save(verificationToken);
        emailService.sendPasswordResetEmail(email, token, user.get(), password);

        redirectAttributes.addFlashAttribute(
                "attentionMessage",
                "Please check your email confirm password recovery");
        return "redirect:/login";
    }

    @GetMapping("recover")
    public String recoverPassword(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        short successId = userService.verifyPassword(token, password);
        String successMessage = switch (successId) {
            case 1 -> "This token does not exist, or this token is not yours";
            case 2 -> "There is no user with this token";
            case 3 -> "Your token has expired";
            default -> "Your password has been successfully changed";
        };

        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        return "redirect:/login";
    }
}
