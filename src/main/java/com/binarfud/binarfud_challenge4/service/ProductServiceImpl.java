package com.binarfud.binarfud_challenge4.service;

import com.binarfud.binarfud_challenge4.dto.ProductDTO;
import com.binarfud.binarfud_challenge4.dto.ProductPageDTO;
import com.binarfud.binarfud_challenge4.entity.Merchant;
import com.binarfud.binarfud_challenge4.entity.Product;
import com.binarfud.binarfud_challenge4.repository.MerchantRepository;
import com.binarfud.binarfud_challenge4.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MerchantRepository merchantRepository;

    /**
     * Method untuk mengambil product menggunakan paging
     * @param page
     * @return
     */
    @Override
    public ProductPageDTO getAllProduct(Integer page) {
        Page<Product> productPage = Optional.ofNullable(page)
                .map(val -> productRepository.findAll(PageRequest.of(val, 5)))
                .orElse(productRepository.findAll(PageRequest.of(0, 5)));

        List<ProductDTO> productDTOS = productPage.getContent().stream()
                .map(val -> ProductDTO.builder()
                        .productName(val.getProductName())
                        .price(val.getPrice())
                        .merchantName(val.getMerchant().getMerchantName())
                        .build())
                .collect(Collectors.toList());
        log.info("Getting product page successful with page : {} and total page : {}", page, productPage.getTotalPages());
        return ProductPageDTO.builder()
                .productDTOS(productDTOS)
                .totalPages(productPage.getTotalPages())
                .build();
    }

    /**
     * Method untuk menambah product
     * @param productDTO
     * @return
     */
    @Override
    public Boolean addProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .productName(productDTO.getProductName())
                .price(productDTO.getPrice())
                .merchant(merchantRepository.findByMerchantName(productDTO.getMerchantName()))
                .build();
        log.info("Saving product successful with product name : {}", productDTO.getProductName());
        return Optional.ofNullable(product)
                .map(val -> productRepository.save(val))
                .isPresent();
    }

    @Override
    public String getProductIdByIndexAndPage(Integer index, Integer page) throws NullPointerException, IndexOutOfBoundsException {
        List<Product> products = productRepository.findAll(PageRequest.of(page, 5)).getContent();
        return products.get(index - 1).getProductId();
    }

    @Override
    public Boolean checkProductAvailability(ProductDTO productDTO) {
        List<Product> products = Optional.ofNullable(productRepository.getProductsByMerchantNameAndProductName(productDTO.getMerchantName(),
                productDTO.getProductName())).orElse(Collections.emptyList());
        return products.isEmpty();
    }

    /**
     * Method untuk update product
     * @param productDTO
     * @return
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Boolean updateProduct(ProductDTO productDTO) throws NullPointerException, IndexOutOfBoundsException {
        String productId = getProductIdByIndexAndPage(productDTO.getIndex(), productDTO.getPage());
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setProductName(productDTO.getProductName());
            product.setPrice(productDTO.getPrice());

            productRepository.save(product);
            log.info("Update product successfull with product name : {}", productDTO.getProductName());
            return true;
        } else {
            log.error("Update product unsuccessfull with product name : {}", productDTO.getProductName());
            return false;
        }
    }

    /**
     * Method untuk delete product
     * @param productDTO
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    @Override
    public void deleteProduct(ProductDTO productDTO) throws  NullPointerException, IndexOutOfBoundsException {
        String productId = getProductIdByIndexAndPage(productDTO.getIndex(), productDTO.getPage());
        productRepository.deleteById(productId);
        log.info("Delete product successfull with name : {}", productDTO.getProductName());
    }
}
