package com.binarfud.binarfud_challenge7.service;

import com.binarfud.binarfud_challenge7.entity.Order;
import com.binarfud.binarfud_challenge7.enums.OrderStatus;
import com.binarfud.binarfud_challenge7.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SchedulerService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Method untuk menghapus order yang belum tidak di checkout dalam kurun waktu satu bulan terakhir
     * Method ini akan dijalankan setiap hari pada pukul 01.00
     */
    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void deleteIncompleteOrdersOlderThanOneMonth() {
        log.info("Deleting incomplete orders more than one month ago");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date cutoffTime = calendar.getTime();
        List<Order> orders = orderRepository.findByOrderStatusOlderThan(cutoffTime, OrderStatus.INCOMPLETE);
        if (!orders.isEmpty()) {
            orderRepository.deleteAll(orders);
            log.info("Deletion of incomplete orders more than one month ago initiated");
        } else {
            log.info("No incomplete orders found older than one month");
        }
    }
}
