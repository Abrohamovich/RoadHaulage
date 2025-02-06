package ua.ithillel.roadhaulage.controller.main;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.UserRating;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.entity.VerificationToken;
import ua.ithillel.roadhaulage.exception.UserCreateException;
import ua.ithillel.roadhaulage.service.interfaces.EmailService;
import ua.ithillel.roadhaulage.service.interfaces.UserRatingService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;
import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;

import java.util.UUID;

@Controller
@RequestMapping("/register")
@AllArgsConstructor
public class RegistrationController {
    private UserService userService;
    private VerificationTokenService verificationTokenService;
    private EmailService emailService;
    private UserRatingService userRatingService;

    @GetMapping
    public String register(@ModelAttribute("errorMessage") String errorMessage,
                           Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "register";
    }

    @PostMapping("/reg")
    public String register(@RequestParam(required = true) String email,
                           @RequestParam(required = true) String password,
                           @RequestParam(required = true, name = "countryCode") String phoneCode,
                           @RequestParam(required = true) String phone,
                           @RequestParam(required = true) String firstName,
                           @RequestParam(required = true) String lastName, RedirectAttributes redirectAttributes) {
        //todo
        try{
            User user = userService.createUser(email, password, firstName,
                    lastName, phoneCode, phone, true, UserRole.USER);

            userService.save(user);

            UserRating userRating = new UserRating();
            userRating.setAverage(0.0);
            userRating.setCount(0);
            userRating.setUser(user);
            userRatingService.save(userRating);

            String token = UUID.randomUUID().toString();

            VerificationToken verificationToken = verificationTokenService.create(user, token);
            verificationTokenService.save(verificationToken);

            emailService.sendEmailConfirmation(email, token, user);

            redirectAttributes.addFlashAttribute(
                    "attentionMessage",
                    "Please check your email to confirm registration. The link is valid for 20 minutes");
            return "redirect:/login";

        } catch (UserCreateException ex){
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/register";
        }
    }
}
