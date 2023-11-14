package com.binarfud.binarfud_challenge7.servicetest;

import com.binarfud.binarfud_challenge7.dto.OrderDTO;
import com.binarfud.binarfud_challenge7.entity.*;
import com.binarfud.binarfud_challenge7.enums.MerchantStatus;
import com.binarfud.binarfud_challenge7.enums.OrderStatus;
import com.binarfud.binarfud_challenge7.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge7.repository.*;

import com.binarfud.binarfud_challenge7.service.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest
public class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MerchantRepository merchantRepository;
    @Mock
    private  ProductRepository productRepository;

    @Test
    void updateOrderTest() {
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
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
        List<OrderDetail> orderDetails = Arrays.asList(
                OrderDetail.builder()
                        .quantity(2)
                        .subtotalPrice(20000)
                        .product(product)
                        .build(),
                OrderDetail.builder()
                        .quantity(1)
                        .subtotalPrice(10000)
                        .product(product)
                        .build());
        Order order = Order.builder()
                .orderStatus(OrderStatus.INCOMPLETE)
                .user(user)
                .orderDetails(orderDetails)
                .build();
        Mockito.when(orderRepository.findByUsernameAndOrderStatus(user.getUsername(), OrderStatus.INCOMPLETE))
                .thenReturn(order);
        Mockito.when(orderRepository.save(order)).thenAnswer(invocation -> invocation.getArguments()[0]);
        OrderDTO orderDTO = OrderDTO.builder()
                .destinationAddress("TestDestinationAddress")
                .build();
        orderService.updateOrder(orderDTO, "TestUserUsername");
        Mockito.verify(orderRepository, Mockito.times(1))
                .findByUsernameAndOrderStatus(user.getUsername(), OrderStatus.INCOMPLETE);
        Mockito.verify(orderRepository, Mockito.times(1)).save(order);
        Assertions.assertDoesNotThrow(() -> orderService.updateOrder(orderDTO, "TestUserUsername"));
    }

    @Test
    void updateOrderTest_throwDataNotFoundException() {
        OrderDTO orderDTO = OrderDTO.builder()
                .destinationAddress("TestDestinationAddress")
                .build();
        Mockito.when(orderRepository.findByUsernameAndOrderStatus("TestUserUsername", OrderStatus.INCOMPLETE))
                .thenReturn(null);
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
