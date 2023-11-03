package com.binarfud.binarfud_challenge6.service;

import com.binarfud.binarfud_challenge6.dto.OrderDTO;
import com.binarfud.binarfud_challenge6.entity.OrderDetail;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;

public interface OrderService {
    void updateOrder(OrderDTO orderDTO, String username);
    Integer calculateTotalPrice(List<OrderDetail> orderDetails);
    byte[] printInvoice(String username) throws JRException, FileNotFoundException;
}
