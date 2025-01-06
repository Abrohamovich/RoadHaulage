package ua.ithillel.roadhaulage.controller.account.settings;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

@Controller
@RequestMapping("/account/settings/privacy")
@AllArgsConstructor
public class PrivacyController {
    private UserService userService;

    @GetMapping
    public String privacyPage(@AuthenticationPrincipal User loggedUser,
                              Model model) {
        model.addAttribute("password", loggedUser.getPassword());
        model.addAttribute("email", loggedUser.getEmail());
        return "account/settings/privacy";
    }

    @PostMapping("/update")
    public String update(@AuthenticationPrincipal User loggedUser,
                         @RequestParam String password,
                         @RequestParam String email) {
        if(!email.isEmpty()) {
            loggedUser.setEmail(email);
        }
        if(!password.isEmpty()) {
            loggedUser.setPassword(password);
        }
        userService.update(loggedUser);
        return "redirect:/account/settings/privacy";
    }
}
