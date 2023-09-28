package repositoryTest;

import org.example.repository.OrderDetailRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class OrderDetailRepositoryTest {
    private OrderDetailRepository orderDetailRepository;

    @BeforeEach
    void init() {
        orderDetailRepository = new OrderDetailRepository();
    }

    @AfterEach
    void tearDown() {
        orderDetailRepository = null;
    }

    @Test
    void getQuantityByUserIdAndOrderIdTest() {
        List<String> listTest = Collections.singletonList("1");

        List<String> result = orderDetailRepository.getQuantityByUserIdAndOrderId(1);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertEquals(listTest, result);
    }

    @Test
    void getSubTotalByUserIdAndOraderIdTest() {
        List<String> listTest = Collections.singletonList("12000");

        List<String> result = orderDetailRepository.getSubTotalByUserIdAndOraderId(1);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertEquals(listTest, result);
    }

    @Test
    void getProductIdByUserIdAndOrderIdTest() {
        List<String> listTest = Collections.singletonList("1");

        List<String> result = orderDetailRepository.getProductIdByUserIdAndOrderId(1);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertEquals(listTest, result);
    }

    @Test
    void addOrderDetail() {
        boolean result = orderDetailRepository.addOrderDetail(2, 2, 2, 12000);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertTrue(result);
    }

    @Test
    void updateOrderIdByUserIdAndOrderId() {
        boolean result = orderDetailRepository.updateOrderIdByUserIdAndOrderId(1, 2);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertTrue(result);
    }

    @Test
    void updateQuantityTest() {
        boolean result = orderDetailRepository.updateQuantity(2,2,2);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertTrue(result);
    }
}
