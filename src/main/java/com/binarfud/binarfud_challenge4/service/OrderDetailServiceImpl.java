package com.binarfud.binarfud_challenge4.service;

import com.binarfud.binarfud_challenge4.dto.OrderDetailDTO;
import com.binarfud.binarfud_challenge4.dto.OrderDetailPageDTO;
import com.binarfud.binarfud_challenge4.dto.OrderListDTO;
import com.binarfud.binarfud_challenge4.entity.Order;
import com.binarfud.binarfud_challenge4.entity.OrderDetail;
import com.binarfud.binarfud_challenge4.entity.Product;
import com.binarfud.binarfud_challenge4.entity.User;
import com.binarfud.binarfud_challenge4.repository.OrderDetailRepository;
import com.binarfud.binarfud_challenge4.repository.OrderRepository;
import com.binarfud.binarfud_challenge4.repository.ProductRepository;
import com.binarfud.binarfud_challenge4.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    /**
     * Method untuk mengecek order detail
     * @param orderDetailDTO
     * @return
     */
    @Override
    public Boolean checkOrderDetailAvailability(OrderDetailDTO orderDetailDTO) {
        User user = userRepository.findByUsername(orderDetailDTO.getUsername());
        Product product = productRepository.getProductsByMerchantNameAndProductName(
                orderDetailDTO.getMerchantName(), orderDetailDTO.getProductName()).get(0);
        Order order = null;
        if (!orderRepository.findByUsernameAndCompleted(user.getUsername(), false).isEmpty()) {
            order = orderRepository.findByUsernameAndCompleted(user.getUsername(), false).get(0);
        }
        OrderDetail orderDetail = null;
        if (!orderDetailRepository.findByOrderAndProduct(order, product).isEmpty()) {
            orderDetail = orderDetailRepository.findByOrderAndProduct(order, product).get(0);
        }
        return Optional.ofNullable(orderDetail)
                .map(val -> {
                    log.info("Order detail available with product name : {}", orderDetailDTO.getProductName());
                    return true;
                })
                .orElseGet(() -> {
                    log.info("Order detail not available with product name : {}", orderDetailDTO.getProductName());
                    return false;
                });
    }

    /**
     * Method untuk menambah order detail
     * @param orderDetailDTO
     * @return
     */
    @Override
    public Boolean addOrderDetail(OrderDetailDTO orderDetailDTO) {
        User user = userRepository.findByUsername(orderDetailDTO.getUsername());
        Product product = productRepository.getProductsByMerchantNameAndProductName(
                orderDetailDTO.getMerchantName(), orderDetailDTO.getProductName()).get(0);
        Order order;
        if (!orderRepository.findByUsernameAndCompleted(user.getUsername(), false).isEmpty()) {
            order = orderRepository.findByUsernameAndCompleted(user.getUsername(), false).get(0);
        } else {
            order = Order.builder()
                    .completed(false)
                    .user(user)
                    .build();
        }
        order = orderRepository.save(order);
        OrderDetail orderDetail = OrderDetail.builder()
                .quantity(orderDetailDTO.getQuantity())
                .subtotalPrice(calculateSubtotalPrice(product.getPrice(), orderDetailDTO.getQuantity()))
                .product(product)
                .order(order)
                .build();
        return Optional.ofNullable(orderDetail)
                .map(val -> orderDetailRepository.save(val))
                .map(val -> {
                    log.info("Saving order detail successful with product name : {}, quantity : {}, and subtotal price : {}",
                            orderDetailDTO.getProductName(),
                            val.getQuantity(),
                            val.getSubtotalPrice());
                    return true;
                }).orElseGet(() ->{
                    log.error("Saving order detail unsuccessful for product name : {}", orderDetailDTO.getProductName());
                    return false;
                });
    }

    /**
     * Method untuk update order detail
     * @param orderDetailDTO
     */
    @Override
    public void updateOrderDetail(OrderDetailDTO orderDetailDTO) {
        User user = userRepository.findByUsername(orderDetailDTO.getUsername());
        Order order = null;
        if (!orderRepository.findByUsernameAndCompleted(user.getUsername(), false).isEmpty()) {
            order = orderRepository.findByUsernameAndCompleted(user.getUsername(), false).get(0);
        }
        Product product = productRepository.getProductsByMerchantNameAndProductName(
                orderDetailDTO.getMerchantName(), orderDetailDTO.getProductName()).get(0);
        OrderDetail orderDetail = null;
        if (!orderDetailRepository.findByOrderAndProduct(order, product).isEmpty()) {
            orderDetail= orderDetailRepository.findByOrderAndProduct(order, product).get(0);
            orderDetail.setQuantity(orderDetailDTO.getQuantity());
            orderDetail.setSubtotalPrice(calculateSubtotalPrice(product.getPrice(), orderDetailDTO.getQuantity()));
        }

        if (Objects.nonNull(orderDetailRepository.save(orderDetail))){
            log.info("Saving order detail successful with product name : {}, quantity : {}, and subtotal price : {}",
                    orderDetailDTO.getProductName(),
                    orderDetail.getQuantity(),
                    orderDetail.getSubtotalPrice());
        } else {
            log.error("Saving order detail unsuccessful with product name : {}, quantity : {}, and subtotal price : {}",
                    orderDetailDTO.getProductName(),
                    orderDetail.getQuantity(),
                    orderDetail.getSubtotalPrice());
        }
    }

    @Override
    public Integer calculateSubtotalPrice(Integer price, Integer quantity) {
        return price * quantity;
    }

    /**
     * Method untuk mengambil order detail
     * @param username
     * @param page
     * @return
     */
    @Override
    public OrderDetailPageDTO getAllOrderDetail(String username, Integer page) {
        User user = userRepository.findByUsername(username);
        Order order;
        if (!orderRepository.findByUsernameAndCompleted(user.getUsername(), false).isEmpty()) {
            order = orderRepository.findByUsernameAndCompleted(user.getUsername(), false).get(0);
        } else {
            return OrderDetailPageDTO.builder()
                    .orderListDTOS(Collections.emptyList())
                    .totalPages(0)
                    .build();
        }
        Page<OrderListDTO> orderListDTOPage = Optional.ofNullable(page)
                .map(val -> orderDetailRepository
                        .getOrderDetailByOrderWithPaging(PageRequest.of(val, 5), order.getOrderId()))
                .orElse(orderDetailRepository
                        .getOrderDetailByOrderWithPaging(PageRequest.of(0, 5), order.getOrderId()));
        List<OrderListDTO> orderListDTOS = orderListDTOPage.getContent();
        log.info("Getting OrderDetail page successful with page : {} and total page : {}", page, orderListDTOPage.getTotalPages());
        return OrderDetailPageDTO.builder()
                .orderListDTOS(orderListDTOS)
                .totalPages(orderListDTOPage.getTotalPages())
                .build();
    }


}
