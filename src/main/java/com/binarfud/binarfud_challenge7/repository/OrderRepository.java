package com.binarfud.binarfud_challenge7.repository;

import com.binarfud.binarfud_challenge7.entity.Order;
import com.binarfud.binarfud_challenge7.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query("select o from Order o join o.user u where u.username = :username and o.orderStatus = :orderStatus")
    Order findByUsernameAndOrderStatus(String username, OrderStatus orderStatus);

    @Query("select o from Order o join o.user u where u.username = :username order by o.orderTime DESC")
    List<Order> findNewestOrderByUsername(String username);

    @Query("select o from Order o where o.orderStatus = :orderStatus and o.orderTime < :cutoffTime")
    List<Order> findByOrderStatusOlderThan(Date cutoffTime, OrderStatus orderStatus);
}
