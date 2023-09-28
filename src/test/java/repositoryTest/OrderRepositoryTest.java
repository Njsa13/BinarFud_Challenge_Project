package repositoryTest;

import org.example.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class OrderRepositoryTest {
    private OrderRepository orderRepository;

    @BeforeEach
    void init() {
        orderRepository = new OrderRepository();
    }

    @AfterEach
    void tearDown() {
        orderRepository = null;
    }

    @Test
    void getMaxOrderIdTest() {
        List<String> result = orderRepository.getMaxOrderId();
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertNotNull(result);
    }

    @Test
    void addOrderTest() {
        boolean result = orderRepository.addOrder("Solo", 9999);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertTrue(result);
    }
}
