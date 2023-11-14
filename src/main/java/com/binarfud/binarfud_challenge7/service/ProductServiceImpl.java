package com.binarfud.binarfud_challenge7.service;

import com.binarfud.binarfud_challenge7.dto.PaginationDTO;
import com.binarfud.binarfud_challenge7.dto.ProductDTO;
import com.binarfud.binarfud_challenge7.entity.Merchant;
import com.binarfud.binarfud_challenge7.entity.Product;
import com.binarfud.binarfud_challenge7.enums.MerchantStatus;
import com.binarfud.binarfud_challenge7.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge7.repository.MerchantRepository;
import com.binarfud.binarfud_challenge7.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
     * Method untuk mengecek ketersediaan product
     * @param productDTO
     * @return
     */
    @Override
    public Boolean checkProductAvailability(ProductDTO productDTO) {
        Optional<ProductDTO> productDTOOptional = Optional.ofNullable(productDTO);
        if (productDTOOptional.isPresent()) {
            log.debug("Checking Product data availability with product name = {} and merchant name = {}",
                    productDTO.getProductName(), productDTO.getMerchantName());
            return productRepository.existsByProductNameAndMerchantName(productDTO.getProductName(), productDTO.getMerchantName());
        } else {
            throw new IllegalArgumentException("Product cannot be null");
        }
    }

    @Override
    public Product convertProductDTOToProduct(ProductDTO productDTO, Merchant merchant) {
        log.debug("Converting productDTO to Product with product name = {} ", productDTO.getProductName());
        return Product.builder()
                .productName(productDTO.getProductName())
                .price(productDTO.getPrice())
                .merchant(merchant)
                .imageFile(productDTO.getImageFile())
                .build();
    }

    @Override
    public List<ProductDTO> convertProductPageToProductDTOList(Page<Product> productPage) {
        log.debug("Converting productPage to productDTOList");
        return productPage.getContent().stream()
                .map(val -> ProductDTO.builder()
                        .productName(val.getProductName())
                        .price(val.getPrice())
                        .merchantName(val.getMerchant().getMerchantName())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Method untuk mengubah Json menjadi ProductDTO
     * @param productDTOString
     * @return
     */
    @Override
    public ProductDTO getJson(String productDTOString) {
        ProductDTO productDTOJson = new ProductDTO();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            productDTOJson = objectMapper.readValue(productDTOString, ProductDTO.class);
        } catch (IOException e) {
            log.error("IOException : {}", e.getMessage());
        }
        return productDTOJson;
    }

    /**
     * Method untuk get image product
     * @param productName
     * @param merchantName
     * @return
     */
    @Override
    public byte[] getImageByProductNameAndMerchantName(String productName, String merchantName) {
        Optional<String> productNameOptional = Optional.ofNullable(productName);
        Optional<String> merchantNameOptional = Optional.ofNullable(merchantName);
        if (productNameOptional.isPresent() && merchantNameOptional.isPresent()) {
            log.debug("Getting product image with product name = {} and merchant name = {}", productName, merchantName);
            Optional<Product> productOptional = Optional
                    .ofNullable(productRepository.findByProductNameAndMerchantName(productName, merchantName));
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                if (Optional.ofNullable(product.getImageFile()).isPresent()) {
                    log.info("Getting product image successful with product name = {} and merchant name = {}", productName, merchantName);
                    return product.getImageFile();
                }
            }
            log.error("Getting product image unsuccessful with product name = {} and merchant name = {}", productName, merchantName);
            throw new  DataNotFoundException("Image with product name = "+productName+" and merchant name = "+merchantName);
        } else {
            throw new IllegalArgumentException("Product name cannot be null");
        }
    }

    /**
     * Method untuk get product
     * @param page
     * @return
     */
    @Override
    public PaginationDTO<ProductDTO> getAllProductWithPagination(Integer page) {
        log.debug("Getting Product with pagination with current page = {}", page);
        Page<Product> productPage = Optional.ofNullable(page)
                .map(val -> {
                    if (val < 1) {
                        throw new IllegalArgumentException("Page index must not be less than one");
                    }
                    return productRepository.findAll(PageRequest.of(val-1, 5));
                })
                .orElse(productRepository.findAll(PageRequest.of(0, 5)));
        if (productPage.isEmpty()) {
            throw new DataNotFoundException("Product");
        }
        List<ProductDTO> productDTOS = convertProductPageToProductDTOList(productPage);
        log.info("Getting Product with pagination successful with current page = {}", page);
        return new PaginationDTO<>(productDTOS, page, productPage.getTotalPages());
    }

    /**
     * Method untuk get product yang status merhant-nya open
     * @param page
     * @return
     */
    @Override
    public PaginationDTO<ProductDTO> getAllProductByMerchantStatusWithPagination(Integer page) {
        log.debug("Getting Product with pagination with current page = {}", page);
        Page<Product> productPage = Optional.ofNullable(page)
                .map(val -> {
                    if (val < 1) {
                        throw new IllegalArgumentException("Page index must not be less than one");
                    }
                    return productRepository.findByMerchantStatusWithPagination(PageRequest.of(val-1, 5), MerchantStatus.OPEN);
                })
                .orElse(productRepository.findByMerchantStatusWithPagination(PageRequest.of(0, 5), MerchantStatus.OPEN));
        if (productPage.isEmpty()) {
            throw new DataNotFoundException("Product");
        }
        List<ProductDTO> productDTOS = convertProductPageToProductDTOList(productPage);
        log.info("Getting Product with pagination successful with current page = {}", page);
        return new PaginationDTO<>(productDTOS, page, productPage.getTotalPages());
    }

    /**
     * Method untuk menambah product
     * @param productDTO
     */
    @Override
    public void addProduct(ProductDTO productDTO) {
        Optional<ProductDTO> productDTOOptional = Optional.ofNullable(productDTO);
        if (productDTOOptional.isPresent()) {
            Optional<String> productNameOptional = Optional.ofNullable(productDTO.getProductName());
            Optional<Integer> priceOptional = Optional.ofNullable(productDTO.getPrice());
            Optional<String> merchantNameOptional = Optional.ofNullable(productDTO.getMerchantName());
            if (productNameOptional.isPresent() && priceOptional.isPresent() && merchantNameOptional.isPresent()) {
                Optional<Merchant> merchantOptional = Optional.ofNullable(merchantRepository.findByMerchantName(productDTO.getMerchantName()));
                log.debug("Saving Product with product name = {} and merchant name = {}",
                        productDTO.getProductName(), productDTO.getMerchantName());
                if (merchantOptional.isPresent()) {
                    Merchant merchant = merchantOptional.get();
                    productRepository.save(convertProductDTOToProduct(productDTO, merchant));
                    log.info("Saving Product successful with product name = {} and merchant name = {}",
                            productDTO.getProductName(), productDTO.getMerchantName());
                } else {
                    log.error("Saving Product unsuccessful with product name = {} and merchant name = {}",
                            productDTO.getProductName(), productDTO.getMerchantName());
                    throw new DataNotFoundException("Merchant with merchant name = "+productDTO.getMerchantName());
                }
            } else {
                throw new IllegalArgumentException("Product name, Price, and Merchant name cannot be null");
            }
        } else {
            throw new IllegalArgumentException("Product cannot be null");
        }
    }

    /**
     * Method untuk update product
     * @param productDTO
     * @param oldProductName
     */
    @Override
    public void updateProduct(ProductDTO productDTO, String oldProductName) {
        Optional<ProductDTO> productDTOOptional = Optional.ofNullable(productDTO);
        Optional<String> oldProductNameOptional = Optional.ofNullable(oldProductName);
        if (productDTOOptional.isPresent() && oldProductNameOptional.isPresent()) {
            Optional<Product> productOptional = Optional.ofNullable(productRepository
                    .findByProductNameAndMerchantName(oldProductName, productDTO.getMerchantName()));
            log.debug("Updating Product with product name = {} and merchant name = {}",
                    productDTO.getProductName(), productDTO.getMerchantName());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                product.setProductName(productDTO.getProductName());
                product.setPrice(productDTO.getPrice());
                product.setImageFile(productDTO.getImageFile());
                productRepository.save(product);
                log.info("Updating Product successful with product name = {} and merchant name = {}",
                        productDTO.getProductName(), productDTO.getMerchantName());
            } else {
                log.error("Updating Product successful with product name = {} and merchant name = {}",
                        productDTO.getProductName(), productDTO.getMerchantName());
                throw new DataNotFoundException("Product with product name = "+oldProductName+ " and merchant name = "+productDTO.getMerchantName());
            }
        } else {
            throw new IllegalArgumentException("Product and old product name cannot be null");
        }
    }

    /**
     * Method untuk hapus product
     * @param productName
     * @param merchantName
     */
    @Override
    public void deleteProduct(String productName, String merchantName) {
        Optional<String> productNameOptional = Optional.ofNullable(productName);
        Optional<String> merchantNameOptional = Optional.ofNullable(merchantName);
        if (productNameOptional.isPresent() && merchantNameOptional.isPresent()) {
            Optional<Product> productOptional = Optional.ofNullable(productRepository
                    .findByProductNameAndMerchantName(productName, merchantName));
            log.debug("Deleting Product with product name = {} and merchant name = {}",
                    productName, merchantName);
            if (productOptional.isPresent()) {
                productRepository.deleteById(productOptional.get().getProductId());
                log.info("Deleting Product successful with product name = {} and merchant name = {}",
                        productName, merchantName);
            } else {
                log.error("Deleting Product unsuccessful with product name = {} and merchant name = {}",
                        productName, merchantName);
                throw new DataNotFoundException("Product with product name = "+productName+" and merchant name = "+merchantName);
            }
        } else {
            throw new IllegalArgumentException("Product name and Merchant name cannot be null");
        }
    }
}
