package ua.ithillel.roadhaulage.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.entity.User;
import ua.ithillel.roadhaulage.repository.UserRepository;
import ua.ithillel.roadhaulage.service.interfaces.EmailService;
import ua.ithillel.roadhaulage.service.interfaces.UserService;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService, UserDetailsService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private EmailService emailService;

    @Override
    public boolean save(User user) {
        User userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB != null) {
            return false;
        }
        user.setEnabled(false);
        userRepository.save(user);
        String confirmUrl = "http://localhost:8080/register/verify-email?token=" + user.getVerificationToken();
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

        emailService.sendEmail(
                user.getEmail(),
                "Confirmation of email address",
                String.format(emailBody, user.getFirstName() + " " +  user.getLastName(), confirmUrl)
        );
        return true;
    }

    public String validateVerificationToken(String token) {
        User user = userRepository.findByVerificationToken(token);
        if(user == null) {
            return "invalid";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public boolean delete(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        Optional<User> userFromDB = userRepository.findById(user.getId());
        if (userFromDB.isPresent() && !userFromDB.get().getPassword().equals(user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
       return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if(user == null) {
            throw new UsernameNotFoundException("Cant find user with username: " + email);
        }
        return user;
    }

}
