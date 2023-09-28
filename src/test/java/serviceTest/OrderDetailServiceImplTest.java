package serviceTest;

import org.example.model.OrderDetail;
import org.example.service.OrderDetailService;
import org.example.service.OrderDetailServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class OrderDetailServiceImplTest {
    private OrderDetailService orderDetailService;

    @BeforeEach
    void init() {
        orderDetailService = new OrderDetailServiceImpl();
    }

    @AfterEach
    void tearDown() {
        orderDetailService = null;
    }

    @Test
    void setOrderDetailFromDatabaseTest() {
        Assertions.assertDoesNotThrow(() -> orderDetailService.setOrderDetailFromDatabase());
    }

    @Test
    void setOrderDetailTest() {
        Assertions.assertDoesNotThrow(() -> orderDetailService.setOrderDetail(1, 1, 12000, 1, 12000));
    }

    @Test
    void setUserIdTest() {
        Assertions.assertDoesNotThrow(() -> orderDetailService.setUserId(1));
    }

    @Test
    void setOrderIdTest() {
        Assertions.assertDoesNotThrow(() -> orderDetailService.setOrderId(1));
    }

    @Test
    void clearListOrderDetailTest() {
        Assertions.assertDoesNotThrow(() -> orderDetailService.clearListOrderDetail());
    }

    @Test
    void getOrderDetailTest() {
        orderDetailService.setOrderDetailFromDatabase();
        List<OrderDetail> result = orderDetailService.getOrderDetail();
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertNotNull(result);
    }

    @Test
    void calculateSubtotalTest() {
        int result = orderDetailService.calculateSubtotal(2, 6000);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertEquals(12000, result);
    }

    @Test
    void checkProductAvailabilityTest_success_dataAvailable() {
        orderDetailService.setOrderDetail(1, 1, 12000, 1, 12000);
        boolean result = orderDetailService.checkProductAvailability();
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertTrue(result);
    }

    @Test
    void checkProductAvailabilityTest_failed_dataNotAvailable() {
        orderDetailService.setOrderDetail(5, 1, 12000, 1, 12000);
        boolean result = orderDetailService.checkProductAvailability();
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertFalse(result);
    }

    @Test
    void updateQuantityTest() {
        orderDetailService.setOrderDetail(2, 2, 6000, 2, 12000);
        Assertions.assertDoesNotThrow(() -> orderDetailService.updateOrderDetail());
    }
}
