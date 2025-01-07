package ua.ithillel.roadhaulage.controller.account.settings;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import java.util.regex.Pattern;

@Controller
@RequestMapping("/account/settings/privacy")
@AllArgsConstructor
public class PrivacyController {
    private UserService userService;

    @GetMapping
    public String privacyPage(@AuthenticationPrincipal User user,
                              Model model) {
        model.addAttribute("password", user.getPassword());
        return "account/settings/privacy";
    }

    @PostMapping("/update")
    public String update(@AuthenticationPrincipal User user,
                         @RequestParam String password,
                         RedirectAttributes redirectAttributes) {
        if(!password.isEmpty() || Pattern.compile("\\d").matcher(password).find() ||
                Pattern.compile("[A-Z]").matcher(password).find()) {
            user.setPassword(password);
        } else {
            redirectAttributes.addFlashAttribute("passwordContain", "Password must contain at least one digit and one uppercase");
            return "redirect:/account/settings/privacy";
        }
        userService.update(user);
        return "redirect:/account/settings/privacy";
    }
}
