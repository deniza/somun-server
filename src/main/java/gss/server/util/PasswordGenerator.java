package gss.server.util;

import java.util.Random;

public class PasswordGenerator {

    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyz0123456789";

    private static Random random = new Random();

    public static String generatePassword(int length) {

        StringBuilder builder = new StringBuilder();        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(index));
        }
        
        return builder.toString();

    }

}
