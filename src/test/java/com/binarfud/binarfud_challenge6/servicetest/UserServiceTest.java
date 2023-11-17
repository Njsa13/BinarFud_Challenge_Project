package com.binarfud.binarfud_challenge6.servicetest;

import com.binarfud.binarfud_challenge6.dto.PaginationDTO;
import com.binarfud.binarfud_challenge6.dto.UserDTO;
import com.binarfud.binarfud_challenge6.dto.request.SignupRequest;
import com.binarfud.binarfud_challenge6.entity.Roles;
import com.binarfud.binarfud_challenge6.entity.User;
import com.binarfud.binarfud_challenge6.enums.ERole;
import com.binarfud.binarfud_challenge6.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge6.repository.RoleRepository;
import com.binarfud.binarfud_challenge6.repository.UserRepository;
import com.binarfud.binarfud_challenge6.service.UserService;
import com.binarfud.binarfud_challenge6.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Signature;
import java.util.*;

@AutoConfigureMockMvc
@SpringBootTest
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void checkUsernameAvailabilityTest() {
        SignupRequest signupRequest = SignupRequest.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Mockito.when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(true);
        Boolean result = userService.checkUsernameAvailability(signupRequest);
        Mockito.verify(userRepository, Mockito.times(1))
                .existsByUsername(signupRequest.getUsername());
        Assertions.assertTrue(result);
    }

    @Test
    void checkUsernameAvailabilityTest_DataNotAvailable() {
        SignupRequest signupRequest = SignupRequest.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Mockito.when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(false);
        Boolean result = userService.checkUsernameAvailability(signupRequest);
        Mockito.verify(userRepository, Mockito.times(1))
                .existsByUsername(signupRequest.getUsername());
        Assertions.assertFalse(result);
    }

    @Test
    void checkUsernameAvailabilityTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.checkUsernameAvailability(null));
    }

    @Test
    void getAllUserWithPagination() {
        List<User> users = Collections.singletonList(User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build());
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(0, 5), 1);
        Mockito.when(userRepository.findAll(PageRequest.of(0, 5))).thenReturn(userPage);
        PaginationDTO<UserDTO> paginationDTO = userService.getAllUserWithPagination(1);
        UserDTO userDTO = UserDTO.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Mockito.verify(userRepository, Mockito.times(2))
                .findAll(PageRequest.of(0, 5));
        Assertions.assertEquals(userDTO, paginationDTO.getData().get(0));
        Assertions.assertEquals(1, paginationDTO.getCurrentPage());
        Assertions.assertEquals(1, paginationDTO.getTotalPages());
    }

    @Test
    void getAllUserWithPagination_throwDataNotFoundException() {
        Mockito.when(userRepository.findAll(PageRequest.of(0, 5))).thenReturn(Page.empty());
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
        Mockito.when(roleRepository.findByRoleName(ERole.ROLE_CUSTOMER)).thenReturn(Optional.of(new Roles()));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        userService.addUser(signupRequest);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(roleRepository, Mockito.times(1)).findByRoleName(ERole.ROLE_CUSTOMER);
        Assertions.assertDoesNotThrow(() -> userService.addUser(signupRequest));
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
        String oldUsername = "TestUserUsername";
        UserDTO newUser = UserDTO.builder()
                .username("NewTestUserUsername")
                .password("NewTestUserPassword")
                .email("newtestemail@gmail.com")
                .build();
        Mockito.when(userRepository.findByUsername(oldUsername)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        userService.updateUser(newUser, oldUsername);
        Mockito.verify(userRepository, Mockito.times(1))
                .findByUsername(oldUsername);
        Mockito.verify(userRepository,Mockito.times(1)).save(Mockito.any(User.class));
        Assertions.assertDoesNotThrow(() -> userService.updateUser(newUser, oldUsername));
    }

    @Test
    void updateUserTest_throwDataNotFoundException() {
        UserDTO userDTO = UserDTO.builder()
                .username("NewTestUserUsername")
                .password("NewTestUserPassword")
                .email("newtestemail@gmail.com")
                .build();
        Mockito.when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class, () -> userService.updateUser(userDTO, "WrongUsername"));
    }

    @Test
    void updateUserTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.updateUser(null, null));
    }

    @Test
    void deleteUserTest() {
        User user = User.builder()
                .userId("1")
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        String username = "TestUserUsername";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        userService.deleteUser(username);
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(user.getUserId());
        Assertions.assertDoesNotThrow(() -> userService.deleteUser(username));
    }

    @Test
    void deleteUserTest_throwDataNotFoundException() {
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(DataNotFoundException.class, () -> userService.deleteUser("WrongUsername"));
    }

    @Test
    void deleteUserTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(null));
    }
}
