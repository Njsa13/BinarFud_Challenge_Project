package com.binarfud.binarfud_challenge7.servicetest;

import com.binarfud.binarfud_challenge7.dto.PaginationDTO;
import com.binarfud.binarfud_challenge7.dto.ProductDTO;
import com.binarfud.binarfud_challenge7.entity.Merchant;
import com.binarfud.binarfud_challenge7.entity.Product;
import com.binarfud.binarfud_challenge7.enums.MerchantStatus;
import com.binarfud.binarfud_challenge7.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge7.repository.MerchantRepository;
import com.binarfud.binarfud_challenge7.repository.ProductRepository;
import com.binarfud.binarfud_challenge7.service.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest
public class ProductServiceTest {
    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MerchantRepository merchantRepository;

    @Test
    void checkProductAvailabilityTest() {
        Mockito.when(productRepository.existsByProductNameAndMerchantName("TestProductName", "TestMerchantName"))
                .thenReturn(true);
        ProductDTO productDTO = ProductDTO.builder()
                .productName("TestProductName")
                .price(10000)
                .merchantName("TestMerchantName")
                .build();
        Boolean result = productService.checkProductAvailability(productDTO);
        Mockito.verify(productRepository, Mockito.times(1))
                .existsByProductNameAndMerchantName("TestProductName", "TestMerchantName");
        Assertions.assertTrue(result);
    }

    @Test
    void checkProductAvailabilityTest_notFound() {
        Mockito.when(productRepository.existsByProductNameAndMerchantName("TestProductName", "TestMerchantName"))
                .thenReturn(false);
        ProductDTO productDTO = ProductDTO.builder()
                .productName("TestProductName")
                .price(10000)
                .merchantName("TestMerchantName")
                .build();
        Boolean result = productService.checkProductAvailability(productDTO);
        Mockito.verify(productRepository, Mockito.times(1))
                .existsByProductNameAndMerchantName("TestProductName", "TestMerchantName");
        Assertions.assertFalse(result);
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
                        .merchant(merchant1)
                        .build(),
                Product.builder()
                        .productName("TestProductName2")
                        .price(20000)
                        .merchant(merchant2)
                        .build()
        );
        Page<Product> productPage = new PageImpl<>(products, PageRequest.of(0, 5), 1);
        Mockito.when(productRepository.findAll(PageRequest.of(0, 5))).thenReturn(productPage);
        PaginationDTO<ProductDTO> productDTOPaginationDTO = productService.getAllProductWithPagination(1);
        Mockito.verify(productRepository, Mockito.times(2)).findAll(PageRequest.of(0, 5));
        Assertions.assertEquals(2, productDTOPaginationDTO.getData().size());
        Assertions.assertEquals(1, productDTOPaginationDTO.getCurrentPage());
        Assertions.assertEquals(1, productDTOPaginationDTO.getTotalPages());
    }

    @Test
    void getAllProductWithPaginationTest_throwDataNotFoundException() {
        Mockito.when(productRepository.findAll(PageRequest.of(0, 5))).thenReturn(Page.empty());
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
        List<Product> products = Collections.singletonList(
                Product.builder()
                        .productName("TestProductName1")
                        .price(10000)
                        .merchant(merchant1)
                        .build()
        );
        Page<Product> productPage = new PageImpl<>(products, PageRequest.of(0, 5), 1);
        Mockito.when(productRepository.findByMerchantStatusWithPagination(PageRequest.of(0, 5), MerchantStatus.OPEN))
                .thenReturn(productPage);
        PaginationDTO<ProductDTO> productDTOPaginationDTO = productService.getAllProductByMerchantStatusWithPagination(1);
        Mockito.verify(productRepository, Mockito.times(2))
                .findByMerchantStatusWithPagination(PageRequest.of(0, 5), MerchantStatus.OPEN);
        Assertions.assertEquals(1, productDTOPaginationDTO.getData().size());
        Assertions.assertEquals(1, productDTOPaginationDTO.getCurrentPage());
        Assertions.assertEquals(1, productDTOPaginationDTO.getTotalPages());
    }

    @Test
    void getAllProductByMerchantStatusWithPaginationTest_throwDataNotFoundException() {
        Mockito.when(productRepository.findByMerchantStatusWithPagination(PageRequest.of(0, 5), MerchantStatus.OPEN)).thenReturn(Page.empty());
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
        ProductDTO productDTO = ProductDTO.builder()
                .productName("TestProductName")
                .price(10000)
                .merchantName("TestMerchantName")
                .imageFile(null)
                .build();
        Mockito.when(merchantRepository.findByMerchantName(merchant.getMerchantName())).thenReturn(merchant);
        productService.addProduct(productDTO);
        Mockito.verify(merchantRepository, Mockito.times(1)).findByMerchantName(merchant.getMerchantName());
        Assertions.assertDoesNotThrow(() -> productService.addProduct(productDTO));
    }

    @Test
    void addProductTest_throwDataNotFoundException() {
        ProductDTO productDTO = ProductDTO.builder()
                .productName("TestProductName")
                .price(10000)
                .merchantName("WrongMerchantName")
                .build();
        Mockito.when(merchantRepository.findByMerchantName(productDTO.getMerchantName())).thenReturn(null);
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
                .merchant(merchant)
                .build();
        String oldProductName = "TestProductName";
        ProductDTO productDTO = ProductDTO.builder()
                .productName("NewTestProductName")
                .price(20000)
                .merchantName("TestMerchantName")
                .build();
        Mockito.when(productRepository.findByProductNameAndMerchantName(oldProductName, merchant.getMerchantName()))
                .thenReturn(product);
        productService.updateProduct(productDTO, oldProductName);
        Mockito.verify(productRepository, Mockito.times(1))
                .findByProductNameAndMerchantName(oldProductName, merchant.getMerchantName());
        Assertions.assertDoesNotThrow(() -> productService.updateProduct(productDTO, oldProductName));
    }

    @Test
    void updateProductTest_throwDataNotFoundException() {
        ProductDTO productDTO = ProductDTO.builder()
                .productName("NewTestProductName")
                .price(20000)
                .merchantName("TestMerchantName")
                .build();
        Mockito.when(productRepository.findByProductNameAndMerchantName(productDTO.getProductName(), productDTO.getMerchantName()))
                        .thenReturn(null);
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
                .productId("1")
                .productName("TestProductName")
                .price(10000)
                .merchant(merchant)
                .build();
        String productName = "TestProductName";
        Mockito.when(productRepository.findByProductNameAndMerchantName(productName, merchant.getMerchantName()))
                .thenReturn(product);
        productService.deleteProduct(productName, merchant.getMerchantName());
        Mockito.verify(productRepository, Mockito.times(1))
                .findByProductNameAndMerchantName(productName, merchant.getMerchantName());
        Mockito.verify(productRepository, Mockito.times(1))
                .deleteById(product.getProductId());
        Assertions.assertDoesNotThrow(() -> productService.deleteProduct(productName, merchant.getMerchantName()));
    }

    @Test
    void deleteProductTest_throwDataNotFoundException() {
        Mockito.when(productRepository.findByProductNameAndMerchantName("TestProductName", "TestMerchantName"))
                        .thenReturn(null);
        Assertions.assertThrows(DataNotFoundException.class, () -> productService.deleteProduct("WrongTestProductName", "WrongTestMerchantName"));
    }

    @Test
    void deleteProductTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> productService.deleteProduct(null, null));
    }
}
