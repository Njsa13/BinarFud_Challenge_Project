package org.example.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Order {
    private int orderId;
    private String address;
    private int totalPrice;
}
