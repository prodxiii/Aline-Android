package coolgroup.com.aline.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxVerifier {

    /**
     * Checks if an email address is of valid syntax.
     *
     * @param email The email to be verified
     * @return Expression of how valid the email is.
     */
    public static boolean verifyEmail(String email) {

        // This email regex string given by http://emailregex.com/
        String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        Pattern p = Pattern.compile(emailRegex);
        Matcher m = p.matcher(email);
        return m.find();
    }

    /**
     * Checks that a password meets a set of given conditions.
     *
     * @param password The password to be verified.
     * @return Expression of how valid the password is.
     */
    public static boolean verifyPassword(String password) {
        return (password.length() > 5);
    }

}
