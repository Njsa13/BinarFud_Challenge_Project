package com.binarfud.binarfud_challenge6.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String destinationAddress;
    private String orderTime;
    private Integer totalPrice;
}
