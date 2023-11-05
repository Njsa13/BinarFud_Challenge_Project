package com.binarfud.binarfud_challenge6.servicetest;

import com.binarfud.binarfud_challenge6.dto.OrderDetailDTO;
import com.binarfud.binarfud_challenge6.dto.PaginationDTO;
import com.binarfud.binarfud_challenge6.entity.*;
import com.binarfud.binarfud_challenge6.enums.MerchantStatus;
import com.binarfud.binarfud_challenge6.enums.OrderStatus;
import com.binarfud.binarfud_challenge6.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge6.repository.*;
import com.binarfud.binarfud_challenge6.service.OrderDetailService;
import com.binarfud.binarfud_challenge6.service.OrderDetailServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AutoConfigureMockMvc
@SpringBootTest
public class OrderDetailTest {

    @InjectMocks
    private OrderDetailServiceImpl orderDetailService;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MerchantRepository merchantRepository;

    @Test
    void checkOrderDetailAvailabilityTest() {
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Order order = Order.builder()
                .orderStatus(OrderStatus.INCOMPLETE)
                .user(user)
                .build();
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
        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(2)
                .subtotalPrice(20000)
                .product(product)
                .order(order)
                .build();
        Mockito.when(orderDetailRepository
                .existsByUsernameAndProductNameAndMerchantNameAndOrderStatus(
                        user.getUsername(), product.getProductName(), merchant.getMerchantName(), OrderStatus.INCOMPLETE))
                .thenReturn(true);
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .build();
        Boolean result = orderDetailService.checkOrderDetailAvailability(orderDetailDTO, "TestUserUsername");
        Mockito.verify(orderDetailRepository, Mockito.times(1)).existsByUsernameAndProductNameAndMerchantNameAndOrderStatus(
                user.getUsername(), product.getProductName(), merchant.getMerchantName(), OrderStatus.INCOMPLETE);
        Assertions.assertTrue(result);
    }

