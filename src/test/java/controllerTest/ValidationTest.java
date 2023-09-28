package controllerTest;

import org.example.controller.Validation;
import org.example.service.UserService;
import org.example.service.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValidationTest {
    private Validation validation;
    private UserService userService = new UserServiceImpl();

    @BeforeEach
    void init() {
        validation = new Validation();
    }

    @AfterEach
    void tearDown() {
        validation = null;
    }

    @Test
    void loginValidationTest_success() {
        boolean result = validation.loginValidation("Test", "Test12TestAccount", userService);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertTrue(result);
    }

    @Test
    void loginValidationTest_failed() {
        boolean result = validation.loginValidation("WrongTest", "WTest123", userService);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertFalse(result);
    }

    @Test
    void loginValidationTest_failed_nullParameter() {
        Assertions.assertThrows(NullPointerException.class, () -> validation.loginValidation(null, null, null));
    }

    @Test
    void signUpValidationTest_failed_wrongUsernameFormat() {
        boolean result = validation.signUpValidation("Ts", "Test12TestAccount", userService);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertTrue(result);
    }

    @Test
    void signUpValidationTest_failed_wrongPasswordFormat() {
        boolean result = validation.signUpValidation("TestT", "Th1", userService);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertTrue(result);
    }

    @Test
    void signUpValidationTest_failed_usernameAvailable() {
        boolean result = validation.signUpValidation("Test", "Test12Test", userService);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertTrue(result);
    }
}
