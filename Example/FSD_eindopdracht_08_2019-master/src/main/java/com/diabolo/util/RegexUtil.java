package com.diabolo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    public static final String VALIDATION_PATTERN = "^[A-Za-z0-9]{100}$";
    //public static final String USER_PATTERN = "(?!(?:[^0-9]*[0-9]){3})^[A-Za-z]{1}[A-Za-z0-9]{3,}$";
    public static final String USER_PATTERN = "(?!(?:[^0-9]*[0-9]){4}|(?:[^\\.]*[\\.]){2})^[A-Za-z]{1}[A-Za-z0-9\\.]{4,38}?[^\\.]{1}$";
    public static final String OLD_PASS_PATTERN = "^(?!(?:[^0-9]*[0-9]){6}|(?:[^!@#$%&_*\\-?]*[!@#$%&_*\\-?]){3})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?:[\\da-zA-Z!@#$%&_*\\-?]){10,32}$";
    public static final String NEW_PASS_PATTERN = "^(?!(?:[^0-9]*[0-9]){4}|(?:[^!@#$%&_*\\-?]*[!@#$%&_*\\-?]){3})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&_*\\-?])(?:[\\da-zA-Z!@#$%&_*\\-?]){10,32}$";
    public static final String PWD_PATTERN = "([=]|[(]|[)]|[']|[\"]|[;]|\\s)";
    public static final String EMAIL_PATTERN = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
    public static final String FIRST_NAME_PATTERN = "^[\\w'][^0-9\\s\\x22\\-,._!¡?÷?¿/\\\\+=@#$%ˆ&*(){}|~<>;:\\[\\]]{1,39}$";
    public static final String LAST_NAME_PATTERN = "^(?!.*\\s{2})([\\w'])[^0-9\\t\\n\\r\\f\\v\\x22\\-,._!¡?÷?¿/\\\\+=@#$%ˆ&*(){}|~<>;:\\[\\]]{1,38}?[^\\s]{1}$";
    public static final String COUNTRY_PATTERN = "^[A-Z]{2}$";

    public static boolean hasValidPattern(String inputToCheck, String patternToCheck) {
        Pattern pattern = Pattern.compile(patternToCheck);
        Matcher matcher = pattern.matcher(inputToCheck);
        return matcher.find();
    }

    /*private static boolean hasValidTest(String input) {
        return hasValidPattern(input, EMAIL_PATTERN);
    }*/

    public static String getPatternByInputName(String inputName) {
        switch (inputName) {
            case "user":
                return USER_PATTERN;
            case "newpassword":
            case "newpassword2":
                return NEW_PASS_PATTERN;
            case "email":
                return EMAIL_PATTERN;
            case "firstname":
                return FIRST_NAME_PATTERN;
            case "lastname":
                return LAST_NAME_PATTERN;
            case "country":
                return COUNTRY_PATTERN;
        }
        return "";
    }
}
