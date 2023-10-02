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
    private Order order = new Order();

    @Override
    public void setAddress(String address) {
        this.order.setAddress(address);
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
    public int calculateTotal(List<OrderDetail> orderDetails) throws NullPointerException {
        return orderDetails.stream()
                .reduce(0, (result, val) -> result + val.getPriceSubTotal(), Integer::sum);
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
     * Method untuk membuat/mencetak struk belanja
     * @param filePath
     * @param productService
     * @param orderDetailService
     */
    @Override
    public void printReceipt(String filePath, ProductService productService, OrderDetailService orderDetailService) {
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
                String productName = productService.getProductNameById(productId);
                int price = productService.getPriceById(productId);
                int quantity = orderDetails.get(i).getQuantity();
                int subTotalPrice = orderDetails.get(i).getPriceSubTotal();
                bufferedWriter.write(String.format("%d. %s ---- Rp %,d x %d =====> %d %n", i+1, productName, price, quantity, subTotalPrice).replace(",","."));
            }
            bufferedWriter.write("------------------------------------------------------\n");
            bufferedWriter.write(String.format("Total harga : Rp %,d %n", totalPrice).replace(",", "."));
        } catch (IOException e) {
            System.out.println("--- Gagal Mencetak Struk Belanja ---\n");
        }
    }
}
