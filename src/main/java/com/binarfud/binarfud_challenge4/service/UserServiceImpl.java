package com.binarfud.binarfud_challenge4.service;

import com.binarfud.binarfud_challenge4.dto.UserDTO;
import com.binarfud.binarfud_challenge4.dto.UserPageDTO;
import com.binarfud.binarfud_challenge4.entity.Merchant;
import com.binarfud.binarfud_challenge4.entity.User;
import com.binarfud.binarfud_challenge4.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final String USERNAMEREGEX = "^[a-zA-Z0-9]{4,}$";
    private static final String PASSWORDREGEX = "^(?=.*[a-zA-Z])(?=.*\\d).{6,}$";
    private static final String EMAILREGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    Pattern usernamePattern = Pattern.compile(USERNAMEREGEX);
    Pattern passwordPattern = Pattern.compile(PASSWORDREGEX);
    Pattern emailPattern = Pattern.compile(EMAILREGEX);
    @Autowired
    private UserRepository userRepository;

    /**
     * Method untuk check login user
     * @param userDTO
     * @return
     */
    @Override
    public Boolean checkUserLogin(UserDTO userDTO) {
        return Optional.ofNullable(userDTO)
                .map(val -> userRepository.findByUsernameAndPassword(val.getUsername(), val.getPassword()))
                .map(val -> {
                    log.info("User is available with username : {}", val.getUsername());
                    return true;
                })
                .orElseGet(() -> {
                    log.info("User is not available with username : {}", userDTO.getUsername());
                    return false;
                });
    }

    /**
     * Method untuk mengecek ketersediaan username
     * @param userDTO
     * @return
     */
    @Override
    public Boolean checkUsernameAvailability(UserDTO userDTO) {
        return Optional.ofNullable(userRepository.findByUsername(userDTO.getUsername()))
                .map(val -> {
                    log.info("User is available with username : {}", val.getUsername());
                    return true;
                })
                .orElseGet(() -> {
                    log.info("User is not available with username : {}", userDTO.getUsername());
                    return false;
                });
    }

    /**
     * Method untuk validasi username, password, dan email dengan regex
     * @param userDTO
     * @return
     * @throws NullPointerException
     */
    @Override
    public Boolean validateUserWithRegex(UserDTO userDTO) throws NullPointerException {
        User user = convertUserDTOToUser(userDTO);
        Matcher usernameMatcher = usernamePattern.matcher(user.getUsername());
        Matcher passwordMatcher = passwordPattern.matcher(user.getPassword());
        Matcher emailMatcher = emailPattern.matcher(user.getEmail());

        return usernameMatcher.matches() && passwordMatcher.matches() && emailMatcher.matches();
    }

    /**
     * Method untuk mengambil user menggunakan paging
     * @param page
     * @return
     */
    @Override
    public UserPageDTO getAllUser(Integer page) {
        Page<User> userPage = Optional.ofNullable(page)
                .map(val -> userRepository.findAll(PageRequest.of(val, 5)))
                .orElse(userRepository.findAll(PageRequest.of(0, 5)));

        List<UserDTO> userDTOS = userPage.getContent().stream()
                .map(val -> UserDTO.builder()
                        .username(val.getUsername())
                        .password(val.getPassword())
                        .email(val.getEmail())
                        .build())
                .collect(Collectors.toList());
        log.info("Getting user page successful with page : {} and total page : {}", page, userPage.getTotalPages());
        return UserPageDTO.builder()
                .userDTOS(userDTOS)
                .totalPages(userPage.getTotalPages())
                .build();
    }

    @Override
    public String getUserIdByIndexAndPage(Integer index, Integer page) {
        List<User> users = userRepository.findAll(PageRequest.of(page, 5)).getContent();
        return users.get(index - 1).getUserId();
    }

    /**
     * Method untuk menambah user account
     * @param userDTO
     * @return
     * @throws NullPointerException
     */
    @Override
    public Boolean addUserAccount(UserDTO userDTO) throws NullPointerException {
        User user = convertUserDTOToUser(userDTO);
        log.info("Saving user successful with username : {}", userDTO.getUsername());
        return Optional.ofNullable(user)
                .map(val -> userRepository.save(val))
                .isPresent();
    }

    /**
     * Method untuk update user
     * @param userDTO
     * @return
     * @throws NullPointerException
     */
    @Override
    public Boolean updateUser(UserDTO userDTO) throws NullPointerException {
        String userId = getUserIdByIndexAndPage(userDTO.getIndex(), userDTO.getPage());
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
            user.setEmail(userDTO.getEmail());

            userRepository.save(user);
            log.info("Update user successfull with username : {}", userDTO.getUsername());
            return true;
        } else {
            log.error("Update user unsuccessfull with username : {}", userDTO.getUsername());
            return false;
        }
    }

    @Override
    public User convertUserDTOToUser(UserDTO userDTO) throws NullPointerException {
        return User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .build();
    }

    /**
     * Method untuk menghapus user
     * @param userDTO
     */
    @Override
    public void deleteUser(UserDTO userDTO) {
        String userId = getUserIdByIndexAndPage(userDTO.getIndex(), userDTO.getPage());
        userRepository.deleteById(userId);
        log.info("Delete user successfull with username : {}", userDTO.getUsername());
    }
}
