package com.binarfud.binarfud_challenge4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private String username;
    private String productName;
    private String merchantName;
    private Integer price;
    private Integer quantity;
    private Integer subtotalPrice;
}
