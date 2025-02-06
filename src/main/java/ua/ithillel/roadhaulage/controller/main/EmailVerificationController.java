package ua.ithillel.roadhaulage.controller.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

@Controller
@RequiredArgsConstructor
public class EmailVerificationController {
    private final UserService userService;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token,
                              RedirectAttributes redirectAttributes) {
        short successId = userService.verifyEmail(token);
        String successMessage = switch (successId) {
            case 1 -> "This token does not exist, or this token is not yours";
            case 2 -> "There is no user with this token";
            case 3 -> "Your token has expired";
            default -> "You have been successfully verified";
        };

        redirectAttributes.addFlashAttribute("successMessage", successMessage);
        return "redirect:/login";
    }
}
