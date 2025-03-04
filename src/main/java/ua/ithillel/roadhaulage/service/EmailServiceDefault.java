package ua.ithillel.roadhaulage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ua.ithillel.roadhaulage.dto.UserDto;
import ua.ithillel.roadhaulage.service.interfaces.EmailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceDefault implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        log.info("Sending email to: {}, with subject: {}", to, subject);
        mailSender.send(message);
    }

    @Override
    public void sendEmailConfirmation(String email, String token, UserDto userDto) {
        String confirmUrl = "http://localhost:8080/verify-email?token=" + token;
        String emailBody = """
                Hello, %s!
                
                You have provided this email address to register or update your details on our website.
                To complete the registration process and confirm your address, please click on the link below:
                
                %s
                
                If you have not requested a confirmation email, simply PASS this message.
                Your account will remain secure and no changes will be made.
                
                If you have any questions or concerns, please contact our support team.
                
                Thank you for using our service!
                
                Regards,
                RoadHaulage Team
                """;
        sendEmail(email, "Confirmation of email address",
                String.format(emailBody, userDto.getFirstName() + " " + userDto.getLastName(), confirmUrl));
    }

    @Override
    public void sendPasswordResetEmail(String email, String token, UserDto userDto) {
        String confirmUrl = "http://localhost:8080/password-recovery/recover?token=" + token;
        String emailBody = """
                Hello, %s!
                
                You have requested to change your password on our website. To confirm this change and update your password, please click on the link below:
                
                %s
                
                If you have not requested a password change, please IGNORE this message. Your account will remain secure, and no changes will be made.
                
                If you have any questions or concerns, please contact our support team.
                
                Thank you for using our service!
                
                Regards, \s
                RoadHaulage Team
                
                """;
        sendEmail(email, "Confirmation of password recovery",
                String.format(emailBody, userDto.getFirstName() + " " + userDto.getLastName(), confirmUrl));
    }
}
