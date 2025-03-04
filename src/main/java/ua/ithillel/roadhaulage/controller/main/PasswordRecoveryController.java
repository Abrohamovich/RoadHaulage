package ua.ithillel.roadhaulage.controller.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;
import ua.ithillel.roadhaulage.service.interfaces.EmailService;
import ua.ithillel.roadhaulage.service.interfaces.RegisterService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;
import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/password-recovery")
@RequiredArgsConstructor
public class PasswordRecoveryController {
    private final UserService userService;
    private final RegisterService registerService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    @GetMapping
    public String getPasswordRecoveryRequestPage(@ModelAttribute("attentionMessage") String attentionMessage,
                                                 Model model) {
        model.addAttribute("attentionMessage", attentionMessage);
        return "password-recovery";
    }

    @GetMapping("/change")
    public String getPasswordRecoveryPage(@ModelAttribute("email") String email, Model model) {
        model.addAttribute("email", email);
        return "change-password";
    }

    @PostMapping("/change/confirm")
    public String changePassword(@RequestParam String email,
                                 @RequestParam String password) {
        Optional<UserDto> userDtoOptional = userService.findByEmail(email);
        if (userDtoOptional.isPresent()) {
            UserDto userDto = userDtoOptional.get();
            Optional<VerificationTokenDto> verificationTokenDtoOptional =
                    verificationTokenService.findByUser(userDto);

            VerificationTokenDto verificationTokenDto = verificationTokenDtoOptional.get();

            userDto.setPassword(password);
            registerService.update(userDto);

            verificationTokenService.delete(verificationTokenDto);

            return "redirect:/login";
        }
        return "redirect:/error";
    }

    @PostMapping("/confirm")
    public String sendRecoveryRequest(@RequestParam String email,
                                      RedirectAttributes redirectAttributes) {
        Optional<UserDto> userDB = userService.findByEmail(email);
        if (userDB.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "attentionMessage",
                    "User with email " + email + " not found");
            return "redirect:/password-recovery";
        }

        String token = UUID.randomUUID().toString();
        VerificationTokenDto verificationTokenDto = new VerificationTokenDto();
        verificationTokenDto.setToken(token);
        verificationTokenDto.setExpiresAt(LocalDateTime.now().plusMinutes(20));
        verificationTokenDto.setUser(userDB.get());
        verificationTokenService.save(verificationTokenDto);
        emailService.sendPasswordResetEmail(email, token, userDB.get());

        redirectAttributes.addFlashAttribute(
                "attentionMessage",
                "Please check your email confirm password recovery");
        return "redirect:/login";
    }

    @GetMapping("recover")
    public String checkRecoveryRequest(@RequestParam("token") String token,
                                       RedirectAttributes redirectAttributes) {
        short successId = userService.verifyPassword(token);
        String successMessage = switch (successId) {
            case 1 -> "This token does not exist, or this token is not yours";
            case 2 -> "There is no user with this token";
            case 3 -> "Your token has expired";
            default -> "success";
        };

        Optional<VerificationTokenDto> verificationTokenDto = verificationTokenService.getToken(token);
        if (verificationTokenDto.isPresent() && successMessage.equals("success")) {
            String email = verificationTokenDto.get().getUser().getEmail();
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/password-recovery/change";
        }

        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        return "redirect:/login";
    }
}
