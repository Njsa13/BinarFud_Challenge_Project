package org.example.service;

import org.example.model.OrderDetail;
import org.example.repository.OrderDetailRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDetailServiceImpl implements OrderDetailService {
    private List<OrderDetail> listOrderDetail = new ArrayList<>();
    private OrderDetail orderDetail = new OrderDetail();

    /**
     * Method untuk mengambil data dari repository
     */
    @Override
    public void setOrderDetailFromDatabase() {
        OrderDetailRepository orderDetailRepository = new OrderDetailRepository();
        for (int i = 0; i < orderDetailRepository.getQuantityByUserIdAndOrderId(this.orderDetail.getUserId()).size(); i++) {
            OrderDetail orderDetail = OrderDetail.builder()
                    .productId(Integer.parseInt(orderDetailRepository.getProductIdByUserIdAndOrderId(this.orderDetail.getUserId()).get(i)))
                    .quantity(Integer.parseInt(orderDetailRepository.getQuantityByUserIdAndOrderId(this.orderDetail.getUserId()).get(i)))
                    .priceSubTotal(Integer.parseInt(orderDetailRepository.getSubTotalByUserIdAndOraderId(this.orderDetail.getUserId()).get(i)))
                    .build();
           this.listOrderDetail.add(orderDetail);
        }
    }

    /**
     * Method untuk set data kedalam field
     * @param productId
     * @param quantity
     * @param price
     * @param priceSubTotal
     */
    @Override
    public void setOrderDetail(int productId, int quantity, int price, int userId, int priceSubTotal) {
        this.orderDetail = OrderDetail.builder()
                .productId(productId)
                .quantity(quantity)
                .priceSubTotal(priceSubTotal)
                .userId(userId)
                .build();
    }

    @Override
    public void setUserId(int userId) {
        this.orderDetail.setUserId(userId);
    }

    @Override
    public void setOrderId(int orderId) {
        this.orderDetail.setOrderId(orderId);
    }

    @Override
    public void clearListOrderDetail() {
        listOrderDetail.clear();
    }

    /**
     * Method untuk mengambil data dari field
     * @return
     */
    @Override
    public List<OrderDetail> getOrderDetail() {
        return listOrderDetail.stream()
                .map(value -> OrderDetail.builder()
                        .productId(value.getProductId())
                        .quantity(value.getQuantity())
                        .priceSubTotal(value.getPriceSubTotal())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public int calculateSubtotal(int quantity, int price) {
        return quantity * price;
    }

    /**
     * Method untuk mengecek apakah produk sudah dipesan atau belum
     */
    @Override
    public boolean checkProductAvailability() {
        OrderDetailRepository orderDetailRepository = new OrderDetailRepository();
        List<Integer> productIdList = orderDetailRepository.getProductIdByUserIdAndOrderId(this.orderDetail.getUserId()).stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        if (productIdList.contains(this.orderDetail.getProductId())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method untuk insert data
     */
    @Override
    public void insertOrderDetail() {
        OrderDetailRepository orderDetailRepository = new OrderDetailRepository();
        int produkId = this.orderDetail.getProductId();
        int userId = this.orderDetail.getUserId();
        int quantity = this.orderDetail.getQuantity();
        int priceSubTotal = this.orderDetail.getPriceSubTotal();
        orderDetailRepository.addOrderDetail(produkId, userId, quantity, priceSubTotal);
    }

    /**
     * Method untuk update data orderId
     * @return
     */
    @Override
    public boolean updateOrderDetail() {
        OrderDetailRepository orderDetailRepository = new OrderDetailRepository();
        return orderDetailRepository.updateOrderIdByUserIdAndOrderId(this.orderDetail.getOrderId(), this.orderDetail.getUserId());
    }

    /**
     * Method untuk update data quantty
     */
    @Override
    public void updateQuantity() {
        OrderDetailRepository orderDetailRepository = new OrderDetailRepository();
        int userId = this.orderDetail.getUserId();
        int quantity = this.orderDetail.getQuantity();
        int productId = this.orderDetail.getProductId();
        orderDetailRepository.updateQuantity(userId, quantity, productId);
    }
}
