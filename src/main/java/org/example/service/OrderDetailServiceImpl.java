package org.example.service;

import org.example.model.OrderDetail;
import org.example.repository.OrderDetailRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDetailServiceImpl implements OrderDetailService {
    List<OrderDetail> listOrderDetail = new ArrayList<>();
    int userId;
    int orderId;

    /**
     * Method untuk mengambil data dari repository
     */
    @Override
    public void setOrderDetailFromDatabase() {
        OrderDetailRepository orderDetailRepository = new OrderDetailRepository();
        for (int i = 0; i < orderDetailRepository.getQuantityByUserIdAndOrderId(this.userId).size(); i++) {
            OrderDetail orderDetail = OrderDetail.builder()
                    .productId(Integer.parseInt(orderDetailRepository.getProductIdByUserIdAndOrderId(this.userId).get(i)))
                    .quantity(Integer.parseInt(orderDetailRepository.getQuantityByUserIdAndOrderId(this.userId).get(i)))
                    .priceSubTotal(Integer.parseInt(orderDetailRepository.getSubTotalByUserIdAndOraderId(this.userId).get(i)))
                    .build();
           this.listOrderDetail.add(orderDetail);
        }
    }

    /**
     * Method untuk set data kedalam field
     * @param productId
     * @param quantity
     * @param price
     */
    @Override
    public void setOrderDetail(int productId, int quantity, int price) {
        int priceSubTotal = calculateSubtotal(quantity, price);
        OrderDetail orderDetail = OrderDetail.builder()
                .productId(productId)
                .quantity(quantity)
                .priceSubTotal(priceSubTotal)
                .build();
        this.listOrderDetail.add(orderDetail);
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void setOrderId(int orderId) {
        this.orderId = orderId;
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
        List<OrderDetail> list = new ArrayList<>();
        for (OrderDetail detail : listOrderDetail) {
            OrderDetail orderDetail = OrderDetail.builder()
                    .productId(detail.getProductId())
                    .quantity(detail.getQuantity())
                    .priceSubTotal(detail.getPriceSubTotal())
                    .build();
            list.add(orderDetail);
        }
        return list;
    }

    @Override
    public int calculateSubtotal(int quantity, int price) {
        return quantity * price;
    }

    /**
     * Method untuk mengecek apakah produk sudah dipesan atau belum
     */
    @Override
    public void checkProductAvailability() {
        OrderDetailRepository orderDetailRepository = new OrderDetailRepository();
        List<Integer> productIdList = orderDetailRepository.getProductIdByUserIdAndOrderId(this.userId).stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        if (productIdList.contains(this.listOrderDetail.get(0).getProductId())) {
            updateQuantity();
        } else {
            insertOrderDetail();
        }
    }

    /**
     * Method untuk insert data
     * @return
     */
    @Override
    public boolean insertOrderDetail() {
        OrderDetailRepository orderDetailRepository = new OrderDetailRepository();
        int produkId = this.listOrderDetail.get(0).getProductId();
        int quantity = this.listOrderDetail.get(0).getQuantity();
        int priceSubTotal = this.listOrderDetail.get(0).getPriceSubTotal();
        return orderDetailRepository.addOrderDetail(produkId, this.userId, quantity, priceSubTotal);
    }

    /**
     * Method untuk update data orderId
     * @return
     */
    @Override
    public boolean updateOrderDetail() {
        OrderDetailRepository orderDetailRepository = new OrderDetailRepository();
        return orderDetailRepository.updateOrderIdByUserIdAndOrderId(this.orderId, this.userId);
    }

    /**
     * Method untuk update data quantty
     * @return
     */
    @Override
    public boolean updateQuantity() {
        OrderDetailRepository orderDetailRepository = new OrderDetailRepository();
        int quantity = this.listOrderDetail.get(0).getQuantity();
        int produkId = this.listOrderDetail.get(0).getProductId();
        return orderDetailRepository.updateQuantity(this.userId, quantity, produkId);
    }
}
