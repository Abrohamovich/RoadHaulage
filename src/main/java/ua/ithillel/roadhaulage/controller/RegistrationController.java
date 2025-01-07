package ua.ithillel.roadhaulage.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.entity.VerificationToken;
import ua.ithillel.roadhaulage.service.interfaces.EmailService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;
import ua.ithillel.roadhaulage.service.interfaces.VerificationTokenService;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/register")
@AllArgsConstructor
public class RegistrationController {
    private UserService userService;
    private VerificationTokenService verificationTokenService;
    private EmailService emailService;

    @GetMapping
    public String register(@ModelAttribute("emailExists") String emailExists,
                           @ModelAttribute("invalidNameContent") String invalidNameContent,
                           @ModelAttribute("passwordContain") String passwordContain,
                           @ModelAttribute("phoneError") String phoneError,
                           Model model) {
        model.addAttribute("emailExists", emailExists);
        model.addAttribute("invalidNameContent", invalidNameContent);
        model.addAttribute("passwordContain", passwordContain);
        model.addAttribute("phoneError", phoneError);
        return "register";
    }

    @PostMapping
    public String register(@RequestParam(required = true) String email,
                           @RequestParam(required = true) String password,
                           @RequestParam(required = true) String phone,
                           @RequestParam(required = true) String firstName,
                           @RequestParam(required = true) String lastName, RedirectAttributes redirectAttributes) {
        byte i = 0;
        if(userService.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("emailExists", "Email already exists");
            i++;
        }
        if(!firstName.chars().allMatch(Character::isAlphabetic) ||
                !lastName.chars().allMatch(Character::isAlphabetic)) {
            redirectAttributes.addFlashAttribute("invalidNameContent", "Name can`t contain non-alphabets");
            i++;
        }
        if (!Pattern.compile("\\d").matcher(password).find() ||
                !Pattern.compile("[A-Z]").matcher(password).find()) {
            redirectAttributes.addFlashAttribute("passwordContain", "Password must contain at least one digit and one uppercase");
            i++;
        }
        if(!phone.chars().allMatch(Character::isDigit)){
            redirectAttributes.addFlashAttribute("phoneError", "Write full phone number");
            i++;
        }
        if(i>0) return "redirect:/register";

        String emailBody = """
    Hello, %s!
    
    You have provided this email address to register or update your details on our website. To complete the registration process and confirm your address, please click on the link below:
    
    %s
    
    If you have not requested a confirmation email, simply PASS this message. Your account will remain secure and no changes will be made.
    
    If you have any questions or concerns, please contact our support team.
    
    Thank you for using our service!
    
    Regards,
    RoadHaulage Team
    """;

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(false);
        user.setRole("ROLE_USER");
        userService.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiresAt(LocalDateTime.now());
        verificationTokenService.save(verificationToken);

        String confirmUrl = "http://localhost:8080/register/verify-email?token=" + token;

        emailService.sendEmail(
                email,
                "Confirmation of email address",
                String.format(emailBody, firstName + " " + lastName, confirmUrl)
        );
        redirectAttributes.addFlashAttribute("attentionMessage", "Please check your email to confirm your registration");
        return "redirect:/login";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        String validMessage = "You have been successfully verified";
        String invalidMessage = "The verification token is invalid";
        boolean param = userService.verifyEmail(token);
        if (param) {
            redirectAttributes.addFlashAttribute("successMessage", validMessage);
        } else {
            redirectAttributes.addFlashAttribute("successMessage", invalidMessage);
        }
        return "redirect:/login";
    }
}
