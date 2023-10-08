package com.binarfud.binarfud_challenge4.service;

import com.binarfud.binarfud_challenge4.dto.ProductDTO;
import com.binarfud.binarfud_challenge4.dto.ProductPageDTO;

public interface ProductService {
    ProductPageDTO getAllProduct(Integer page);
    Boolean addProduct(ProductDTO productDTO);
    String getProductIdByIndexAndPage(Integer index, Integer page);
    Boolean checkProductAvailability(ProductDTO productDTO);
    Boolean updateProduct(ProductDTO productDTO);
    void deleteProduct(ProductDTO productDTO);
}
