package com.binarfud.binarfud_challenge5.servicetest;

import com.binarfud.binarfud_challenge5.dto.PaginationDTO;
import com.binarfud.binarfud_challenge5.dto.UserDTO;
import com.binarfud.binarfud_challenge5.entity.User;
import com.binarfud.binarfud_challenge5.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge5.repository.UserRepository;
import com.binarfud.binarfud_challenge5.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    void checkUserLoginTest() {
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        userRepository.save(user);
        UserDTO userDTO = UserDTO.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Boolean result = userService.checkUserLogin(userDTO);
        Assertions.assertTrue(result);
    }

    @Test
    void checkUserLoginTest_DataNotAvailable() {
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        userRepository.save(user);
        UserDTO userDTO = UserDTO.builder()
                .username("TestUserUsername1")
                .password("TestUserPassword2")
                .email("testemail3@gmail.com")
                .build();
        Boolean result = userService.checkUserLogin(userDTO);
        Assertions.assertFalse(result);
    }

    @Test
    void checkUserLoginTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.checkUserLogin(null));
    }

    @Test
    void checkUsernameAvailabilityTest() {
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        userRepository.save(user);
        UserDTO userDTO = UserDTO.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Boolean result = userService.checkUsernameAvailability(userDTO);
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
        UserDTO userDTO = UserDTO.builder()
                .username("TestUserUsername1")
                .password("TestUserPassword2")
                .email("testemail3@gmail.com")
                .build();
        Boolean result = userService.checkUsernameAvailability(userDTO);
        Assertions.assertFalse(result);
    }

    @Test
    void checkUsernameAvailabilityTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.checkUsernameAvailability(null));
    }

    @Test
    void validateUserWithRegexTest() {
        UserDTO userDTO = UserDTO.builder()
                .username("TestUserUsername")
                .password("TestUserPassword123")
                .email("testemail@gmail.com")
                .build();
        Boolean result = userService.validateUserWithRegex(userDTO);
        Assertions.assertTrue(result);
    }

    @Test
    void validateUserWithRegexTest_NotMatch() {
        UserDTO userDTO = UserDTO.builder()
                .username("WrongUsername")
                .password("WrongPassword")
                .email("WrongEmail")
                .build();
        Boolean result = userService.validateUserWithRegex(userDTO);
        Assertions.assertFalse(result);
    }

    @Test
    void validateUserWithRegexTest_throwIllegalArgumentException1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.validateUserWithRegex(null));
    }

    @Test
    void validateUserWithRegexTest_throwIllegalArgumentException2() {
        UserDTO userDTO = UserDTO.builder()
                .username("TestUserUsername")
                .password(null)
                .email(null)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.validateUserWithRegex(userDTO));
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
        UserDTO userDTO = UserDTO.builder()
                .username("TestUserUsername12")
                .password("TestUserPassword12")
                .email("testemail@gmail.com")
                .build();
        userService.addUser(userDTO);
        User user = userRepository.findByUsername(userDTO.getUsername());
        Assertions.assertEquals("TestUserUsername12", user.getUsername());
        Assertions.assertEquals("TestUserPassword12", user.getPassword());
        Assertions.assertEquals("testemail@gmail.com", user.getEmail());
    }

    @Test
    void addUserTest_throwIllegalArgumentException1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.addUser(null));
    }

    @Test
    void addUserTest_throwIllegalArgumentException2() {
        UserDTO userDTO = UserDTO.builder()
                .username("TestUserUsername12")
                .password(null)
                .email(null)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.addUser(userDTO));
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
        User userCheck = userRepository.findByUsername(newUser.getUsername());
        Assertions.assertEquals("NewTestUserUsername", userCheck.getUsername());
        Assertions.assertEquals("NewTestUserPassword", userCheck.getPassword());
        Assertions.assertEquals("newtestemail@gmail.com", userCheck.getEmail());
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
