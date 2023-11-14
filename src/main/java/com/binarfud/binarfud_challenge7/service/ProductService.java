package com.binarfud.binarfud_challenge7.service;

import com.binarfud.binarfud_challenge7.dto.PaginationDTO;
import com.binarfud.binarfud_challenge7.dto.ProductDTO;
import com.binarfud.binarfud_challenge7.entity.Merchant;
import com.binarfud.binarfud_challenge7.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Boolean checkProductAvailability(ProductDTO productDTO);
    Product convertProductDTOToProduct(ProductDTO productDTO, Merchant merchant);
    List<ProductDTO> convertProductPageToProductDTOList(Page<Product> productPage);
    ProductDTO getJson(String productDTO);
    byte[] getImageByProductNameAndMerchantName(String productName, String merchantName);
    PaginationDTO<ProductDTO> getAllProductWithPagination(Integer page);
    PaginationDTO<ProductDTO> getAllProductByMerchantStatusWithPagination(Integer page);
    void addProduct(ProductDTO productDTO);
    void updateProduct(ProductDTO productDTO, String oldProductName);
    void deleteProduct(String productName, String merchantName);
}
