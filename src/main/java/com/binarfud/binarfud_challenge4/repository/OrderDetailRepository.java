package com.binarfud.binarfud_challenge4.repository;

import com.binarfud.binarfud_challenge4.dto.OrderListDTO;
import com.binarfud.binarfud_challenge4.entity.Order;
import com.binarfud.binarfud_challenge4.entity.OrderDetail;
import com.binarfud.binarfud_challenge4.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    List<OrderDetail> findByOrderAndProduct(Order order, Product product);

    List<OrderDetail> findByOrder(Order order);

    @Query("select new com.binarfud.binarfud_challenge4.dto.OrderListDTO    " +
            "(p.productName, m.merchantName, p.price, od.quantity, od.subtotalPrice) " +
            "from OrderDetail od right join od.order o left join od.product p left join p.merchant m " +
            "where o.orderId = :orderId")
    Page<OrderListDTO> getOrderDetailByOrderWithPaging(Pageable pageable, String orderId);

    @Query("select new com.binarfud.binarfud_challenge4.dto.OrderListDTO    " +
            "(p.productName, m.merchantName, p.price, od.quantity, od.subtotalPrice) " +
            "from OrderDetail od right join od.order o left join od.product p left join p.merchant m " +
            "where o.orderId = :orderId")
    List<OrderListDTO> getOrderDetailByOrder(String orderId);

}
