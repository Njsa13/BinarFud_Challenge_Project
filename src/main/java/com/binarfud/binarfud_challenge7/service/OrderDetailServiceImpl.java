package com.binarfud.binarfud_challenge7.service;

import com.binarfud.binarfud_challenge7.dto.OrderDetailDTO;
import com.binarfud.binarfud_challenge7.dto.PaginationDTO;
import com.binarfud.binarfud_challenge7.entity.Order;
import com.binarfud.binarfud_challenge7.entity.OrderDetail;
import com.binarfud.binarfud_challenge7.entity.Product;
import com.binarfud.binarfud_challenge7.entity.User;
import com.binarfud.binarfud_challenge7.enums.OrderStatus;
import com.binarfud.binarfud_challenge7.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge7.repository.OrderDetailRepository;
import com.binarfud.binarfud_challenge7.repository.OrderRepository;
import com.binarfud.binarfud_challenge7.repository.ProductRepository;
import com.binarfud.binarfud_challenge7.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class OrderDetailServiceImpl implements OrderDetailService{

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    /**
     * Method untuk mengecek ketersediaan order detail
     * @param orderDetailDTO
     * @param username
     * @return
     */
    @Override
    public Boolean checkOrderDetailAvailability(OrderDetailDTO orderDetailDTO, String username) {
        Optional<OrderDetailDTO> orderDetailDTOOptional = Optional.ofNullable(orderDetailDTO);
        Optional<String> usernameOptional = Optional.ofNullable(username);
        if (orderDetailDTOOptional.isPresent() && usernameOptional.isPresent()) {
            log.debug("Checking order detail data availability with username = {}, product name = {}, and merchant name = {}",
                    username, orderDetailDTO.getProductName(), orderDetailDTO.getMerchantName());
            return orderDetailRepository.existsByUsernameAndProductNameAndMerchantNameAndOrderStatus(
                    username, orderDetailDTO.getProductName(), orderDetailDTO.getMerchantName(), OrderStatus.INCOMPLETE);
        } else {
            throw new IllegalArgumentException("Order Detail and username cannot be null");
        }
    }

    /**
     * Method untuk get order detail
     * @param page
     * @param username
     * @return
     */
    @Override
    public PaginationDTO<OrderDetailDTO> getAllOrderDetailWithPagination(Integer page, String username) {
        Optional<String> usernameOptional = Optional.ofNullable(username);
        if (usernameOptional.isPresent()) {
            Page<OrderDetailDTO> orderDetailDTOPage = Optional.ofNullable(page)
                    .map(val -> {
                        if (val < 1) {
                            throw new IllegalArgumentException("Page index must not be less than one");
                        }
                        return orderDetailRepository.findByUsernameAndOrderStatusWithPagination(
                                PageRequest.of(val-1, 5), username, OrderStatus.INCOMPLETE);
                    })
                    .orElse(orderDetailRepository.findByUsernameAndOrderStatusWithPagination(
                            PageRequest.of(0, 5), username, OrderStatus.INCOMPLETE));
            if (orderDetailDTOPage.isEmpty()) {
                throw new DataNotFoundException("Order Detail");
            }
            log.debug("Getting OrderDetail with pagination with current page = {} and total page = {}", page, orderDetailDTOPage.getTotalPages());
            return new PaginationDTO<>(orderDetailDTOPage.getContent(), page, orderDetailDTOPage.getTotalPages());
        } else {
            throw new IllegalArgumentException("Username cannot be null");
        }
    }

    /**
     * Method untuk menambahkan order detail
     * @param orderDetailDTO
     * @param username
     */
    @Override
    public void addOrderDetail(OrderDetailDTO orderDetailDTO, String username) {
        Optional<OrderDetailDTO> orderDetailDTOOptional = Optional.ofNullable(orderDetailDTO);
        if (orderDetailDTOOptional.isPresent()) {
            Optional<String> usernameOptional = Optional.ofNullable(username);
            Optional<String> productNameOptional = Optional.ofNullable(orderDetailDTO.getProductName());
            Optional<String> merchantNameOptional = Optional.ofNullable(orderDetailDTO.getMerchantName());
            Optional<Integer> quantityOptional = Optional.ofNullable(orderDetailDTO.getQuantity());
            log.debug("Saving order detail with username = {}, product name = {}, and merchant name = {}",
                    username, orderDetailDTO.getProductName(), orderDetailDTO.getMerchantName());
            if (usernameOptional.isPresent() && productNameOptional.isPresent() &&
                    merchantNameOptional.isPresent() && quantityOptional.isPresent()) {
                Optional<User> userOptional = userRepository.findByUsername(username);
                Optional<Product> productOptional = Optional.ofNullable(productRepository
                        .findByProductNameAndMerchantName(orderDetailDTO.getProductName(), orderDetailDTO.getMerchantName()));
                if (userOptional.isPresent() && productOptional.isPresent()) {
                    Optional<Order> orderOptional = Optional.ofNullable(orderRepository.findByUsernameAndOrderStatus(username, OrderStatus.INCOMPLETE));
                    User user = userOptional.get();
                    Product product = productOptional.get();
                    if (orderOptional.isPresent()) {
                        OrderDetail orderDetail = OrderDetail.builder()
                                .quantity(orderDetailDTO.getQuantity())
                                .subtotalPrice(product.getPrice()*orderDetailDTO.getQuantity())
                                .product(product)
                                .order(orderOptional.get())
                                .build();
                        orderDetailRepository.save(orderDetail);
                        log.info("Saving order detail with available order successful with username = {}, product name = {}, and merchant name = {}",
                                username, orderDetailDTO.getProductName(), orderDetailDTO.getMerchantName());
                    } else {
                        Order order = Order.builder()
                                .orderStatus(OrderStatus.INCOMPLETE)
                                .orderTime(new Date())
                                .user(user)
                                .build();
                        OrderDetail orderDetail = OrderDetail.builder()
                                .quantity(orderDetailDTO.getQuantity())
                                .subtotalPrice(product.getPrice()*orderDetailDTO.getQuantity())
                                .product(product)
                                .order(orderRepository.save(order))
                                .build();
                        orderDetailRepository.save(orderDetail);
                        log.info("Saving order detail with no order successful with username = {}, product name = {}, and merchant name = {}",
                                username, orderDetailDTO.getProductName(), orderDetailDTO.getMerchantName());
                    }
                } else {
                    log.error("Saving order detail unsuccessful with username = {}, product name = {}, and merchant name = {}",
                            username, orderDetailDTO.getProductName(), orderDetailDTO.getMerchantName());
                    throw new DataNotFoundException("User with username = "+username+
                            " or Product with product name = "+orderDetailDTO.getProductName()+" and merchant name = "+orderDetailDTO.getMerchantName());
                }
            } else {
                throw new IllegalArgumentException("Username, productName, merchantName, and quantity cannot be null");
            }
        } else {
            throw new IllegalArgumentException("Order Detail and User cannot be null");
        }
    }

    /**
     * Method untuk update order detail
     * @param orderDetailDTO
     * @param username
     */
    @Override
    public void updateOrderDetail(OrderDetailDTO orderDetailDTO, String username) {
        Optional<OrderDetailDTO> orderDetailDTOOptional = Optional.ofNullable(orderDetailDTO);
        if (orderDetailDTOOptional.isPresent()) {
            Optional<String> usernameOptional = Optional.ofNullable(username);
            Optional<String> productNameOptional = Optional.ofNullable(orderDetailDTO.getProductName());
            Optional<String> merchantNameOptional = Optional.ofNullable(orderDetailDTO.getMerchantName());
            Optional<Integer> quantityOptional = Optional.ofNullable(orderDetailDTO.getQuantity());
            log.debug("Updating order detail with username = {}, product name = {}, and merchant name = {}",
                    username, orderDetailDTO.getProductName(), orderDetailDTO.getMerchantName());
            if (usernameOptional.isPresent() && productNameOptional.isPresent() &&
                    merchantNameOptional.isPresent() && quantityOptional.isPresent()) {
                Optional<OrderDetail> orderDetailOptional = Optional.ofNullable(orderDetailRepository
                        .findByUsernameAndProductNameAndMerchantNameAndOrderStatus
                                (username, orderDetailDTO.getProductName(), orderDetailDTO.getMerchantName(), OrderStatus.INCOMPLETE));
                if (orderDetailOptional.isPresent()) {
                    OrderDetail orderDetail = orderDetailOptional.get();
                    orderDetail.setQuantity(orderDetailDTO.getQuantity());
                    orderDetail.setSubtotalPrice(orderDetail.getProduct().getPrice()*orderDetailDTO.getQuantity());
                    orderDetailRepository.save(orderDetail);
                    log.info("Updating order detail successful with username = {}, product name = {}, and merchant name = {}",
                            username, orderDetailDTO.getProductName(), orderDetailDTO.getMerchantName());
                } else {
                    log.error("Updating order detail unsuccessful with username = {}, product name = {}, and merchant name = {}",
                            username, orderDetailDTO.getProductName(), orderDetailDTO.getMerchantName());
                    throw new DataNotFoundException("OrderDetail with username = "+username+
                            ", product name = "+orderDetailDTO.getProductName()+", and merchant = "+orderDetailDTO.getMerchantName());
                }
            } else {
                throw new IllegalArgumentException("Username, productName, merchantName, and quantity cannot be null");
            }
        } else {
            throw new IllegalArgumentException("Order Detail and User cannot be null");
        }
    }

    /**
     * Method untuk hapus order detail
     * @param productName
     * @param merchantName
     * @param username
     */
    @Override
    public void deleteOrderDetail(String productName, String merchantName, String username) {
        Optional<String> usernameOptional = Optional.ofNullable(username);
        Optional<String> productNameOptional = Optional.ofNullable(productName);
        Optional<String> merchantNameOptional = Optional.ofNullable(merchantName);
        log.debug("Deleting order detail with username = {}, product name = {}, and merchant name = {}",
                username, productName, merchantName);
        if (usernameOptional.isPresent() && productNameOptional.isPresent() && merchantNameOptional.isPresent()) {
            Optional<OrderDetail> orderDetailOptional = Optional.ofNullable(orderDetailRepository
                    .findByUsernameAndProductNameAndMerchantNameAndOrderStatus
                            (username, productName, merchantName, OrderStatus.INCOMPLETE));
            if (orderDetailOptional.isPresent()) {
                OrderDetail orderDetail = orderDetailOptional.get();
                orderDetailRepository.deleteById(orderDetail.getOrderDetailId());
                log.info("Deleting order detail successful with username = {}, product name = {}, and merchant name = {}",
                        username, productName, merchantName);
            } else {
                log.error("Deleting order detail unsuccessful with username = {}, product name = {}, and merchant name = {}",
                        username, productName, merchantName);
                throw new DataNotFoundException("OrderDetail with username = "+username+
                        ", product name = "+productName+", and merchant = "+merchantName);
            }
        } else {
            throw new IllegalArgumentException("Username, productName, merchantName, price, and quantity cannot be null");
        }
    }
}
