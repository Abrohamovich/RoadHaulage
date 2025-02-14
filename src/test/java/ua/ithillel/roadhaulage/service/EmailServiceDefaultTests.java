//package ua.ithillel.roadhaulage.service;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import ua.ithillel.roadhaulage.entity.User;
//
//import java.util.Objects;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class EmailServiceDefaultTests {
//    @Mock
//    private JavaMailSender mailSender;
//    @InjectMocks
//    private EmailServiceDefault service;
//
//    @Test
//    public void sendEmailTest(){
//        String to = "test@example.com";
//        String subject = "Test Subject";
//        String body = "This is the email body.";
//
//        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
//
//        service.sendEmail(to, subject, body);
//
//        verify(mailSender, times(1)).send(messageCaptor.capture());
//        SimpleMailMessage capturedMessage = messageCaptor.getValue();
//        assertEquals(to, Objects.requireNonNull(capturedMessage.getTo())[0]);
//        assertEquals(subject, capturedMessage.getSubject());
//        assertEquals(body, capturedMessage.getText());
//    }
//
//    @Test
//    public void sendEmailConfirmationTest(){
//        String email = "test@example.com";
//        String token = "12345";
//
//        User user = new User();
//        user.setFirstName("John");
//        user.setLastName("Doe");
//
//        String expectedConfirmUrl = "http://localhost:8080/verify-email?token=" + token;
//        String expectedEmailBody = """
//                Hello, John Doe!
//
//                You have provided this email address to register or update your details on our website.
//                To complete the registration process and confirm your address, please click on the link below:
//
//                http://localhost:8080/verify-email?token=12345
//
//                If you have not requested a confirmation email, simply PASS this message.
//                Your account will remain secure and no changes will be made.
//
//                If you have any questions or concerns, please contact our support team.
//
//                Thank you for using our service!
//
//                Regards,
//                RoadHaulage Team
//                """;
//
//        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
//
//        // Act
//        service.sendEmailConfirmation(email, token, user);
//
//        // Assert
//        verify(mailSender).send(messageCaptor.capture());
//        SimpleMailMessage capturedMessage = messageCaptor.getValue();
//
//        assertEquals(email, Objects.requireNonNull(capturedMessage.getTo())[0]);
//        assertEquals("Confirmation of email address", capturedMessage.getSubject());
//        assertEquals(expectedEmailBody, capturedMessage.getText());
//    }
//
//    @Test
//    public void sendPasswordResetEmailTest(){
//        // Arrange
//        String email = "test@example.com";
//        String token = "reset-token-123";
//        String password = "newPassword123";
//        User user = new User();
//        user.setFirstName("John");
//        user.setLastName("Doe");
//
//        String expectedConfirmUrl = "http://localhost:8080/password-recovery/recover?token=" + token;
//        String emailBody = """
//        Hello, %s!
//
//        You have requested to change your password on our website. To confirm this change and update your password, please click on the link below:
//
//        %s
//
//        Your new password is: %s
//
//        If you have not requested a password change, please IGNORE this message. Your account will remain secure, and no changes will be made.
//
//        If you have any questions or concerns, please contact our support team.
//
//        Thank you for using our service!
//
//        Regards, \s
//        RoadHaulage Team
//
//        """;
//        String expectedEmailBody = String.format(emailBody, user.getFirstName() + " " + user.getLastName(), expectedConfirmUrl, password);
//
//        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
//
//        // Act
//        service.sendPasswordResetEmail(email, token, user, password);
//
//        // Assert
//        verify(mailSender).send(messageCaptor.capture());
//        SimpleMailMessage capturedMessage = messageCaptor.getValue();
//
//        assertEquals(email, Objects.requireNonNull(capturedMessage.getTo())[0]);
//        assertEquals("Confirmation of password recovery", capturedMessage.getSubject());
//        assertEquals(expectedEmailBody, capturedMessage.getText());
//    }
//
//}
