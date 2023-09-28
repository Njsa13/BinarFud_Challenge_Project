package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;

public class UserServiceImpl implements UserService{
    private User user = new User();

    @Override
    public void setUsername(String username) {
        this.user.setUsername(username);
    }

    @Override
    public void setPassword(String password) {
        this.user.setPassword(password);
    }

    /**
     * Method untuk mengambil data dari repository
     * @param username
     * @param password
     */
    @Override
    public void setUserIdFromDatabase(String username, String password) {
        UserRepository userRepository = new UserRepository();
        int id = Integer.parseInt(userRepository.getIdByUsernameAndPassword(username, password).get(0));
        this.user.setUserId(id);
    }

    @Override
    public int getUserId() {
        return this.user.getUserId();
    }

    /**
     * Method untuk validasi / mengecek kredensial
     * @return
     */
    @Override
    public boolean checkLogin() {
        UserRepository userRepository = new UserRepository();
        return !userRepository.getIdByUsernameAndPassword(this.user.getUsername(), this.user.getPassword()).isEmpty();
    }

    /**
     * Method untuk mengecek ketersediaan username
     * @return
     */
    @Override
    public boolean checkUsernameAvailability() {
        UserRepository userRepository = new UserRepository();
        return !userRepository.getIdByUsername(this.user.getUsername()).isEmpty();
    }

    /**
     * Method untuk membuat akun baru
     * @return
     */
    @Override
    public boolean createUserAccount() {
        UserRepository userRepository = new UserRepository();
        return userRepository.addUsernameAndPassword(this.user.getUsername(), this.user.getPassword());
    }
}
