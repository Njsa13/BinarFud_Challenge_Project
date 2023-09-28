package repositoryTest;

import org.example.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class ProductRepositoryTest {
    private ProductRepository productRepository;

    @BeforeEach
    void init() {
        productRepository = new ProductRepository();
    }

    @AfterEach
    void tearDown() {
        productRepository = null;
    }

    @Test
    void getProductIdTest() {
        List<String> result = productRepository.getProductId();
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertNotNull(result);
    }

    @Test
    void getProductNameTest() {
        List<String> result = productRepository.getProductName();
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertNotNull(result);
    }

    @Test
    void getProductCategoryTest() {
        List<String> result = productRepository.getProductCategory();
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertNotNull(result);
    }

    @Test
    void getProductPriceTest() {
        List<String> result = productRepository.getProductPrice();
        Assertions.assertDoesNotThrow(() -> result);
        Assertions.assertNotNull(result);
    }
}
