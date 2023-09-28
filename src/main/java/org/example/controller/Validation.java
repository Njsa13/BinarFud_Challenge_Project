package org.example.controller;

import org.example.service.UserService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    Alert alert = new Alert();

    private String usernameRegex = "^[a-zA-Z0-9]{4,}$";
    private String passwordRegex = "^(?=.*[a-zA-Z])(?=.*\\d).{6,}$";

    Pattern usernamePattern = Pattern.compile(usernameRegex);
    Pattern passwordPattern = Pattern.compile(passwordRegex);

    /**
     * Method untuk validasi login
     * @param username
     * @param password
     * @param userService
     * @return
     */
    public boolean loginValidation(String username, String password, UserService userService) throws NullPointerException {
        userService.setUsername(username);
        userService.setPassword(password);
        if (userService.checkLogin()) {
            alert.loginSuccess();
            return true;
        } else {
            alert.loginFail();
            return false;
        }
    }

    /**
     * Method untuk validasi sign up
     * @param  username
     * @param  password
     * @param  userService
     * @return
     */
    public boolean signUpValidation(String username, String password, UserService userService) {
        Matcher usernameMatcher = usernamePattern.matcher(username);
        Matcher passwordMatcher = passwordPattern.matcher(password);

        if (!usernameMatcher.matches()) {
            alert.wrongUsernameFormat();
            return true;
        }
        if (!passwordMatcher.matches()) {
            alert.wrongPasswordFormat();
            return true;
        }

        userService.setUsername(username);
        if (userService.checkUsernameAvailability()) {
            userService.setPassword(password);
            if (userService.createUserAccount()) {
                alert.signUpSuccess();
            } else {
                alert.signUpFail();
            }
            return false;
        } else {
            alert.usernameAvailable();
            return true;
        }
    }
}
