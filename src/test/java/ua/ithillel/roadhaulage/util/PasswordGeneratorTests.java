package ua.ithillel.roadhaulage.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordGeneratorTests {

    @Test
    void testGeneratePasswordValidLength() {
        int length = 10;
        String password = PasswordGenerator.generatePassword(length);

        assertNotNull(password, "Password should not be null");
        assertEquals(length, password.length(), "Password length should match the specified length");
    }

    @Test
    void testGeneratePasswordZeroLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordGenerator.generatePassword(0);
        }, "Length should be greater than zero");
    }

    @Test
    void testGeneratePasswordNegativeLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            PasswordGenerator.generatePassword(-5);
        }, "Length should be greater than zero");
    }

    @Test
    void testGeneratePasswordUniqueCharacters() {
        int length = 20;
        String password1 = PasswordGenerator.generatePassword(length);
        String password2 = PasswordGenerator.generatePassword(length);

        assertNotEquals(password1, password2, "Passwords generated should be unique");
    }

    @Test
    void testGeneratePasswordIncludesValidCharacters() {
        int length = 15;
        String password = PasswordGenerator.generatePassword(length);
        String validCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        assertTrue(password.chars().allMatch(c -> validCharacters.indexOf(c) >= 0),
                "Password should contain only valid characters");
    }

}
