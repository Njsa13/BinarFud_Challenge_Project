package repositoryTest;

import org.example.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class UserRepositoryTest {
    private UserRepository userRepository;
    private List<String> listTest = Collections.singletonList("1");

    @BeforeEach
    void init() {
        userRepository = new UserRepository();
    }

    @AfterEach
    void tearDown() {
        userRepository =null;
    }

    @Test
    void getIdByUsernameAndPasswordTest() {
        List<String> result = userRepository.getIdByUsernameAndPassword("Test", "Test12TestAccount");
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertEquals(listTest, result);
    }

    @Test
    void getIdByUsernameTest() {
        List<String> result = userRepository.getIdByUsername("Test");
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertEquals(listTest, result);
    }

    @Test
    void addUsernameAndPasswordTest() {
        boolean result = userRepository.addUsernameAndPassword("TestCreate1", "123das");
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertTrue(result);
    }
}
