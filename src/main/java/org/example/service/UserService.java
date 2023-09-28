package org.example.service;

public interface UserService {
    void setUsername(String username);
    void setPassword(String password);
    void setUserIdFromDatabase(String username, String password);
    int getUserId();
    boolean checkLogin();
    boolean checkUsernameAvailability();
    boolean createUserAccount();
}
