package ua.ithillel.roadhaulage.controller.main;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.ithillel.roadhaulage.dto.AuthUserDto;
import ua.ithillel.roadhaulage.entity.AuthRequest;
import ua.ithillel.roadhaulage.service.interfaces.UserService;
import ua.ithillel.roadhaulage.util.JwtUtil;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public String showLoginPage(@ModelAttribute("attentionMessage") String attentionMessage,
                                @ModelAttribute("successMessage") String successMessage,
                                Model model) {
        model.addAttribute("authRequest", new AuthRequest());
        model.addAttribute("attentionMessage", attentionMessage);
        model.addAttribute("successMessage", successMessage);
        return "login";
    }

    @PostMapping
    public String login(@ModelAttribute AuthRequest authRequest,
                        RedirectAttributes redirectAttributes,
                        HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
            AuthUserDto user = (AuthUserDto) userService.loadUserByUsername(authRequest.getEmail());
            String token = jwtUtil.generateToken(user);

            Cookie cookie = new Cookie("jwtToken", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return "redirect:/home";
        } catch (AuthenticationException e) {
            redirectAttributes.addFlashAttribute("attentionMessage", "Invalid credentials");
            return "redirect:/login";
        }
    }
}
