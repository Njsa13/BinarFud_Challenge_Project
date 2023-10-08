package com.binarfud.binarfud_challenge4.service;

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
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    /**
     * Method untuk update order
     * @param username
     * @param address
     */
    @Override
    public void updateOrder(String username, String address) {
        User user = userRepository.findByUsername(username);
        Order order;
        if (!orderRepository.findByUsernameAndCompleted(user.getUsername(), false).isEmpty()) {
            order = orderRepository.findByUsernameAndCompleted(user.getUsername(), false).get(0);
            Integer totalPrice = calculateTotal(orderDetailRepository.findByOrder(order));
            order.setOrderTime(new Date());
            order.setCompleted(true);
            order.setDestinationAddress(address);
            order.setTotalPrice(totalPrice);
            orderRepository.save(order);
            log.info("Saving order successful with order time : {}, completed : {}, " +
                    "destination address : {}, and total price : {}",
                    order.getOrderTime(), order.getCompleted(),
                    order.getDestinationAddress(), order.getTotalPrice());
        } else {
            log.error("Saving order unsuccessful");
        }
    }

    @Override
    public Integer calculateTotal(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .reduce(0, (result, val) -> result + val.getSubtotalPrice(), Integer::sum);
    }

    /**
     * Method untuk mencetak struk belanja
     * @param username
     * @param address
     * @param filePath
     */
    @Override
    public void printReceipt(String username, String address, String filePath) {
        User user = userRepository.findByUsername(username);
        if (!orderRepository.findByUsernameAndCompleted(user.getUsername(), false).isEmpty()) {
            Order order = orderRepository.findByUsernameAndCompleted(user.getUsername(), false).get(0);
            List<OrderListDTO> orderListDTOS = orderDetailRepository.getOrderDetailByOrder(order.getOrderId());
            Integer totalPrice = calculateTotal(orderDetailRepository.findByOrder(order));
            File file = new File(filePath);
            try (
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            ) {
                if (file.createNewFile()) {
                    throw new IOException();
                }
                bufferedWriter.write("+===============+\n");
                bufferedWriter.write("| Struk Belanja |\n");
                bufferedWriter.write("+===============+\n");
                for (int i = 0; i < orderListDTOS.size(); i++) {
                    bufferedWriter.write(String.format("%d. %s --- %s --- %d x %d ------> %d %n", i+1,
                            orderListDTOS.get(i).getProductName(),
                            orderListDTOS.get(i).getMerchantName(),
                            orderListDTOS.get(i).getPrice(),
                            orderListDTOS.get(i).getQuantity(),
                            orderListDTOS.get(i).getSubtotalPrice()));
                }
                bufferedWriter.write("------------------------------------------------------\n");
                bufferedWriter.write(String.format("Alamat : %s %n", address));
                bufferedWriter.write(String.format("Total harga : Rp %,d %n", totalPrice).replace(",", "."));

                log.info("Print receipt successful");
            } catch (IOException e) {
                System.out.println("--- Gagal Mencetak Struk Belanja ---\n");
            }
        } else {
            log.error("Print receipt unsuccessful");
        }
    }
}
