package gss.server.util;

public class CredentialUtils {
    
    static final int MaxUserNameLength = 16;
    static final int MinUserNameLength = 1;
    static final int MaxPasswordLength = 16;
    static final int MinPasswordLength = 5;

    public static boolean checkUsernameValidity(String username) {
        if (username.length() >= MinUserNameLength && username.length() <= MaxUserNameLength) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean checkPasswordValidity(String password) {
        if (password.length() >= MinPasswordLength && password.length() <= MaxPasswordLength) {
            return true;
        }
        else {
            return false;
        }
    }

}
