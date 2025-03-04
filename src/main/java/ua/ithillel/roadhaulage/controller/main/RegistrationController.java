package ua.ithillel.roadhaulage.controller.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.dto.UserRatingDto;
import ua.ithillel.roadhaulage.dto.VerificationTokenDto;
import ua.ithillel.roadhaulage.entity.UserRole;
import ua.ithillel.roadhaulage.exception.UserCreateException;
import ua.ithillel.roadhaulage.service.interfaces.EmailService;
import ua.ithillel.roadhaulage.service.interfaces.RegisterService;
import ua.ithillel.roadhaulage.service.interfaces.UserRatingService;
import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegisterService registerService;
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
                           @RequestParam(name = "countryCode") String countryCode,
                           @RequestParam String localPhone,
                           @RequestParam String firstName,
                           @RequestParam String lastName, RedirectAttributes redirectAttributes) {
        try {
            UserDto userDto = new UserDto();
            userDto.setEmail(email);
            userDto.setRole(UserRole.USER);
            userDto.setEnabled(false);
            userDto.setFirstName(firstName);
            userDto.setLastName(lastName);
            userDto.setCountryCode(countryCode);
            userDto.setLocalPhone(localPhone);
            userDto.setPassword(password);

            userDto = registerService.register(userDto);

            UserRatingDto userRatingDto = new UserRatingDto();
            userRatingDto.setAverage(0.0);
            userRatingDto.setCount(0);
            userRatingDto.setUser(userDto);
            userRatingService.save(userRatingDto);

            String token = UUID.randomUUID().toString();

            VerificationTokenDto verificationTokenDto = new VerificationTokenDto();
            verificationTokenDto.setToken(token);
            verificationTokenDto.setExpiresAt(LocalDateTime.now().plusMinutes(20));
            verificationTokenDto.setUser(userDto);

            verificationTokenService.save(verificationTokenDto);

            emailService.sendEmailConfirmation(email, token, userDto);

            redirectAttributes.addFlashAttribute(
                    "attentionMessage",
                    "Please check your email to confirm registration. The link is valid for 20 minutes");
            return "redirect:/login";

        } catch (UserCreateException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/register";
        }
    }
}
