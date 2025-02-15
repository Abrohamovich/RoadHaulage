package ua.ithillel.roadhaulage.controller.account.settings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.UserService;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/account/settings/personal-information")
@RequiredArgsConstructor
@Slf4j
public class PersonalInfoController {
    private final UserService userService;

    @GetMapping
    public String personalInfoPage(@ModelAttribute("firstNameError") String firstNameError,
                                   @ModelAttribute("lastNameError") String lastNameError,
                                   @ModelAttribute("phoneError") String phoneError,
                                   @AuthenticationPrincipal UserDto userDto,
                                   Model model) {
        List<String> codes = getCodes(userDto.getPhoneCode());
        Map<String, String> map = new HashMap<>();
        map.put("firstName", userDto.getFirstName());
        map.put("lastName", userDto.getLastName());
        map.put("phone", userDto.getPhone());
        map.put("iban", userDto.getIban());
        model.addAttribute("codes", codes);
        model.addAllAttributes(map);
        return "account/settings/personal-information";
    }

    @PostMapping("/update")
    public String update(@AuthenticationPrincipal UserDto userDto,
                         @RequestParam(required = false) String firstName,
                         @RequestParam(required = false) String lastName,
                         @RequestParam(required = false) String phoneCode,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) String iban,
                         RedirectAttributes redirectAttributes) {
        if (firstName.isEmpty()) {
            firstName = userDto.getFirstName();
        }
        if (lastName.isEmpty()) {
            lastName = userDto.getLastName();
        }
        if (iban.isEmpty()) {
            iban = userDto.getIban();
        }
        if (userService.findByPhoneCodeAndPhone(phoneCode, phone).isPresent() &&
                !userDto.getPhone().equals(phone)) {
            redirectAttributes.addFlashAttribute("phoneError",
                    "Phone number already exists");
            return "redirect:/account/settings/personal-information";
        } else if (phone.isEmpty()) {
            phone = userDto.getPhone();
        }

        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setPhoneCode(phoneCode);
        userDto.setPhone(phone);
        userDto.setIban(iban);
        userService.update(userDto);


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
