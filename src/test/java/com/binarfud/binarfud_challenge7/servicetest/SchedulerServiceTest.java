package com.binarfud.binarfud_challenge7.servicetest;

import com.binarfud.binarfud_challenge7.entity.Merchant;
import com.binarfud.binarfud_challenge7.entity.Order;
import com.binarfud.binarfud_challenge7.entity.Product;
import com.binarfud.binarfud_challenge7.entity.User;
import com.binarfud.binarfud_challenge7.enums.MerchantStatus;
import com.binarfud.binarfud_challenge7.enums.OrderStatus;
import com.binarfud.binarfud_challenge7.repository.OrderRepository;
import com.binarfud.binarfud_challenge7.service.SchedulerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@AutoConfigureMockMvc
@SpringBootTest
public class SchedulerServiceTest {
    @InjectMocks
    private SchedulerService schedulerService;
    @Mock
    private OrderRepository orderRepository;

    @Test
    void deleteIncompleteOrdersOlderThanOneMonthTest() {
        User user = User.builder()
                .username("TestUserUsername")
                .password("TestUserPassword")
                .email("testemail@gmail.com")
                .build();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date cutoffTime = calendar.getTime();
        Order order = Order.builder()
                .orderStatus(OrderStatus.INCOMPLETE)
                .user(user)
                .orderTime(cutoffTime)
                .build();
        List<Order> orders = Collections.singletonList(order);
        Mockito.when(orderRepository
                        .findByOrderStatusOlderThan(Mockito.any(Date.class), Mockito.eq(OrderStatus.INCOMPLETE)))
                .thenReturn(orders);
        schedulerService.deleteIncompleteOrdersOlderThanOneMonth();
        Mockito.verify(orderRepository, Mockito.times(1))
                .findByOrderStatusOlderThan(Mockito.any(Date.class), Mockito.eq(OrderStatus.INCOMPLETE));
        Mockito.verify(orderRepository, Mockito.times(1)).deleteAll(orders);
    }
}
