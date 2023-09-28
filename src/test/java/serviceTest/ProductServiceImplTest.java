package serviceTest;

import org.example.model.Product;
import org.example.service.ProductService;
import org.example.service.ProductServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class ProductServiceImplTest {
    private ProductService productService;

    @BeforeEach
    void init() {
        productService = new ProductServiceImpl();
    }

    @AfterEach
    void tearDown() {
        productService = null;
    }

    @Test
    void setProductFromDatabaseTest() {
        Assertions.assertDoesNotThrow(()-> productService.setProductFromDatabase());
        productService.clearListProduct();
    }

    @Test
    void clearListProductTest() {
        Assertions.assertDoesNotThrow(() -> productService.clearListProduct());
    }

    @Test
    void getProductNameByIdTest_success() {
        productService.setProductFromDatabase();
        String result = productService.getProductNameById(1);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertEquals("Nasi Goreng", result);
        productService.clearListProduct();
    }

    @Test
    void getProductNameByIdTest_failed_noData() {
        productService.setProductFromDatabase();
        String result = productService.getProductNameById(0);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertEquals("Nama Kosong", result);
        productService.clearListProduct();
    }

    @Test
    void getProductNameByIdTest_failed_nullParameter() {
        productService.setProductFromDatabase();
        Assertions.assertThrows(NullPointerException.class, () -> productService.getProductNameById(null));
        productService.clearListProduct();
    }

    @Test
    void getPriceByIdTest_success() {
        productService.setProductFromDatabase();
        Integer result = productService.getPriceById(1);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertEquals(12000, result);
        productService.clearListProduct();
    }

    @Test
    void getPriceByIdTest_failed_noData() {
        productService.setProductFromDatabase();
        Integer result = productService.getPriceById( 0);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertEquals(0, result);
        productService.clearListProduct();
    }

    @Test
    void getPriceByIdTest_failed_nullParameter() {
        productService.setProductFromDatabase();
        Assertions.assertThrows(NullPointerException.class, () -> productService.getPriceById(null));
        productService.clearListProduct();
    }

    @Test
    void getIdByIndexTest_succes() {
        productService.setProductFromDatabase();
        Integer result = productService.getIdByIndex(1);
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertEquals(1, result);
        productService.clearListProduct();
    }

    @Test
    void getIdByIndexTest_failed_noIndex() {
        productService.setProductFromDatabase();
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> productService.getIdByIndex(0));
        productService.clearListProduct();
    }

    @Test
    void getIdByIndexTest_failed_nullParameter() {
        productService.setProductFromDatabase();
        Assertions.assertThrows(NullPointerException.class, () -> productService.getIdByIndex(null));
        productService.clearListProduct();
    }

    @Test
    void getProductTest() {
        productService.setProductFromDatabase();
        List<Product> result = productService.getProduct();
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertNotNull(result);
        productService.clearListProduct();
    }
}
