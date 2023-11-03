package com.binarfud.binarfud_challenge6.servicetest;

import com.binarfud.binarfud_challenge6.dto.OrderDetailDTO;
import com.binarfud.binarfud_challenge6.dto.PaginationDTO;
import com.binarfud.binarfud_challenge6.entity.*;
import com.binarfud.binarfud_challenge6.enums.MerchantStatus;
import com.binarfud.binarfud_challenge6.enums.OrderStatus;
import com.binarfud.binarfud_challenge6.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge6.repository.*;
import com.binarfud.binarfud_challenge6.service.OrderDetailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderDetailTest {

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private OrderDetailService orderDetailService;

    @AfterEach
    void tearDown() {
        orderDetailRepository.deleteAll();
        userRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        merchantRepository.deleteAll();
    }

    @Test
    void checkOrderDetailAvailabilityTest() {
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Order order = Order.builder()
                .orderStatus(OrderStatus.INCOMPLETE)
                .user(userRepository.save(user))
                .build();
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
        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(2)
                .subtotalPrice(20000)
                .product(productRepository.save(product))
                .order(orderRepository.save(order))
                .build();
        orderDetailRepository.save(orderDetail);
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .build();
        Boolean result = orderDetailService.checkOrderDetailAvailability(orderDetailDTO, "TestUserUsername");
        Assertions.assertTrue(result);
    }

    @Test
    void checkOrderDetailAvailabilityTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderDetailService.checkOrderDetailAvailability(null, null));
    }

    @Test
    void getAllOrderDetailWithPaginationTest() {
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Order order = Order.builder()
                .orderStatus(OrderStatus.INCOMPLETE)
                .user(userRepository.save(user))
                .build();
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
        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(2)
                .subtotalPrice(20000)
                .product(productRepository.save(product))
                .order(orderRepository.save(order))
                .build();
        orderDetailRepository.save(orderDetail);
        PaginationDTO<OrderDetailDTO> paginationDTO = orderDetailService
                .getAllOrderDetailWithPagination(1, "TestUserUsername");
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .price(10000)
                .quantity(2)
                .subtotalPrice(20000)
                .build();
        Assertions.assertEquals(orderDetailDTO, paginationDTO.getData().get(0));
        Assertions.assertEquals(1, paginationDTO.getCurrentPage());
        Assertions.assertEquals(1, paginationDTO.getTotalPages());
    }

    @Test
    void getAllOrderDetailWithPaginationTest_throwDataNotFoundException() {
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
                .merchant(merchantRepository.save(merchant))
                .build();
        productRepository.save(product);
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Order order = Order.builder()
                .orderStatus(OrderStatus.INCOMPLETE)
                .user(userRepository.save(user))
                .build();
        orderRepository.save(order);
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .quantity(2)
                .build();
        orderDetailService.addOrderDetail(orderDetailDTO, "TestUserUsername");
        OrderDetail orderDetail = orderDetailRepository
                .findByUsernameAndProductNameAndMerchantNameAndOrderStatus
                        ("TestUserUsername", "TestProductName", "TestMerchantName", OrderStatus.INCOMPLETE);
        Assertions.assertEquals("TestProductName", orderDetail.getProduct().getProductName());
        Assertions.assertEquals(2, orderDetail.getQuantity());
        Assertions.assertEquals(20000, orderDetail.getSubtotalPrice());
        Assertions.assertEquals(OrderStatus.INCOMPLETE, orderDetail.getOrder().getOrderStatus());
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
                .merchant(merchantRepository.save(merchant))
                .build();
        productRepository.save(product);
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        userRepository.save(user);
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .quantity(2)
                .build();
        orderDetailService.addOrderDetail(orderDetailDTO, "TestUserUsername");
        OrderDetail orderDetail = orderDetailRepository
                .findByUsernameAndProductNameAndMerchantNameAndOrderStatus
                        ("TestUserUsername", "TestProductName", "TestMerchantName", OrderStatus.INCOMPLETE);
        Assertions.assertEquals("TestProductName", orderDetail.getProduct().getProductName());
        Assertions.assertEquals(2, orderDetail.getQuantity());
        Assertions.assertEquals(20000, orderDetail.getSubtotalPrice());
        Assertions.assertEquals(OrderStatus.INCOMPLETE, orderDetail.getOrder().getOrderStatus());
    }

    @Test
    void addOrderDetailTest_throwDataNotFoundException() {
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .quantity(2)
                .build();
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
                .user(userRepository.save(user))
                .build();
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
        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(2)
                .subtotalPrice(20000)
                .product(productRepository.save(product))
                .order(orderRepository.save(order))
                .build();
        orderDetailRepository.save(orderDetail);
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .quantity(3)
                .build();
        orderDetailService.updateOrderDetail(orderDetailDTO, "TestUserUsername");
        OrderDetail orderDetailCheck = orderDetailRepository
                .findByUsernameAndProductNameAndMerchantNameAndOrderStatus
                        ("TestUserUsername", "TestProductName", "TestMerchantName", OrderStatus.INCOMPLETE);
        Assertions.assertEquals(3, orderDetailCheck.getQuantity());
        Assertions.assertEquals(30000, orderDetailCheck.getSubtotalPrice());
    }

    @Test
    void updateOrderDetailTest_throwDataNotFoundException() {
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .quantity(3)
                .build();
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
                .user(userRepository.save(user))
                .build();
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
        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(2)
                .subtotalPrice(20000)
                .product(productRepository.save(product))
                .order(orderRepository.save(order))
                .build();
        orderDetailRepository.save(orderDetail);
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("TestProductName")
                .merchantName("TestMerchantName")
                .build();
        orderDetailService.deleteOrderDetail("TestProductName", "TestMerchantName", "TestUserUsername");
        Assertions.assertNull(orderDetailRepository
                .findByUsernameAndProductNameAndMerchantNameAndOrderStatus
                        ("TestUserUsername", "TestProductName", "TestMerchantName", OrderStatus.INCOMPLETE));
    }

    @Test
    void deleteOrderDetailTest_throwDataNotFoundException() {
        Assertions.assertThrows(DataNotFoundException.class, () -> orderDetailService.deleteOrderDetail("TestProductName", "TestMerchantName", "TestUserUsername"));
    }

    @Test
    void deleteOrderDetailTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderDetailService.deleteOrderDetail(null, null, null));
    }

}
