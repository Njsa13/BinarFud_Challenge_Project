package com.binarfud.binarfud_challenge7.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDTO {
    private String merchantName;
    private String merchantLocation;
    private String merchantStatus;
    private String username;
}
