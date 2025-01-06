package ua.ithillel.roadhaulage.service.interfaces;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