    @Test
    void checkOrderDetailAvailabilityTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderDetailService.checkOrderDetailAvailability(null, null));
    }

    @Test
    void getAllOrderDetailWithPaginationTest() {
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .price(10000)
                .quantity(2)
                .subtotalPrice(20000)
                .build();
        List<OrderDetailDTO> orderDetails = Collections.singletonList(orderDetailDTO);
        Page<OrderDetailDTO> orderDetailPage = new PageImpl<>(orderDetails, PageRequest.of(0, 5), 1);
        Mockito.when(orderDetailRepository
                .findByUsernameAndOrderStatusWithPagination(PageRequest.of(0, 5), "TestUserUsername", OrderStatus.INCOMPLETE))
                .thenReturn(orderDetailPage);
        PaginationDTO<OrderDetailDTO> paginationDTO = orderDetailService
                .getAllOrderDetailWithPagination(1, "TestUserUsername");
        Mockito.verify(orderDetailRepository, Mockito.times(2))
                .findByUsernameAndOrderStatusWithPagination(PageRequest.of(0, 5), "TestUserUsername", OrderStatus.INCOMPLETE);
        Assertions.assertEquals(orderDetailDTO, paginationDTO.getData().get(0));
        Assertions.assertEquals(1, paginationDTO.getCurrentPage());
        Assertions.assertEquals(1, paginationDTO.getTotalPages());
    }

    @Test
    void getAllOrderDetailWithPaginationTest_throwDataNotFoundException() {
        Mockito.when(orderDetailRepository
                        .findByUsernameAndOrderStatusWithPagination(PageRequest.of(0, 5), "TestUserUsername", OrderStatus.INCOMPLETE))
                .thenReturn(Page.empty());
        Assertions.assertThrows(DataNotFoundException.class, () ->
                orderDetailService.getAllOrderDetailWithPagination(1, "TestUserUsername"));
    }

    @Test
    void getAllOrderDetailWithPaginationTest_throwIllegalArgumentException1() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                orderDetailService.getAllOrderDetailWithPagination(0, "TestUserUsername"));
    }

    @Test
    void getAllOrderDetailWithPaginationTest_throwIllegalArgumentException2() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                orderDetailService.getAllOrderDetailWithPagination(1, null));
    }

    @Test
    void addOrderDetailTest_availableOrder() {
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
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Order order = Order.builder()
                .orderStatus(OrderStatus.INCOMPLETE)
                .user(user)
                .build();
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(productRepository.findByProductNameAndMerchantName(product.getProductName(), merchant.getMerchantName()))
                .thenReturn(product);
        Mockito.when(orderRepository.findByUsernameAndOrderStatus(user.getUsername(), OrderStatus.INCOMPLETE))
                .thenReturn(order);
        Mockito.when(orderDetailRepository.save(Mockito.any(OrderDetail.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .quantity(2)
                .build();
        orderDetailService.addOrderDetail(orderDetailDTO, "TestUserUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(user.getUsername());
        Mockito.verify(productRepository, Mockito.times(1))
                .findByProductNameAndMerchantName(product.getProductName(), merchant.getMerchantName());
        Mockito.verify(orderRepository, Mockito.times(1))
                .findByUsernameAndOrderStatus(user.getUsername(), OrderStatus.INCOMPLETE);
        Mockito.verify(orderDetailRepository, Mockito.times(1)).save(Mockito.any(OrderDetail.class));
        Assertions.assertDoesNotThrow(() -> orderDetailService.addOrderDetail(orderDetailDTO, "TestUserUsername"));
    }

    @Test
    void addOrderDetailTest_noOrder() {
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
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(productRepository.findByProductNameAndMerchantName(product.getProductName(), merchant.getMerchantName()))
                .thenReturn(product);
        Mockito.when(orderRepository.findByUsernameAndOrderStatus(user.getUsername(), OrderStatus.INCOMPLETE))
                .thenReturn(null);
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        Mockito.when(orderDetailRepository.save(Mockito.any(OrderDetail.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .quantity(2)
                .build();
        orderDetailService.addOrderDetail(orderDetailDTO, "TestUserUsername");
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(user.getUsername());
        Mockito.verify(productRepository, Mockito.times(1))
                .findByProductNameAndMerchantName(product.getProductName(), merchant.getMerchantName());
        Mockito.verify(orderRepository, Mockito.times(1))
                .findByUsernameAndOrderStatus(user.getUsername(), OrderStatus.INCOMPLETE);
        Mockito.verify(orderRepository, Mockito.times(1)).save(Mockito.any(Order.class));
        Mockito.verify(orderDetailRepository, Mockito.times(1)).save(Mockito.any(OrderDetail.class));
        Assertions.assertDoesNotThrow(() -> orderDetailService.addOrderDetail(orderDetailDTO, "TestUserUsername"));
    }

    @Test
    void addOrderDetailTest_throwDataNotFoundException() {
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .quantity(2)
                .build();
        Mockito.when(userRepository.findByUsername("TestUserUsername")).thenReturn(Optional.empty());
        Mockito.when(productRepository.findByProductNameAndMerchantName("TestProductName", "TestMerchantName"))
                .thenReturn(null);
        Mockito.when(orderRepository.findByUsernameAndOrderStatus("TestUserUsername", OrderStatus.INCOMPLETE))
                .thenReturn(null);
        Assertions.assertThrows(DataNotFoundException.class, () -> orderDetailService.addOrderDetail(orderDetailDTO, "TestUserUsername"));
    }

    @Test
    void addOrderDetailTest_throwIllegalArgumentException1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderDetailService.addOrderDetail(null, "TestUserUsername"));
    }

    @Test
    void addOrderDetailTest_throwIllegalArgumentException2() {
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .quantity(2)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderDetailService.addOrderDetail(orderDetailDTO, null));
    }

    @Test
    void updateOrderDetailTest() {
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Order order = Order.builder()
                .orderStatus(OrderStatus.INCOMPLETE)
                .user(user)
                .build();
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
        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(2)
                .subtotalPrice(20000)
                .product(product)
                .order(order)
                .build();
        Mockito.when(orderDetailRepository
                .findByUsernameAndProductNameAndMerchantNameAndOrderStatus(
                        "TestUserUsername", product.getProductName(), merchant.getMerchantName(), OrderStatus.INCOMPLETE))
                .thenReturn(orderDetail);
        Mockito.when(orderDetailRepository.save(Mockito.any(OrderDetail.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .quantity(3)
                .build();
        orderDetailService.updateOrderDetail(orderDetailDTO, "TestUserUsername");
        Mockito.verify(orderDetailRepository, Mockito.times(1)).findByUsernameAndProductNameAndMerchantNameAndOrderStatus(
                "TestUserUsername", product.getProductName(), merchant.getMerchantName(), OrderStatus.INCOMPLETE);
        Mockito.verify(orderDetailRepository, Mockito.times(1)).save(Mockito.any(OrderDetail.class));
        Assertions.assertDoesNotThrow(() -> orderDetailService.updateOrderDetail(orderDetailDTO, "TestUserUsername"));
    }

    @Test
    void updateOrderDetailTest_throwDataNotFoundException() {
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .quantity(3)
                .build();
        Mockito.when(orderDetailRepository
                        .findByUsernameAndProductNameAndMerchantNameAndOrderStatus(
                                "TestUserUsername", orderDetailDTO.getProductName(), orderDetailDTO.getMerchantName(), OrderStatus.INCOMPLETE))
                .thenReturn(null);
        Assertions.assertThrows(DataNotFoundException.class, () -> orderDetailService.updateOrderDetail(orderDetailDTO, "TestUserUsername"));
    }

    @Test
    void updateOrderDetailTest_throwIllegalArgumentException1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderDetailService.updateOrderDetail(null, "TestUserUsername"));
    }

    @Test
    void updateOrderDetailTest_throwIllegalArgumentException2() {
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .quantity(2)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderDetailService.updateOrderDetail(orderDetailDTO, null));
    }

    @Test
    void deleteOrderDetailTest() {
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Order order = Order.builder()
                .orderStatus(OrderStatus.INCOMPLETE)
                .user(user)
                .build();
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
        OrderDetail orderDetail = OrderDetail.builder()
                .orderDetailId("1")
                .quantity(2)
                .subtotalPrice(20000)
                .product(product)
                .order(order)
                .build();
        String productName = "TestProductName";
        String merchantName = "TestMerchantName";
        String username = "TestUserUsername";
        Mockito.when(orderDetailRepository
                        .findByUsernameAndProductNameAndMerchantNameAndOrderStatus(
                                username, product.getProductName(), merchant.getMerchantName(), OrderStatus.INCOMPLETE))
                .thenReturn(orderDetail);
        orderDetailService.deleteOrderDetail(productName, merchantName, username);
        Mockito.verify(orderDetailRepository, Mockito.times(1)).findByUsernameAndProductNameAndMerchantNameAndOrderStatus(
                username, product.getProductName(), merchant.getMerchantName(), OrderStatus.INCOMPLETE);
        Mockito.verify(orderDetailRepository, Mockito.times(1)).deleteById(orderDetail.getOrderDetailId());
        Assertions.assertDoesNotThrow(() -> orderDetailService.deleteOrderDetail(productName, merchantName, username));
    }

    @Test
    void deleteOrderDetailTest_throwDataNotFoundException() {
        String productName = "TestProductName";
        String merchantName = "TestMerchantName";
        String username = "TestUserUsername";
        Mockito.when(orderDetailRepository
                        .findByUsernameAndProductNameAndMerchantNameAndOrderStatus(
                                username, productName, merchantName, OrderStatus.INCOMPLETE))
                .thenReturn(null);
        Assertions.assertThrows(DataNotFoundException.class, () -> orderDetailService.deleteOrderDetail("TestProductName", "TestMerchantName", "TestUserUsername"));
    }

    @Test
    void deleteOrderDetailTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderDetailService.deleteOrderDetail(null, null, null));
    }

}
