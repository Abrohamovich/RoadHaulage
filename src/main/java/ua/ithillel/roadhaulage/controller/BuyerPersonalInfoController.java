package ua.ithillel.roadhaulage.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

@Controller
@RequestMapping("/buyer/account/personalInformation")
@AllArgsConstructor
public class BuyerPersonalInfoController {
    private UserService userService;

    @GetMapping
    public String personalInfoPage(@AuthenticationPrincipal User loggedUser, Model model) {
            model.addAttribute("email", loggedUser.getEmail());
            model.addAttribute("firstName", loggedUser.getFirstName());
            model.addAttribute("lastName", loggedUser.getLastName());
            model.addAttribute("phone", loggedUser.getPhone());
            model.addAttribute("iban", loggedUser.getIban());
        return "buyer/settings/personalInformation";
    }

    @PostMapping("/update")
    public String update(@AuthenticationPrincipal User loggedUser,
                         @RequestParam(required = false) String firstName,
                         @RequestParam(required = false) String lastName,
                         @RequestParam(required = false) String email,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) String iban){
        loggedUser.setFirstName(firstName);
        loggedUser.setLastName(lastName);
        loggedUser.setEmail(email);
        loggedUser.setPhone(phone);
        loggedUser.setIban(iban);
        userService.save(loggedUser);
        return "redirect:/buyer/account/personalInformation";
    }
}
