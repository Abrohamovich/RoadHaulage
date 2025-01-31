package ua.ithillel.roadhaulage.controller.account.settings;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.UserService;


import java.util.ArrayList;
import java.util.List;

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
        List<String> codes = getCodes(user.getPhoneCode());
        model.addAttribute("codes", codes);
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("phone", user.getPhone());
        model.addAttribute("iban", user.getIban());
        return "account/settings/personal-information";
    }

    @PostMapping("/update")
    public String update(@AuthenticationPrincipal User user,
                         @RequestParam(required = false) String firstName,
                         @RequestParam(required = false) String lastName,
                         @RequestParam(required = false) String phoneCode,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) String iban,
                         RedirectAttributes redirectAttributes) {
        byte i = 0;
        if (firstName.isEmpty()) {
            firstName = user.getFirstName();
        }
        if (lastName.isEmpty()) {
            lastName = user.getLastName();
        }
        if (iban.isEmpty()) {
            iban = user.getIban();
        }
        if (userService.findByPhoneCodeAndPhone(phoneCode, phone).isPresent()) {
            redirectAttributes.addFlashAttribute("phoneError", "Phone number already exists");
            i++;
        } else if (phone.isEmpty()) {
            phone = user.getPhone();
        }

        if (i > 0) return "redirect:/account/settings/personal-information";

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneCode(phoneCode);
        user.setPhone(phone);
        user.setIban(iban);
        userService.update(user);
        return "redirect:/account/settings/personal-information";
    }

    private static List<String> getCodes(String userPhoneCode) {
        List<String> codes = new ArrayList<>(List.of(
                "376", "355", "374", "43", "994", "375", "32", "387", "359",
                "385", "357", "420", "45", "372", "358", "33", "995", "49",
                "30", "36", "354", "353", "39", "7", "371", "423", "370",
                "352", "356", "373", "377", "382", "31", "389", "47", "48",
                "351", "40", "378", "381", "421", "386", "34", "46",
                "41", "90", "380", "44", "379", "383"
        ));
        codes.remove(userPhoneCode);
        codes.addFirst(userPhoneCode);
        return codes;
    }

}
