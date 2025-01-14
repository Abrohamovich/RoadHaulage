package ua.ithillel.roadhaulage.controller.account.settings;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

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
        return "account/settings/personal-information";
    }

    @PostMapping("/update")
    public String update(@AuthenticationPrincipal User user,
                         @RequestParam(required = false) String firstName,
                         @RequestParam(required = false) String lastName,
                         @RequestParam(name = "countryCode", required = false) String phoneCode,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) String iban,
                         RedirectAttributes redirectAttributes) {
        byte i=0;
        if (firstName.isEmpty()) {
            firstName = user.getFirstName();
        }
        if (lastName.isEmpty()) {
            lastName = user.getLastName();
        }
        if (userService.findByPhone(phoneCode + phone).isPresent()) {
            redirectAttributes.addFlashAttribute("phoneError", "Phone number already exists");
            i++;
        } else if (phone.isEmpty()) {
            phone = user.getPhone();
            phoneCode="";
        }

        if (i>0) return "redirect:/account/settings/personal-information";

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phoneCode + phone);
        user.setIban(iban);
        userService.update(user);
        return "redirect:/account/settings/personal-information";
    }
}
