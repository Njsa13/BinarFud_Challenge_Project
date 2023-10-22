package com.binarfud.binarfud_challenge5.service;

import com.binarfud.binarfud_challenge5.dto.PaginationDTO;
import com.binarfud.binarfud_challenge5.dto.UserDTO;
import com.binarfud.binarfud_challenge5.entity.User;
import com.binarfud.binarfud_challenge5.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge5.repository.UserRepository;
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
public class UserServiceImpl implements UserService{

    private static final String USERNAMEREGEX = "^[a-zA-Z0-9]{4,}$";
    private static final String PASSWORDREGEX = "^(?=.*[a-zA-Z])(?=.*\\d).{6,}$";
    private static final String EMAILREGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private final Pattern usernamePattern = Pattern.compile(USERNAMEREGEX);
    private final Pattern passwordPattern = Pattern.compile(PASSWORDREGEX);
    private final Pattern emailPattern = Pattern.compile(EMAILREGEX);

    @Autowired
    private UserRepository userRepository;

    /**
     * Method untuk mengecek login user
     * @param userDTO
     * @return
     */
    @Override
    public Boolean checkUserLogin(UserDTO userDTO) {
        Optional<UserDTO> userDTOOptional = Optional.ofNullable(userDTO);
        if (userDTOOptional.isPresent()) {
            log.debug("Checking user data availability with username = {}", userDTO.getUsername());
            return userRepository.existsByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
        } else {
            throw new IllegalArgumentException("User cannot be null");
        }
    }

    /**
     * Method untuk mengecek ketersediaan username
     * @param userDTO
     * @return
     */
    @Override
    public Boolean checkUsernameAvailability(UserDTO userDTO) {
        Optional<UserDTO> userDTOOptional = Optional.ofNullable(userDTO);
        if (userDTOOptional.isPresent()) {
            log.debug("Checking username availability with username = {}", userDTO.getUsername());
            return userRepository.existsByUsername(userDTO.getUsername());
        } else {
            throw new IllegalArgumentException("User cannot be null");
        }
    }

    /**
     * Method untuk memvalidasi data menggunakan regex
     * @param userDTO
     * @return
     */
    @Override
    public Boolean validateUserWithRegex(UserDTO userDTO) {
        Optional<UserDTO> userDTOOptional = Optional.ofNullable(userDTO);
        if (userDTOOptional.isPresent()) {
            Optional<String> usernameOptional = Optional.ofNullable(userDTO.getUsername());
            Optional<String> passwordOptional = Optional.ofNullable(userDTO.getPassword());
            Optional<String> emailOptional = Optional.ofNullable(userDTO.getEmail());
            if (usernameOptional.isPresent() && passwordOptional.isPresent() && emailOptional.isPresent()) {
                User user = convertUserDTOToUser(userDTO);
                log.debug("Validating user data using regex with username = {}", userDTO.getUsername());
                Matcher usernameMatcher = usernamePattern.matcher(user.getUsername());
                Matcher passwordMatcher = passwordPattern.matcher(user.getPassword());
                Matcher emailMatcher = emailPattern.matcher(user.getEmail());
                return usernameMatcher.matches() && passwordMatcher.matches() && emailMatcher.matches();
            } else {
                throw new IllegalArgumentException("Username, Password, and Email cannot be null");
            }
        } else {
            throw new IllegalArgumentException("User cannot be null");
        }
    }

    @Override
    public User convertUserDTOToUser(UserDTO userDTO) {
        log.debug("Converting userDTO to User with username = {}", userDTO.getUsername());
        return User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .build();
    }

    /**
     * Method untuk get semua user
     * @param page
     * @return
     */
    @Override
    public PaginationDTO<UserDTO> getAllUserWithPagination(Integer page) {
        log.debug("Getting User with pagination with current page = {}", page);
        Page<User> userPage = Optional.ofNullable(page)
                .map(val -> {
                    if (val < 1) {
                        throw new IllegalArgumentException("Page index must not be less than one");
                    }
                    return userRepository.findAll(PageRequest.of(val-1, 5));
                })
                .orElse(userRepository.findAll(PageRequest.of(0, 5)));
        if (userPage.isEmpty()) {
            throw new DataNotFoundException("Users");
        }
        List<UserDTO> userDTOS = userPage.getContent().stream()
                .map(val -> UserDTO.builder()
                        .username(val.getUsername())
                        .password(val.getPassword())
                        .email(val.getEmail())
                        .build())
                .collect(Collectors.toList());
        log.info("Getting User with pagination successful with current page = {}", page);
        return new PaginationDTO<>(userDTOS, page, userPage.getTotalPages());
    }

    /**
     * Method untuk menambah user
     * @param userDTO
     */
    @Override
    public void addUser(UserDTO userDTO) {
        Optional<UserDTO> userDTOOptional = Optional.ofNullable(userDTO);
        if (userDTOOptional.isPresent()) {
            Optional<String> usernameOptional = Optional.ofNullable(userDTO.getUsername());
            Optional<String> passwordOptional = Optional.ofNullable(userDTO.getPassword());
            Optional<String> emailOptional = Optional.ofNullable(userDTO.getEmail());
            log.debug("Saving User with username = {}", userDTO.getUsername());
            if (usernameOptional.isPresent() && passwordOptional.isPresent() && emailOptional.isPresent()) {
                userRepository.save(convertUserDTOToUser(userDTO));
                log.info("Saving User successful with username = {}", userDTO.getUsername());
            } else {
                log.error("Saving User unsuccessful with username = {}", userDTO.getUsername());
                throw new IllegalArgumentException("Username, Password, and Email cannot be null");
            }
        } else {
            throw new IllegalArgumentException("User cannot be null");
        }
    }

    /**
     * Method untuk update user
     * @param userDTO
     * @param oldUsername
     */
    @Override
    public void updateUser(UserDTO userDTO, String oldUsername) {
        Optional<UserDTO> userDTOOptional = Optional.ofNullable(userDTO);
        Optional<String> oldUsernameOptional = Optional.ofNullable(oldUsername);
        if (userDTOOptional.isPresent() && oldUsernameOptional.isPresent()) {
            Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(oldUsername));
            log.debug("Updating User with username = {}", userDTO.getUsername());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setUsername(userDTO.getUsername());
                user.setPassword(userDTO.getPassword());
                user.setEmail(userDTO.getEmail());
                userRepository.save(user);
                log.info("Updating User successful with username = {}", userDTO.getUsername());
            } else {
                log.error("Updating User unsuccessful with username = {}", userDTO.getUsername());
                throw new DataNotFoundException("User with username = "+oldUsername);
            }
        } else {
            throw new IllegalArgumentException("User and old username cannot be null");
        }
    }

    /**
     * Method untuk hapus user
     * @param username
     */
    @Override
    public void deleteUser(String username) {
        Optional<String> usernameOptional = Optional.ofNullable(username);
        if (usernameOptional.isPresent()) {
            Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));
            log.debug("Deleting User with username = {}", username);
            if (userOptional.isPresent()) {
                userRepository.deleteById(userOptional.get().getUserId());
                log.info("Deleting User successful with username = {}", username);
            } else {
                log.error("Deleting User unsuccessful with username = {}", username);
                throw new DataNotFoundException("User with username = "+username);
            }
        } else {
            throw new IllegalArgumentException("Username cannot be null");
        }
    }
}
