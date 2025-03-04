package ua.ithillel.roadhaulage.controller.account.settings;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.dto.AuthUserDto;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.service.interfaces.RegisterService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.util.*;

@Controller
@RequestMapping("/account/settings/personal-information")
@RequiredArgsConstructor
public class PersonalInfoController {
    private final UserService userService;
    private final RegisterService registerService;

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

    @GetMapping
    public String personalInfoPage(@ModelAttribute("firstNameError") String firstNameError,
                                   @ModelAttribute("lastNameError") String lastNameError,
                                   @ModelAttribute("phoneError") String phoneError,
                                   @AuthenticationPrincipal AuthUserDto authUserDto,
                                   Model model) {
        UserDto userDto = userService.findById(authUserDto.getId()).get();
        List<String> codes = getCodes(userDto.getCountryCode());
        Map<String, String> map = new HashMap<>();
        map.put("firstName", userDto.getFirstName());
        map.put("lastName", userDto.getLastName());
        map.put("localPhone", userDto.getLocalPhone());
        map.put("iban", userDto.getIban());
        model.addAttribute("codes", codes);
        model.addAllAttributes(map);
        return "account/settings/personal-information";
    }

    @PostMapping("/update")
    public String update(@AuthenticationPrincipal AuthUserDto authUserDto,
                         @RequestParam(required = false) String firstName,
                         @RequestParam(required = false) String lastName,
                         @RequestParam(required = false) String countryCode,
                         @RequestParam(required = false) String localPhone,
                         @RequestParam(required = false) String iban,
                         RedirectAttributes redirectAttributes) {
        Optional<UserDto> userDtoOptional = userService.findById(authUserDto.getId());
        UserDto userDto = userDtoOptional.get();
        if (firstName.isEmpty()) {
            firstName = userDto.getFirstName();
        }
        if (lastName.isEmpty()) {
            lastName = userDto.getLastName();
        }
        if (iban.isEmpty()) {
            iban = userDto.getIban();
        }
        if (userService.findByCountryCodeAndLocalPhone(countryCode, localPhone).isPresent() &&
                !userDto.getLocalPhone().equals(localPhone)) {
            redirectAttributes.addFlashAttribute("phoneError",
                    "Phone number already exists");
            return "redirect:/account/settings/personal-information";
        } else if (localPhone.isEmpty()) {
            localPhone = userDto.getLocalPhone();
        }

        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setCountryCode(countryCode);
        userDto.setLocalPhone(localPhone);
        userDto.setIban(iban);
        registerService.update(userDto);

        return "redirect:/account/settings/personal-information";
    }

}
