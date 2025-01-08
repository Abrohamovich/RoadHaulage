package ua.ithillel.roadhaulage.util;

import java.util.Random;

public class PasswordGenerator {

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz" +
            "0123456789";
    private static final int LENGTH = CHARACTERS.length();

    private static final Random random = new Random();

    public static String generatePassword(int length) {
        if (length<=0){
            throw new IllegalArgumentException("Length should be greater than zero");
        }else {
            StringBuilder password = new StringBuilder();
            for (int i = 0; i < length; i++) {
                int randomInt = random.nextInt(LENGTH);
                char randomChar = CHARACTERS.charAt(randomInt);
                password.append(randomChar);

            }
            return password.toString();
        }
    }

}
