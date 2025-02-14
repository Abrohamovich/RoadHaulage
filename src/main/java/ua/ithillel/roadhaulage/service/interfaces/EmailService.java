package ua.ithillel.roadhaulage.service.interfaces;

import ua.ithillel.roadhaulage.dto.UserDto;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendEmailConfirmation(String email, String token, UserDto userDto);
    void sendPasswordResetEmail(String email, String token, UserDto userDto, String password);
}
