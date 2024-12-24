package ua.ithillel.roadhaulage.controller.buyer;

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
@RequestMapping("/buyer/account/privacy")
@AllArgsConstructor
public class bPrivacyController {
    private UserService userService;

    @GetMapping
    public String privacyPage(@AuthenticationPrincipal User loggedUser,
                              Model model) {
        model.addAttribute("password", loggedUser.getPassword());
        return "buyer/settings/privacy";
    }

    @PostMapping("/update")
    public String update(@AuthenticationPrincipal User loggedUser,
                         @RequestParam String password) {
        if(!password.isEmpty()) {
            loggedUser.setPassword(password);
            userService.update(loggedUser);
        }
        return "redirect:/buyer/account/privacy";
    }
}
