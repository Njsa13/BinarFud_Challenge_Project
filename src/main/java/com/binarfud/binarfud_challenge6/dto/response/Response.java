package com.binarfud.binarfud_challenge6.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private T data;
    private boolean isSuccess;
    private ErrorResponse error;
}
