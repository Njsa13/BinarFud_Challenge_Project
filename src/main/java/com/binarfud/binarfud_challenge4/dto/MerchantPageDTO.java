package com.binarfud.binarfud_challenge4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantPageDTO {
    private List<MerchantDTO> merchantDTOS;
    private Integer totalPages;
}
