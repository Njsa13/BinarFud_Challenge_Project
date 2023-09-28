package serviceTest;

import com.sun.nio.sctp.Association;
import org.example.model.OrderDetail;
import org.example.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

class OrderServiceImplTest {
    private OrderService orderService;

    @BeforeEach
    void init() {
        orderService = new OrderServiceImpl();
    }

    @AfterEach
    void tearDown() {
        orderService = null;
    }

    @Test
    void setAddressTest() {
        Assertions.assertDoesNotThrow(() -> orderService.setAddress("Purwokerto"));
    }

    @Test
    void setTotalPriceTest() {
        Assertions.assertDoesNotThrow(() -> orderService.setTotalPrice(12000));
    }

    @Test
    void setOrderIdFromDatabaseTest() {
        Assertions.assertDoesNotThrow(() -> orderService.setOrderIdFromDatabase());
    }

    @Test
    void getOrderIdTest() {
        Assertions.assertDoesNotThrow(() -> orderService.getOrderId());
    }

    @Test
    void getTotalPrice() {
        orderService.setTotalPrice(12000);

        int result = orderService.getTotalPrice();
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertEquals(12000, result);
    }

    @Test
    void calculateTotalTest_success() {
        List<OrderDetail> listTest = Arrays.asList(
                OrderDetail.builder()
                        .priceSubTotal(12000)
                        .build(),
                OrderDetail.builder()
                        .priceSubTotal(6000)
                        .build(),
                OrderDetail.builder()
                        .priceSubTotal(2000)
                        .build()
        );
        int result = orderService.calculateTotal(listTest);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertEquals(20000, result);
    }

    @Test
    void calculateTotalTest_failed_nullParameter() {
        List<OrderDetail> listTest = null;
        Assertions.assertThrows(NullPointerException.class, () -> orderService.calculateTotal(listTest));
    }

    @Test
    void insertOrderTest() {
        orderService.setAddress("Solo");
        orderService.setTotalPrice(9999);

        boolean result = orderService.insertOrder();
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertTrue(result);
    }

    @Test
    void printReceiptTest() {
        ProductService productServiceTest = new ProductServiceImpl();
        OrderDetailService orderDetailServiceTest = new OrderDetailServiceImpl();
        String filePathTest = "D:\\StrukBelanja.txt";

        productServiceTest.setProductFromDatabase();
        orderDetailServiceTest.setUserId(1);
        orderDetailServiceTest.setOrderDetailFromDatabase();
        orderService.setTotalPrice(12000);

        Assertions.assertDoesNotThrow(() -> orderService.printReceipt(filePathTest, productServiceTest, orderDetailServiceTest));
    }
}
