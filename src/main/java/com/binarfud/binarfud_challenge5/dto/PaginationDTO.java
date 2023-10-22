package com.binarfud.binarfud_challenge5.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDTO<T> {
    private List<T> data;
    private Integer currentPage;
    private Integer totalPages;
}
