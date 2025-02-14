package ua.ithillel.roadhaulage.controller.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.UserRatingDto;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;
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
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    private final UserRatingService userRatingService;

    @GetMapping
    public String register(@ModelAttribute("errorMessage") String errorMessage,
                           Model model) {
        model.addAttribute("errorMessage", errorMessage);
        return "register";
    }

    @PostMapping("/reg")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam(name = "countryCode") String phoneCode,
                           @RequestParam String phone,
                           @RequestParam String firstName,
                           @RequestParam String lastName, RedirectAttributes redirectAttributes) {
        try{
            UserDto userDto = new UserDto();
            userDto.setEmail(email);
            userDto.setRole(UserRole.USER);
            //todo Change to "false"
            userDto.setEnabled(true);
            userDto.setFirstName(firstName);
            userDto.setLastName(lastName);
            userDto.setPhoneCode(phoneCode);
            userDto.setPhone(phone);
            userDto.setPassword(password);

            userDto = userService.save(userDto);

            UserRatingDto userRatingDto = new UserRatingDto();
            userRatingDto.setAverage(0.0);
            userRatingDto.setCount(0);
            userRatingDto.setUser(userDto);
            userRatingService.save(userRatingDto);

            String token = UUID.randomUUID().toString();

            VerificationTokenDto verificationTokenDto = verificationTokenService.create(userDto, token);
            verificationTokenService.save(verificationTokenDto);

            emailService.sendEmailConfirmation(email, token, userDto);

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
