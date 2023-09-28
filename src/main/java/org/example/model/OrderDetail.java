package org.example.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDetail {
    private int productId;
    private int quantity;
    private int priceSubTotal;
    private int userId;
    private int orderId;
}
