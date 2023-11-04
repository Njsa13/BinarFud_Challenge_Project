package com.binarfud.binarfud_challenge6.controller;

import com.binarfud.binarfud_challenge6.dto.MerchantDTO;
import com.binarfud.binarfud_challenge6.dto.PaginationDTO;
import com.binarfud.binarfud_challenge6.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge6.dto.response.ErrorResponse;
import com.binarfud.binarfud_challenge6.dto.response.Response;
import com.binarfud.binarfud_challenge6.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    /**
     * Method untuk add merchant
     * @param merchantDTO
     * @return
     */
    @Secured({"ROLE_MERCHANT", "ROLE_ADMIN"})
    @PostMapping(value = "/add", consumes = "application/json")
    public ResponseEntity<String> addMerchant(@RequestBody MerchantDTO merchantDTO) {
        try {
            if (!merchantService.checkMerchantAvailability(merchantDTO)) {
                merchantService.addMerchant(merchantDTO);
                log.info("Merchant name not available with merchant name = {}", merchantDTO.getMerchantName());
                return ResponseEntity.ok("Add Merchant Successful");
            } else {
                log.info("Merchant name available with merchant name = {}", merchantDTO.getMerchantName());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Merchant name available");
            }
        } catch (IllegalArgumentException e) {
            log.error("Failed to add merchant with merchant name = {}", merchantDTO.getMerchantName());
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    /**
     * Method untuk mengambil semua merchant
     * @param page
     * @return
     */
    @Secured({"ROLE_ADMIN", "ROLE_MERCHANT"})
    @GetMapping(value = "/get-all", produces = "application/json")
    public ResponseEntity<Response<PaginationDTO<MerchantDTO>>> getAllMerchant(@RequestParam("page") Integer page) {
        try {
            return ResponseEntity.ok(new Response<>(merchantService.getAllMerchantWithPagination(page), true, null));
        } catch (DataNotFoundException e) {
            log.error("Getting Merchant with pagination unsuccessful with current page = {}", page);
            return new ResponseEntity<>(new Response<>(null, false, ErrorResponse.builder()
                    .errorMessage(e.getMessage())
                    .errorCode(HttpStatus.NOT_FOUND.value())
                    .build()), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            log.error("Getting Merchant with pagination unsuccessful with current page = {}", page);
            return new ResponseEntity<>(new Response<>(null, false, ErrorResponse.builder()
                    .errorMessage(e.getMessage())
                    .errorCode(HttpStatus.BAD_REQUEST.value())
                    .build()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Method untuk mengambil semua merchant yang statusnya open
     * @param page
     * @return
     */
    @Secured({"ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_MERCHANT"})
    @GetMapping(value = "/get-open", produces = "application/json")
    public ResponseEntity<Response<PaginationDTO<MerchantDTO>>> getOpenMerchant(@RequestParam("page") Integer page) {
        try {
            return ResponseEntity.ok(new Response<>(merchantService.getAllMerchantByMerchantStatusWithPagination(page), true, null));
        } catch (DataNotFoundException e) {
            log.error("Getting Merchant with pagination unsuccessful with current page = {}", page);
            return new ResponseEntity<>(new Response<>(null, false, ErrorResponse.builder()
                    .errorMessage(e.getMessage())
                    .errorCode(HttpStatus.NOT_FOUND.value())
                    .build()), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            log.error("Getting Merchant with pagination unsuccessful with current page = {}", page);
            return new ResponseEntity<>(new Response<>(null, false, ErrorResponse.builder()
                    .errorMessage(e.getMessage())
                    .errorCode(HttpStatus.BAD_REQUEST.value())
                    .build()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Method untuk update merchant
     * @param merchantDTO
     * @param merchantName
     * @return
     */
    @Secured({"ROLE_MERCHANT", "ROLE_ADMIN"})
    @PutMapping(value = "/update/{merchantName}", consumes = "application/json")
    public ResponseEntity<String> updateMerchant(@RequestBody MerchantDTO merchantDTO,
                                                 @PathVariable("merchantName") String merchantName) {
        try {
            if (!(merchantService.checkMerchantAvailability(merchantDTO) &&
                    merchantService.checkMerchantAvailability(MerchantDTO.builder()
                            .merchantName(merchantName)
                            .build()) &&
                    !merchantDTO.getMerchantName().equals(merchantName))) {
                merchantService.updateMerchant(merchantDTO, merchantName);
                log.info("Merchant name not available with merchant name = {}", merchantDTO.getMerchantName());
                return ResponseEntity.ok("Update Merchant Successful");
            } else {
                log.info("Merchant name available with merchant name = {}", merchantDTO.getMerchantName());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Merchant name available");
            }
        }  catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    /**
     * Method untuk update status merchant
     * @param merchantName
     * @param merchantStatus
     * @return
     */
    @Secured({"ROLE_MERCHANT", "ROLE_ADMIN"})
    @PutMapping(value = "/update-status/{merchantName}", consumes = "application/json")
    public ResponseEntity<String> updateMerchantStatus(@PathVariable("merchantName") String merchantName,
                                                       @RequestParam("status") String merchantStatus) {
        try {
            merchantService.updateMerchantStatus(merchantName, merchantStatus);
            return ResponseEntity.ok("Update Successful");
        }  catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    /**
     * Method untuk hapus merchant
     * @param merchantName
     * @return
     */
    @Secured("ROLE_ADMIN")
    @DeleteMapping(value = "/delete/{merchantName}")
    public ResponseEntity<String> deleteMerchant(@PathVariable("merchantName") String merchantName) {
        try {
            merchantService.deleteMerchant(merchantName);
            return ResponseEntity.ok("Delete Successful");
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}
