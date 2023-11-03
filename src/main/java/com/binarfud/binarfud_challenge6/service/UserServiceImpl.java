package com.binarfud.binarfud_challenge6.service;

import com.binarfud.binarfud_challenge6.dto.PaginationDTO;
import com.binarfud.binarfud_challenge6.dto.UserDTO;
import com.binarfud.binarfud_challenge6.dto.request.SignupRequest;
import com.binarfud.binarfud_challenge6.entity.Roles;
import com.binarfud.binarfud_challenge6.entity.User;
import com.binarfud.binarfud_challenge6.enums.ERole;
import com.binarfud.binarfud_challenge6.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge6.repository.RoleRepository;
import com.binarfud.binarfud_challenge6.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Method untuk mengecek ketersediaan username
     * @param signupRequest
     * @return
     */
    @Override
    public Boolean checkUsernameAvailability(SignupRequest signupRequest) {
        Optional<SignupRequest> signupRequestOptional = Optional.ofNullable(signupRequest);
        if (signupRequestOptional.isPresent()) {
            log.debug("Checking username availability with username = {}", signupRequest.getUsername());
            return userRepository.existsByUsername(signupRequest.getUsername());
        } else {
            throw new IllegalArgumentException("User cannot be null");
        }
    }

    @Override
    public Boolean checkEmailAvailability(SignupRequest signupRequest) {
        Optional<SignupRequest> signupRequestOptional = Optional.ofNullable(signupRequest);
        if (signupRequestOptional.isPresent()) {
            log.debug("Checking email availability with email = {}", signupRequest.getEmail());
            return userRepository.existsByEmail(signupRequest.getEmail());
        } else {
            throw new IllegalArgumentException("User cannot be null");
        }
    }

    /**
     * Method untuk memvalidasi data menggunakan regex
     * @param signupRequest
     * @return
     */
    @Override
    public Boolean validateUserWithRegex(SignupRequest signupRequest) {
        Optional<SignupRequest> signupRequestOptional = Optional.ofNullable(signupRequest);
        if (signupRequestOptional.isPresent()) {
            Optional<String> usernameOptional = Optional.ofNullable(signupRequest.getUsername());
            Optional<String> passwordOptional = Optional.ofNullable(signupRequest.getPassword());
            Optional<String> emailOptional = Optional.ofNullable(signupRequest.getEmail());
            if (usernameOptional.isPresent() && passwordOptional.isPresent() && emailOptional.isPresent()) {
                User user = convertUserDTOToUser(signupRequest);
                log.debug("Validating user data using regex with username = {}", signupRequest.getUsername());
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
    public User convertUserDTOToUser(SignupRequest signupRequest) {
        log.debug("Converting userDTO to User with username = {}", signupRequest.getUsername());
        return User.builder()
                .username(signupRequest.getUsername())
                .password(signupRequest.getPassword())
                .email(signupRequest.getEmail())
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
     * @param signupRequest
     */
    @Override
    public void addUser(SignupRequest signupRequest) {
        Optional<SignupRequest> signupRequestOptional = Optional.ofNullable(signupRequest);
        if (signupRequestOptional.isPresent()) {
            Optional<String> usernameOptional = Optional.ofNullable(signupRequest.getUsername());
            Optional<String> passwordOptional = Optional.ofNullable(signupRequest.getPassword());
            Optional<String> emailOptional = Optional.ofNullable(signupRequest.getEmail());
            log.debug("Saving User with username = {}", signupRequest.getUsername());
            if (usernameOptional.isPresent() && passwordOptional.isPresent() && emailOptional.isPresent()) {
                User user = convertUserDTOToUser(signupRequest);
                Set<String> strRoles = signupRequest.getRole();
                Set<Roles> roles = new HashSet<>();
                if(strRoles == null) {
                    Roles role = roleRepository.findByRoleName(ERole.ROLE_CUSTOMER)
                            .orElseThrow(() -> new DataNotFoundException("Error: Role"));
                    roles.add(role);
                } else {
                    strRoles.forEach(role -> {
                        Roles role1 = roleRepository.findByRoleName(ERole.valueOf(role))
                                .orElseThrow(() -> new DataNotFoundException("Error: Role " + role ));
                        roles.add(role1);
                    });
                }
                user.setRoles(roles);
                userRepository.save(user);
                log.info("Saving User successful with username = {}", signupRequest.getUsername());
            } else {
                log.error("Saving User unsuccessful with username = {}", signupRequest.getUsername());
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
            Optional<User> userOptional = userRepository.findByUsername(oldUsername);
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
            Optional<User> userOptional = userRepository.findByUsername(username);
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
