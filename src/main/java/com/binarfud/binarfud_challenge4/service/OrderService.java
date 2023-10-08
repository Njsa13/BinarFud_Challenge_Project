package com.binarfud.binarfud_challenge4.service;

import com.binarfud.binarfud_challenge4.entity.OrderDetail;

import java.util.List;

public interface OrderService {
    void updateOrder(String username, String address);
    Integer calculateTotal(List<OrderDetail> orderDetails);
    void printReceipt(String username, String address, String path);
}
