package com.binarfud.binarfud_challenge6.servicetest;

import com.binarfud.binarfud_challenge6.dto.PaginationDTO;
import com.binarfud.binarfud_challenge6.dto.UserDTO;
import com.binarfud.binarfud_challenge6.dto.request.SignupRequest;
import com.binarfud.binarfud_challenge6.entity.Roles;
import com.binarfud.binarfud_challenge6.entity.User;
import com.binarfud.binarfud_challenge6.enums.ERole;
import com.binarfud.binarfud_challenge6.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge6.repository.UserRepository;
import com.binarfud.binarfud_challenge6.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Signature;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void checkUsernameAvailabilityTest() {
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        userRepository.save(user);
        SignupRequest signupRequest = SignupRequest.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Boolean result = userService.checkUsernameAvailability(signupRequest);
        Assertions.assertTrue(result);
    }

    @Test
    void checkUsernameAvailabilityTest_DataNotAvailable() {
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        userRepository.save(user);
        SignupRequest signupRequest = SignupRequest.builder()
                .username("TestUserUsername1")
                .password("TestUserPassword2")
                .email("testemail3@gmail.com")
                .build();
        Boolean result = userService.checkUsernameAvailability(signupRequest);
        Assertions.assertFalse(result);
    }

    @Test
    void checkUsernameAvailabilityTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.checkUsernameAvailability(null));
    }

    @Test
    void validateUserWithRegexTest() {
        SignupRequest signupRequest = SignupRequest.builder()
                .username("TestUserUsername")
                .password("TestUserPassword123")
                .email("testemail@gmail.com")
                .build();
        Boolean result = userService.validateUserWithRegex(signupRequest);
        Assertions.assertTrue(result);
    }

    @Test
    void validateUserWithRegexTest_NotMatch() {
        SignupRequest signupRequest = SignupRequest.builder()
                .username("WrongUsername")
                .password("WrongPassword")
                .email("WrongEmail")
                .build();
        Boolean result = userService.validateUserWithRegex(signupRequest);
        Assertions.assertFalse(result);
    }

    @Test
    void validateUserWithRegexTest_throwIllegalArgumentException1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.validateUserWithRegex(null));
    }

    @Test
    void validateUserWithRegexTest_throwIllegalArgumentException2() {
        SignupRequest signupRequest = SignupRequest.builder()
                .username("TestUserUsername")
                .password(null)
                .email(null)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.validateUserWithRegex(signupRequest));
    }

    @Test
    void getAllUserWithPagination() {
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        userRepository.save(user);
        PaginationDTO<UserDTO> paginationDTO = userService.getAllUserWithPagination(1);
        UserDTO userDTO = UserDTO.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Assertions.assertEquals(userDTO, paginationDTO.getData().get(0));
        Assertions.assertEquals(1, paginationDTO.getCurrentPage());
        Assertions.assertEquals(1, paginationDTO.getTotalPages());
    }

    @Test
    void getAllUserWithPagination_throwDataNotFoundException() {
        Assertions.assertThrows(DataNotFoundException.class, () -> userService.getAllUserWithPagination(1));
    }

    @Test
    void getAllUserWithPagination_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.getAllUserWithPagination(0));
    }

    @Test
    void addUserTest() {
        Set<String> eRoleSet = new HashSet<>();
        eRoleSet.add(ERole.ROLE_CUSTOMER.name());
        SignupRequest signupRequest = SignupRequest.builder()
                .username("TestUserUsername12")
                .password("TestUserPassword12")
                .email("testemail@gmail.com")
                .role(eRoleSet)
                .build();
        userService.addUser(signupRequest);
        Optional<User> userOptional = userRepository.findByUsername(signupRequest.getUsername());
        if (userOptional.isPresent()) {
            Assertions.assertEquals("TestUserUsername12", userOptional.get().getUsername());
            Assertions.assertEquals("TestUserPassword12", userOptional.get().getPassword());
            Assertions.assertEquals("testemail@gmail.com", userOptional.get().getEmail());
        } else {
            throw new NullPointerException();
        }
    }

    @Test
    void addUserTest_throwIllegalArgumentException1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.addUser(null));
    }

    @Test
    void addUserTest_throwIllegalArgumentException2() {
        SignupRequest signupRequest = SignupRequest.builder()
                .username("TestUserUsername12")
                .password(null)
                .email(null)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.addUser(signupRequest));
    }

    @Test
    void updateUserTest() {
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        userRepository.save(user);
        UserDTO newUser = UserDTO.builder()
                .username("NewTestUserUsername")
                .password("NewTestUserPassword")
                .email("newtestemail@gmail.com")
                .build();
        userService.updateUser(newUser, user.getUsername());
        Optional<User> userOptional = userRepository.findByUsername(newUser.getUsername());
        if (userOptional.isPresent()) {
            Assertions.assertEquals("NewTestUserUsername", userOptional.get().getUsername());
            Assertions.assertEquals("NewTestUserPassword", userOptional.get().getPassword());
            Assertions.assertEquals("newtestemail@gmail.com", userOptional.get().getEmail());
        }
    }

    @Test
    void updateUserTest_throwDataNotFoundException() {
        UserDTO userDTO = UserDTO.builder()
                .username("NewTestUserUsername")
                .password("NewTestUserPassword")
                .email("newtestemail@gmail.com")
                .build();
        Assertions.assertThrows(DataNotFoundException.class, () -> userService.updateUser(userDTO, "WrongUsername"));
    }

    @Test
    void updateUserTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUser(null, null));
    }

    @Test
    void deleteUserTest() {
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        userRepository.save(user);
        userService.deleteUser(user.getUsername());
        Assertions.assertNull(userRepository.findByUsername("TestUserUsername"));
    }

    @Test
    void deleteUserTest_throwDataNotFoundException() {
        Assertions.assertThrows(DataNotFoundException.class, () -> userService.deleteUser("WrongUsername"));
    }

    @Test
    void deleteUserTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(null));
    }
}
