package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.entity.User;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendEmailConfirmation(String email, String token, User user);
    void sendPasswordResetEmail(String email, String token, User user, String password);
}
