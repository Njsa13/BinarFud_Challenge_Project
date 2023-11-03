package com.binarfud.binarfud_challenge6.servicetest;

import com.binarfud.binarfud_challenge6.dto.OrderDTO;
import com.binarfud.binarfud_challenge6.entity.*;
import com.binarfud.binarfud_challenge6.enums.MerchantStatus;
import com.binarfud.binarfud_challenge6.enums.OrderStatus;
import com.binarfud.binarfud_challenge6.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge6.repository.*;
import com.binarfud.binarfud_challenge6.service.OrderService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private  ProductRepository productRepository;
    @Autowired
    private OrderService orderService;

    @AfterEach
    void tearDown() {
        orderDetailRepository.deleteAll();
        userRepository.deleteAll();
        orderRepository.deleteAll();
        merchantRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void updateOrderTest() {
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Order order = Order.builder()
                .orderStatus(OrderStatus.INCOMPLETE)
                .user(userRepository.save(user))
                .build();
        order = orderRepository.save(order);
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
        product = productRepository.save(product);
        List<OrderDetail> orderDetails = Arrays.asList(
                OrderDetail.builder()
                        .quantity(2)
                        .subtotalPrice(20000)
                        .order(order)
                        .product(product)
                        .build(),
                OrderDetail.builder()
                        .quantity(1)
                        .subtotalPrice(10000)
                        .order(order)
                        .product(product)
                        .build());
        orderDetailRepository.saveAll(orderDetails);
        OrderDTO orderDTO = OrderDTO.builder()
                .destinationAddress("TestDestinationAddress")
                .build();
        orderService.updateOrder(orderDTO, "TestUserUsername");
        Order orderCheck = orderRepository
                .findByUsernameAndOrderStatus("TestUserUsername", OrderStatus.COMPLETE);
        Assertions.assertEquals(OrderStatus.COMPLETE, orderCheck.getOrderStatus());
        Assertions.assertEquals("TestUserUsername", orderCheck.getUser().getUsername());
        Assertions.assertEquals("TestDestinationAddress", orderCheck.getDestinationAddress());
        Assertions.assertEquals(30000, orderCheck.getTotalPrice());
    }

    @Test
    void updateOrderTest_throwDataNotFoundException() {
        OrderDTO orderDTO = OrderDTO.builder()
                .destinationAddress("TestDestinationAddress")
                .build();
        Assertions.assertThrows(DataNotFoundException.class, () -> orderService.updateOrder(orderDTO, "TestUserUsername"));
    }

    @Test
    void updateOrderTest_throwIllegalArgumentException1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderService.updateOrder(null, "TestUserUsername"));
    }

    @Test
    void updateOrderTest_throwIllegalArgumentException2() {
        OrderDTO orderDTO = OrderDTO.builder()
                .destinationAddress("TestDestinationAddress")
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> orderService.updateOrder(orderDTO, null));
    }
}
