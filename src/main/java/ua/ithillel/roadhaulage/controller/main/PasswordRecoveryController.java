package ua.ithillel.roadhaulage.controller.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;
import ua.ithillel.roadhaulage.service.interfaces.EmailService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;
import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;

import java.util.Optional;
import java.util.UUID;

import static ua.ithillel.roadhaulage.util.PasswordGenerator.generatePassword;

@Controller
@RequestMapping("/password-recovery")
@RequiredArgsConstructor
public class PasswordRecoveryController {
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    private static final String password = generatePassword(10);

    @GetMapping
    public String getPasswordRecoveryPage(@ModelAttribute("attentionMessage") String attentionMessage,
                                          Model model) {
        model.addAttribute("attentionMessage", attentionMessage);
        return "password-recovery";
    }

    @PostMapping("/confirm")
    public String recoverPasswordQues(@RequestParam String email,
                                      RedirectAttributes redirectAttributes){
        Optional<UserDto> userDB = userService.findByEmail(email);
        if(userDB.isEmpty()){
            redirectAttributes.addFlashAttribute(
                    "attentionMessage",
                    "User with email " + email + " not found");
            return "redirect:/password-recovery";
        }

        String token = UUID.randomUUID().toString();
        VerificationTokenDto verificationTokenDto = verificationTokenService.create(userDB.get(), token);
        verificationTokenService.save(verificationTokenDto);
        emailService.sendPasswordResetEmail(email, token, userDB.get(), password);

        redirectAttributes.addFlashAttribute(
                "attentionMessage",
                "Please check your email confirm password recovery");
        return "redirect:/login";
    }

    @GetMapping("recover")
    public String recoverPassword(@RequestParam("token") String token,
                                  RedirectAttributes redirectAttributes) {
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
