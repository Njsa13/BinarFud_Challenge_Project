package com.binarfud.binarfud_challenge4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String productName;
    private Integer price;
    private String merchantName;
    private Integer index;
    private Integer page;
}
