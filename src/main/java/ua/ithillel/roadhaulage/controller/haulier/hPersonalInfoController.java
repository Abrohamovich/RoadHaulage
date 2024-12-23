package ua.ithillel.roadhaulage.controller.haulier;

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

import java.util.Map;
import java.util.function.Consumer;

@Controller
@RequestMapping("/haulier/account/personalInformation")
@AllArgsConstructor
public class hPersonalInfoController {
    private UserService userService;

    @GetMapping
    public String personalInfoPage(@AuthenticationPrincipal User loggedUser,
                                   Model model) {
        model.addAttribute("email", loggedUser.getEmail());
        model.addAttribute("firstName", loggedUser.getFirstName());
        model.addAttribute("lastName", loggedUser.getLastName());
        model.addAttribute("phone", loggedUser.getPhone());
        model.addAttribute("iban", loggedUser.getIban());
        return "haulier/settings/personalInformation";
    }

    @PostMapping("/update")
    public String update(@AuthenticationPrincipal User loggedUser,
                         @RequestParam Map<String, String> params) {
        Map<String, Consumer<String>> fieldSetters = Map.of(
                "firstName", loggedUser::setFirstName,
                "lastName", loggedUser::setLastName,
                "email", loggedUser::setEmail,
                "phone", loggedUser::setPhone,
                "iban", loggedUser::setIban
        );
        params.forEach((key, value) -> {
            if (fieldSetters.containsKey(key) && value != null && !value.isEmpty()) {
                fieldSetters.get(key).accept(value);
            }
        });
        userService.update(loggedUser);
        return "redirect:/haulier/account/personalInformation";
    }
}
