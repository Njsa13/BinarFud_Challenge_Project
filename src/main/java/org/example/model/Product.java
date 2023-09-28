package org.example.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Product {
    private int productId;
    private String productName;
    private int price;
    private String category;
}
