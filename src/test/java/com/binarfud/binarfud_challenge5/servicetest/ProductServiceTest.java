package com.binarfud.binarfud_challenge5.servicetest;

import com.binarfud.binarfud_challenge5.dto.PaginationDTO;
import com.binarfud.binarfud_challenge5.dto.ProductDTO;
import com.binarfud.binarfud_challenge5.entity.Merchant;
import com.binarfud.binarfud_challenge5.entity.Product;
import com.binarfud.binarfud_challenge5.enums.MerchantStatus;
import com.binarfud.binarfud_challenge5.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge5.repository.MerchantRepository;
import com.binarfud.binarfud_challenge5.repository.ProductRepository;
import com.binarfud.binarfud_challenge5.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MerchantRepository merchantRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        merchantRepository.deleteAll();
    }

    @Test
    void checkProductAvailabilityTest() {
        Merchant merchant = Merchant.builder()
                .merchantName("TestMerchantName")
                .merchantLocation("TestMerchantLocation")
                .merchantStatus(MerchantStatus.CLOSED)
                .build();
        Product product = Product.builder()
                .productName("TestProductName")
                .price(10000)
                .merchant(merchantRepository.save(merchant))
                .build();
        productRepository.save(product);
        ProductDTO productDTO = ProductDTO.builder()
                .productName("TestProductName")
                .price(10000)
                .merchantName("TestMerchantName")
                .build();
        Boolean result = productService.checkProductAvailability(productDTO);
        Assertions.assertTrue(result);
    }

    @Test
    void checkProductAvailabilityTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.checkProductAvailability(null));
    }

    @Test
    void getAllProductWithPaginationTest() {
        Merchant merchant1 = Merchant.builder()
                .merchantName("TestMerchantName1")
                .merchantLocation("TestMerchantLocation1")
                .merchantStatus(MerchantStatus.OPEN)
                .build();
        Merchant merchant2 = Merchant.builder()
                .merchantName("TestMerchantName2")
                .merchantLocation("TestMerchantLocation2")
                .merchantStatus(MerchantStatus.CLOSED)
                .build();
        List<Product> products = Arrays.asList(
                Product.builder()
                        .productName("TestProductName1")
                        .price(10000)
                        .merchant(merchantRepository.save(merchant1))
                        .build(),
                Product.builder()
                        .productName("TestProductName2")
                        .price(20000)
                        .merchant(merchantRepository.save(merchant2))
                        .build()
        );
        productRepository.saveAll(products);
        PaginationDTO<ProductDTO> productDTOPaginationDTO = productService.getAllProductWithPagination(1);
        Assertions.assertEquals(2, productDTOPaginationDTO.getData().size());
        Assertions.assertEquals(1, productDTOPaginationDTO.getCurrentPage());
        Assertions.assertEquals(1, productDTOPaginationDTO.getTotalPages());
    }

    @Test
    void getAllProductWithPaginationTest_throwDataNotFoundException() {
        Assertions.assertThrows(DataNotFoundException.class, () -> productService.getAllProductWithPagination(1));
    }

    @Test
    void getAllProductWithPaginationTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.getAllProductWithPagination(0));
    }

    @Test
    void getAllProductByMerchantStatusWithPaginationTest() {
        Merchant merchant1 = Merchant.builder()
                .merchantName("TestMerchantName1")
                .merchantLocation("TestMerchantLocation1")
                .merchantStatus(MerchantStatus.OPEN)
                .build();
        Merchant merchant2 = Merchant.builder()
                .merchantName("TestMerchantName2")
                .merchantLocation("TestMerchantLocation2")
                .merchantStatus(MerchantStatus.CLOSED)
                .build();
        List<Product> products = Arrays.asList(
                Product.builder()
                        .productName("TestProductName1")
                        .price(10000)
                        .merchant(merchantRepository.save(merchant1))
                        .build(),
                Product.builder()
                        .productName("TestProductName2")
                        .price(20000)
                        .merchant(merchantRepository.save(merchant2))
                        .build()
        );
        productRepository.saveAll(products);
        PaginationDTO<ProductDTO> productDTOPaginationDTO = productService.getAllProductByMerchantStatusWithPagination(1);
        Assertions.assertEquals(1, productDTOPaginationDTO.getData().size());
        Assertions.assertEquals(1, productDTOPaginationDTO.getCurrentPage());
        Assertions.assertEquals(1, productDTOPaginationDTO.getTotalPages());
    }

    @Test
    void getAllProductByMerchantStatusWithPaginationTest_throwDataNotFoundException() {
        Assertions.assertThrows(DataNotFoundException.class, () -> productService.getAllProductByMerchantStatusWithPagination(1));
    }

    @Test
    void getAllProductByMerchantStatusWithPaginationTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.getAllProductByMerchantStatusWithPagination(0));
    }

    @Test
    void addProductTest() {
        Merchant merchant = Merchant.builder()
                .merchantName("TestMerchantName")
                .merchantLocation("TestMerchantLocation")
                .merchantStatus(MerchantStatus.CLOSED)
                .build();
        merchantRepository.save(merchant);
        ProductDTO productDTO = ProductDTO.builder()
                .productName("TestProductName")
                .price(10000)
                .merchantName("TestMerchantName")
                .imageFile(null)
                .build();
        productService.addProduct(productDTO);
        Product product = productRepository.findByProductNameAndMerchantName("TestProductName", "TestMerchantName");
        Assertions.assertEquals("TestProductName", product.getProductName());
        Assertions.assertEquals(10000, product.getPrice());
        Assertions.assertEquals("TestMerchantName", product.getMerchant().getMerchantName());
    }

    @Test
    void addProductTest_throwDataNotFoundException() {
        ProductDTO productDTO = ProductDTO.builder()
                .productName("TestProductName")
                .price(10000)
                .merchantName("WrongMerchantName")
                .build();
        Assertions.assertThrows(DataNotFoundException.class, () -> productService.addProduct(productDTO));
    }

    @Test
    void addProductTest_throwIllegalArgumentException1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.addProduct(null));
    }

    @Test
    void addProductTest_throwIllegalArgumentException2() {
        ProductDTO productDTO = ProductDTO.builder()
                .productName("TestProductName")
                .price(null)
                .merchantName(null)
                .imageFile(null)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.addProduct(productDTO));
    }

    @Test
    void updateProductTest() {
        Merchant merchant = Merchant.builder()
                .merchantName("TestMerchantName")
                .merchantLocation("TestMerchantLocation")
                .merchantStatus(MerchantStatus.CLOSED)
                .build();
        Product product = Product.builder()
                .productName("TestProductName")
                .price(10000)
                .merchant(merchantRepository.save(merchant))
                .build();
        productRepository.save(product);
        ProductDTO productDTO = ProductDTO.builder()
                .productName("NewTestProductName")
                .price(20000)
                .merchantName("TestMerchantName")
                .build();
        productService.updateProduct(productDTO, "TestProductName");
        Product productCheck = productRepository.findByProductNameAndMerchantName("NewTestProductName", "TestMerchantName");
        Assertions.assertEquals("NewTestProductName", productCheck.getProductName());
        Assertions.assertEquals(20000, productCheck.getPrice());
        Assertions.assertEquals("TestMerchantName", productCheck.getMerchant().getMerchantName());
    }

    @Test
    void updateProductTest_throwDataNotFoundException() {
        ProductDTO productDTO = ProductDTO.builder()
                .productName("NewTestProductName")
                .price(20000)
                .merchantName("TestMerchantName")
                .build();
        Assertions.assertThrows(DataNotFoundException.class, () -> productService.updateProduct(productDTO, "WrongProductName"));
    }

    @Test
    void updateProductTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(null, null));
    }

    @Test
    void deleteProductTest() {
        Merchant merchant = Merchant.builder()
                .merchantName("TestMerchantName")
                .merchantLocation("TestMerchantLocation")
                .merchantStatus(MerchantStatus.CLOSED)
                .build();
        Product product = Product.builder()
                .productName("TestProductName")
                .price(10000)
                .merchant(merchantRepository.save(merchant))
                .build();
        productRepository.save(product);
        ProductDTO productDTO = ProductDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                        .build();
        productService.deleteProduct("TestProductName", "TestMerchantName");
        Assertions.assertNull(productRepository.findByProductNameAndMerchantName("TestProductName", "TestMerchantName"));
    }

    @Test
    void deleteProductTest_throwDataNotFoundException() {
        Assertions.assertThrows(DataNotFoundException.class, () -> productService.deleteProduct("TestProductName", "TestMerchantName"));
    }

    @Test
    void deleteProductTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.deleteProduct(null, null));
    }
}
