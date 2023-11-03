package com.binarfud.binarfud_challenge6.repository;

import com.binarfud.binarfud_challenge6.dto.OrderDetailDTO;
import com.binarfud.binarfud_challenge6.entity.OrderDetail;
import com.binarfud.binarfud_challenge6.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {
    @Query("select case when count(od) > 0 then true else false end from OrderDetail od " +
            "join od.order o join o.user u join od.product p join p.merchant m " +
            "where u.username = :username and p.productName = :productName and m.merchantName = :merchantName and o.orderStatus = :orderStatus")
    boolean existsByUsernameAndProductNameAndMerchantNameAndOrderStatus(String username, String productName, String merchantName, OrderStatus orderStatus);

    @Query("select od from OrderDetail od " +
            "join od.order o join o.user u join od.product p join p.merchant m " +
            "where u.username = :username and p.productName = :productName and m.merchantName = :merchantName and o.orderStatus = :orderStatus")
    OrderDetail findByUsernameAndProductNameAndMerchantNameAndOrderStatus(String username, String productName, String merchantName, OrderStatus orderStatus);

    @Query("select new com.binarfud.binarfud_challenge6.dto.OrderDetailDTO" +
            "(p.productName, m.merchantName, p.price, od.quantity, od.subtotalPrice) from OrderDetail od " +
            "join od.order o join o.user u join od.product p join p.merchant m " +
            "where u.username = :username and o.orderStatus = :orderStatus")
    Page<OrderDetailDTO> findByUsernameAndOrderStatusWithPagination(Pageable pageable, String username, OrderStatus orderStatus);
}
