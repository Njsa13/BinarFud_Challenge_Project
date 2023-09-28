package org.example.service;

import org.example.model.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    void setOrderDetailFromDatabase();
    void setOrderDetail(int productId, int quantity, int price);
    void setUserId(int userId);
    void setOrderId(int orderId);
    void clearListOrderDetail();
    List<OrderDetail> getOrderDetail();
    int calculateSubtotal(int quantity, int price);
    void checkProductAvailability();
    boolean insertOrderDetail();
    boolean updateOrderDetail();
    boolean updateQuantity();

}
