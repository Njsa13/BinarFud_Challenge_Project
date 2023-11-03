package com.binarfud.binarfud_challenge6.service;

import com.binarfud.binarfud_challenge6.dto.OrderDTO;
import com.binarfud.binarfud_challenge6.dto.OrderDetailDTO;
import com.binarfud.binarfud_challenge6.entity.Order;
import com.binarfud.binarfud_challenge6.entity.OrderDetail;
import com.binarfud.binarfud_challenge6.enums.OrderStatus;
import com.binarfud.binarfud_challenge6.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge6.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Method untuk update order
     * @param orderDTO
     * @param username
     */
    @Override
    public void updateOrder(OrderDTO orderDTO, String username) {
        Optional<OrderDTO> orderDTOOptional = Optional.ofNullable(orderDTO);
        if (orderDTOOptional.isPresent()) {
            Optional<String> usernameOptional = Optional.ofNullable(username);
            Optional<String> destinationAddressOptional = Optional.ofNullable(orderDTO.getDestinationAddress());
            log.debug("Updating order with username = {}", username);
            if (usernameOptional.isPresent() && destinationAddressOptional.isPresent()) {
                Optional<Order> orderOptional = Optional.ofNullable(orderRepository
                        .findByUsernameAndOrderStatus(username, OrderStatus.INCOMPLETE));
                if (orderOptional.isPresent()) {
                    Order order = orderOptional.get();
                    List<OrderDetail> orderDetails = Optional.ofNullable(order.getOrderDetails())
                            .orElse(Collections.emptyList());
                    order.setOrderTime(new Date());
                    order.setDestinationAddress(orderDTO.getDestinationAddress());
                    order.setTotalPrice(calculateTotalPrice(orderDetails));
                    order.setOrderStatus(OrderStatus.COMPLETE);
                    orderRepository.save(order);
                    log.info("Updating order successful with username = {}", username);
                } else {
                    log.error("Updating order unsuccessful with username = {}", username);
                    throw new DataNotFoundException("Order with username = "+username);
                }
            } else {
                throw new IllegalArgumentException("Username and destination address cannot be null");
            }
        } else {
            throw new IllegalArgumentException("Order cannot be null");
        }
    }

    @Override
    public Integer calculateTotalPrice(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .reduce(0, (result, val) -> result + val.getSubtotalPrice(), Integer::sum);
    }

    /**
     * Method untuk print invoice
     * @param username
     * @return
     * @throws JRException
     * @throws FileNotFoundException
     */
    @Override
    public byte[] printInvoice(String username) throws JRException, FileNotFoundException {
        Optional<Order> orderOptional = Optional.ofNullable(orderRepository.findNewestOrderByUsername(username).get(0));
        log.debug("Printing Invoice");
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            List<OrderDetailDTO> orderDetailDTOS = order.getOrderDetails().stream()
                    .map(val -> OrderDetailDTO.builder()
                            .productName(val.getProduct().getProductName())
                            .merchantName(val.getProduct().getMerchant().getMerchantName())
                            .price(val.getProduct().getPrice())
                            .quantity(val.getQuantity())
                            .subtotalPrice(val.getSubtotalPrice())
                            .build())
                    .collect(Collectors.toList());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("destinationAddress", order.getDestinationAddress());
            parameters.put("orderTime", dateFormat.format(order.getOrderTime()));
            parameters.put("totalPrice", order.getTotalPrice());

            File file = ResourceUtils.getFile("classpath:binarfud_invoice.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(orderDetailDTOS));
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } else {
            log.debug("Printing Invoice Failed");
            throw new DataNotFoundException("Order with username "+username);
        }
    }
}
