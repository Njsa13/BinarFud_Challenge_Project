package serviceTest;

import org.example.service.UserService;
import org.example.service.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceImplTest {
    private UserService userService;

    @BeforeEach
    void init() {
        userService = new UserServiceImpl();
    }

    @AfterEach
    void tearDown() {
        userService = null;
    }

    @Test
    void setUserIdFromDatabaseTest() {
        Assertions.assertDoesNotThrow(() -> userService.setUserIdFromDatabase("Test", "Test12TestAccount"));
    }

    @Test
    void setUsernameTest() {
        Assertions.assertDoesNotThrow(() -> userService.setUsername("Exsample"));
    }

    @Test
    void setPasswordTest() {
        Assertions.assertDoesNotThrow(() -> userService.setPassword("b121b"));
    }

    @Test
    void checkLoginTest_success_dataAvailable() {
        userService.setUsername("Test");
        userService.setPassword("Test12TestAccount");
        boolean result = userService.checkLogin();
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertTrue(result);
    }

    @Test
    void getUserIdTest() {
        Assertions.assertDoesNotThrow(() -> userService.getUserId());
    }

    @Test
    void checkLoginTest_failed_dataNotAvailable() {
        userService.setUsername("Test31");
        userService.setPassword("12345");
        boolean result = userService.checkLogin();
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertFalse(result);
    }

    @Test
    void checkUsernameAvailabilityTest_success_dataNotAvailable() {
        userService.setUsername("Test31");
        userService.setPassword("12345");
        boolean result = userService.checkUsernameAvailability();
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertTrue(result);
    }

    @Test
    void checkUsernameAvailabilityTest_failed_dataAvailable() {
        userService.setUsername("Test");
        userService.setPassword("Test12TestAccount");
        boolean result = userService.checkUsernameAvailability();
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertFalse(result);
    }

    @Test
    void createUserAccountTest() {
        userService.setUsername("TestCreate1");
        userService.setPassword("123das");
        boolean result = userService.createUserAccount();
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertTrue(result);
    }
}
