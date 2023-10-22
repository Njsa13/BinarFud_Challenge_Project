package com.binarfud.binarfud_challenge5.controller;

import com.binarfud.binarfud_challenge5.dto.MerchantDTO;
import com.binarfud.binarfud_challenge5.dto.PaginationDTO;
import com.binarfud.binarfud_challenge5.dto.ProductDTO;
import com.binarfud.binarfud_challenge5.dto.UserDTO;
import com.binarfud.binarfud_challenge5.entity.Product;
import com.binarfud.binarfud_challenge5.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge5.response.ErrorResponse;
import com.binarfud.binarfud_challenge5.response.Response;
import com.binarfud.binarfud_challenge5.service.ProductService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Method untuk menambah product
     * @param productDTOString
     * @param imageFile
     * @return
     */
    @PostMapping(value = "/add", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addProduct(@RequestPart("productDTO") String productDTOString,
                                             @RequestPart("imageFile") MultipartFile imageFile) {
        ProductDTO productDTO = productService.getJson(productDTOString);
        try {
            productDTO.setImageFile(imageFile.getBytes());
            if (!productService.checkProductAvailability(productDTO)) {
                productService.addProduct(productDTO);
                log.info("Product name not available with product name = {} and merchant name = {}", productDTO.getProductName(), productDTO.getProductName());
                return ResponseEntity.ok("Add Product Successful");
            } else {
                log.info("Product name available with product name = {} and merchant name = {}", productDTO.getProductName(), productDTO.getProductName());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Product name available");
            }
        } catch (DataNotFoundException e) {
            log.error("Failed to add merchant with product name = {}", productDTO.getProductName());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalArgumentException | IOException e) {
            log.error("Failed to add merchant with product name = {}", productDTO.getProductName());
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    /**
     * Method untuk get semua product
     * @param page
     * @return
     */
    @GetMapping(value = "/get-all", produces = "application/json")
    public ResponseEntity<Response<PaginationDTO<ProductDTO>>> getAllProduct(@RequestParam("page") Integer page) {
        try {
            return ResponseEntity.ok(new Response<>(productService.getAllProductWithPagination(page), true, null));
        } catch (DataNotFoundException e) {
            log.error("Getting Product with pagination unsuccessful with current page = {}", page);
            return new ResponseEntity<>(new Response<>(null, false, ErrorResponse.builder()
                    .errorMessage(e.getMessage())
                    .errorCode(HttpStatus.NOT_FOUND.value())
                    .build()), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            log.error("Getting Product with pagination unsuccessful with current page = {}", page);
            return new ResponseEntity<>(new Response<>(null, false, ErrorResponse.builder()
                    .errorMessage(e.getMessage())
                    .errorCode(HttpStatus.BAD_REQUEST.value())
                    .build()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Method untuk get semua product yang status merchantnya open
     * @param page
     * @return
     */
    @GetMapping(value = "/get-open", produces = "application/json")
    public ResponseEntity<Response<PaginationDTO<ProductDTO>>> getOpenProduct(@RequestParam("page") Integer page) {
        try {
            return ResponseEntity.ok(new Response<>(productService.getAllProductByMerchantStatusWithPagination(page), true, null));
        } catch (DataNotFoundException e) {
            log.error("Getting Product with pagination unsuccessful with current page = {}", page);
            return new ResponseEntity<>(new Response<>(null, false, ErrorResponse.builder()
                    .errorMessage(e.getMessage())
                    .errorCode(HttpStatus.NOT_FOUND.value())
                    .build()), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            log.error("Getting Product with pagination unsuccessful with current page = {}", page);
            return new ResponseEntity<>(new Response<>(null, false, ErrorResponse.builder()
                    .errorMessage(e.getMessage())
                    .errorCode(HttpStatus.BAD_REQUEST.value())
                    .build()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Method untuk update product
     * @param productDTOString
     * @param imageFile
     * @param productName
     * @return
     */
    @PutMapping(value = "/update/{productName}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateProduct(@RequestPart("productDTO") String productDTOString,
                                                @RequestPart("imageFile") MultipartFile imageFile,
                                                @PathVariable("productName") String productName) {
        ProductDTO productDTO = productService.getJson(productDTOString);
        try {
            productDTO.setImageFile(imageFile.getBytes());
            if (!(productService.checkProductAvailability(productDTO) &&
                    productService.checkProductAvailability(ProductDTO.builder()
                            .productName(productName)
                            .merchantName(productDTO.getMerchantName())
                            .build()) &&
                    !productDTO.getProductName().equals(productName))) {
                productService.updateProduct(productDTO, productName);
                log.info("Product name not available with product name = {} and merchant name = {}", productDTO.getProductName(), productDTO.getProductName());
                return ResponseEntity.ok("Update Product Successful");
            } else {
                log.info("Product name available with product name = {} and merchant name = {}", productDTO.getProductName(), productDTO.getProductName());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Product name available");
            }
        }  catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalArgumentException | IOException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    /**
     * Method untuk hapus product
     * @param productName
     * @param merchantName
     * @return
     */
    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteProduct(@RequestParam("productName") String productName,
                                                @RequestParam("merchantName") String merchantName) {
        try {
            productService.deleteProduct(productName, merchantName);
            return ResponseEntity.ok("Delete Successful");
        }  catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping(value = "/download-image/{productName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> downloadProductImage(@PathVariable("productName") String productName,
                                       @RequestParam("merchantName") String merchantName) {
        try {
            return ResponseEntity.ok(productService.getImageByProductNameAndMerchantName(productName, merchantName));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(null);
        }
    }
}
