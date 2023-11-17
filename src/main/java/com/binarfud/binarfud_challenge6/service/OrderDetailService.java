package com.binarfud.binarfud_challenge6.service;

import com.binarfud.binarfud_challenge6.dto.OrderDetailDTO;
import com.binarfud.binarfud_challenge6.dto.PaginationDTO;

public interface OrderDetailService {
    Boolean checkOrderDetailAvailability(OrderDetailDTO orderDetailDTO, String username);
    PaginationDTO<OrderDetailDTO> getAllOrderDetailWithPagination(Integer page, String username);
    void addOrderDetail(OrderDetailDTO orderDetailDTO, String username);
    void updateOrderDetail(OrderDetailDTO orderDetailDTO, String username);
    void deleteOrderDetail(String productName, String merchantName, String username);
}
