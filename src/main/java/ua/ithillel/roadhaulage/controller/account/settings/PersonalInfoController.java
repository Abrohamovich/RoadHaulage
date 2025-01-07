package ua.ithillel.roadhaulage.controller.account.settings;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Controller
@RequestMapping("/account/settings/personal-information")
@AllArgsConstructor
public class PersonalInfoController {
    private UserService userService;

    @GetMapping
    public String personalInfoPage(@ModelAttribute("firstNameError") String firstNameError,
                                   @ModelAttribute("lastNameError") String lastNameError,
                                   @ModelAttribute("phoneError") String phoneError,
                                   @AuthenticationPrincipal User user,
                                   Model model) {
            model.addAttribute("firstName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
            model.addAttribute("phone", user.getPhone());
            model.addAttribute("iban", user.getIban());
        return "account/settings/personalInformation";
    }

    @PostMapping("/update")
    public String update(@AuthenticationPrincipal User user,
                         @RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String phone,
                         @RequestParam String iban,
                         RedirectAttributes redirectAttributes) {
        byte i=0;
        if(!firstName.chars().allMatch(Character::isAlphabetic)){
            redirectAttributes.addFlashAttribute("firstNameError", "First name must contain only alphanumeric and can`t be empty");
            i++;
        } else if (firstName.isEmpty()) {
            firstName = user.getFirstName();
        }
        if(!lastName.chars().allMatch(Character::isAlphabetic)){
            redirectAttributes.addFlashAttribute("lastNameError", "Last name must contain only alphanumeric and can`t be empty");
            i++;
        } else if (lastName.isEmpty()) {
            lastName = user.getLastName();
        }
        if(!phone.chars().allMatch(Character::isDigit)){
            redirectAttributes.addFlashAttribute("phoneError", "Write full phone number");
            i++;
        } else if (phone.isEmpty()) {
           phone = user.getPhone();
        }

        if (i>0) return "redirect:/account/settings/personal-information";

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setIban(iban);
        userService.update(user);
        return "redirect:/account/settings/personal-information";
    }
}
