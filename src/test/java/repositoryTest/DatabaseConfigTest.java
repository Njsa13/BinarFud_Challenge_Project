package repositoryTest;

import org.example.repository.DatabaseConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class DatabaseConfigTest {
    private DatabaseConfig databaseConfig;

    @BeforeEach
    void init() {
        databaseConfig = new DatabaseConfig();
    }

    @AfterEach
    void tearDown() {
        databaseConfig = null;
    }

    @Test
    void getDataTest_success() {
        String query = "SELECT id_user FROM user WHERE username = 'Test';";
        List<String> listTest = Collections.singletonList("1");

        List<String> result = databaseConfig.getData(query, "id_user");
        Assertions.assertEquals(listTest, result);
    }

    @Test
    void getDataTest_failed_noData() {
        String query = "SELECT username FROM user WHERE id_user = 0;";
        List<String> listTest = Collections.emptyList();

        List<String> result = databaseConfig.getData(query, "username");
        Assertions.assertEquals(listTest, result);
    }

    @Test
    void getDataTest_failed_nullParameter1() {
        List<String> listTest = Collections.emptyList();

        List<String> result = databaseConfig.getData(null, "id_user");
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertEquals(listTest, result);
    }

    @Test
    void getDataTest_failed_nullParameter2() {
        String query = "SELECT id_user FROM user WHERE username = 'Test';";
        List<String> listTest = Collections.emptyList();

        List<String> result = databaseConfig.getData(query, null);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertEquals(listTest, result);
    }

    @Test
    void setDataTest_success() {
        String query = "UPDATE user SET id_user = ? WHERE username = 'Test';";
        Object[] parameterTest = {1};

        boolean result = databaseConfig.setData(query, parameterTest);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertTrue(result);
    }

    @Test
    void setDataTest_failed_nullParameter1() {
        Object[] parameterTest = {1};

        boolean result = databaseConfig.setData(null, parameterTest);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertFalse(result);
    }

    @Test
    void setDataTest_failed_nullParameter2() {
        String query = "UPDATE user SET id_user = ? WHERE username = 'Test';";

        boolean result = databaseConfig.setData(query, null);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertFalse(result);
    }
}
