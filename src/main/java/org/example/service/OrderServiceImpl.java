package org.example.service;

import org.example.model.Order;
import org.example.model.OrderDetail;
import org.example.model.Product;
import org.example.repository.OrderRepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    Order order = new Order();

    @Override
    public void setAddress(String address) {
        this.order.setAddress(address);
    }

    @Override
    public void setTotalPrice(List<OrderDetail> orderDetails) {
        this.order.setTotalPrice(calculateTotal(orderDetails));
    }

    @Override
    public void setTotalPrice(int totalPrice) {
        this.order.setTotalPrice(totalPrice);
    }

    /**
     * Method untuk mengambil data dari repository
     */
    @Override
    public void setOrderIdFromDatabase() {
        OrderRepository orderRepository = new OrderRepository();
        this.order.setOrderId(Integer.parseInt(orderRepository.getMaxOrderId().get(0)));
    }

    @Override
    public int getOrderId() {
        return this.order.getOrderId();
    }

    @Override
    public int getTotalPrice() {
        return this.order.getTotalPrice();
    }

    /**
     * Method untuk menghitung total harga
     * @param orderDetails
     * @return
     */
    @Override
    public int calculateTotal(List<OrderDetail> orderDetails) {
        int total = 0;
        for (int i = 0; i < orderDetails.size(); i++) {
            total += orderDetails.get(i).getPriceSubTotal();
        }
        return total;
    }

    /**
     * Method untuk insert data
     * @return
     */
    @Override
    public boolean insertOrder() {
        OrderRepository orderRepository = new OrderRepository();
        return orderRepository.addOrder(this.order.getAddress(), this.order.getTotalPrice());
    }

    /**
     * Method untuk membuat struk belanja
     * @param filePath
     * @param userId
     */
    @Override
    public void printReceipt(String filePath, int userId) {
        ProductService productService = new ProductServiceImpl();
        OrderDetailService orderDetailService = new OrderDetailServiceImpl();

        productService.clearListProduct();
        productService.setProductFromDatabase();
        orderDetailService.setUserId(userId);
        orderDetailService.clearListOrderDetail();
        orderDetailService.setOrderDetailFromDatabase();
        List<Product> products = productService.getProduct();
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetail();
        int totalPrice = getTotalPrice();

        File file = new File(filePath);
        try (
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                ) {
            if (file.createNewFile()) {
                throw new IOException();
            }
            bufferedWriter.write("+==========================+\n");
            bufferedWriter.write("|  Struk Belanja BinarFud  |\n");
            bufferedWriter.write("+==========================+\n");
            for (int i = 0; i < orderDetails.size(); i++) {
                int productId = orderDetails.get(i).getProductId();
                String productName = products.get(productId).getProductName();
                int price = products.get(productId).getPrice();
                int quantity = orderDetails.get(i).getQuantity();
                int subTotalPrice = orderDetails.get(i).getPriceSubTotal();
                bufferedWriter.write(String.format("%d. %s ---- Rp %,d x %d =====> %d \n", i+1, productName, price, quantity, subTotalPrice).replace(",","."));
            }
            bufferedWriter.write("------------------------------------------------------\n");
            bufferedWriter.write(String.format("Total harga : Rp %,d\n", totalPrice).replace(",", "."));
        } catch (IOException e) {
            System.out.println("--- Gagal Mencetak Struk Belanja ---\n");
        }
    }
}
