package com.binarfud.binarfud_challenge5.service;

import com.binarfud.binarfud_challenge5.dto.OrderDTO;
import com.binarfud.binarfud_challenge5.dto.OrderDetailDTO;
import com.binarfud.binarfud_challenge5.entity.Order;
import com.binarfud.binarfud_challenge5.entity.OrderDetail;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;
import java.util.List;

public interface OrderService {
    void updateOrder(OrderDTO orderDTO, String username);
    Integer calculateTotalPrice(List<OrderDetail> orderDetails);
    byte[] printInvoice(String username) throws JRException, FileNotFoundException;
}
