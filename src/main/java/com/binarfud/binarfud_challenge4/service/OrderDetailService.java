package com.binarfud.binarfud_challenge4.service;

import com.binarfud.binarfud_challenge4.dto.OrderDetailDTO;
import com.binarfud.binarfud_challenge4.dto.OrderDetailPageDTO;

public interface OrderDetailService {
    Boolean checkOrderDetailAvailability(OrderDetailDTO orderDetailDTO);
    Boolean addOrderDetail(OrderDetailDTO orderDetailDTO);
    void updateOrderDetail(OrderDetailDTO orderDetailDTO);
    Integer calculateSubtotalPrice(Integer price, Integer quantity);
    OrderDetailPageDTO getAllOrderDetail(String username, Integer page);
}
