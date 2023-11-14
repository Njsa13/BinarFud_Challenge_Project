package com.binarfud.binarfud_challenge7.controller;

import com.binarfud.binarfud_challenge7.dto.OrderDTO;
import com.binarfud.binarfud_challenge7.dto.OrderDetailDTO;
import com.binarfud.binarfud_challenge7.dto.PaginationDTO;
import com.binarfud.binarfud_challenge7.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge7.dto.response.ErrorResponse;
import com.binarfud.binarfud_challenge7.dto.response.Response;
import com.binarfud.binarfud_challenge7.service.OrderDetailService;
import com.binarfud.binarfud_challenge7.service.OrderService;
import com.binarfud.binarfud_challenge7.utils.AuthExtractor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private AuthExtractor authExtractor;

    /**
     * Method untuk updat order
     * @param orderDTO
     * @param request
     * @return
     */
    @PutMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<String> updateOrder(@RequestBody OrderDTO orderDTO,
                                              HttpServletRequest request) {
        String username = authExtractor.extractorUsernameFromHeaderCookie(request);
        try {
            orderService.updateOrder(orderDTO, username);
            return ResponseEntity.ok("Update Order Successful");
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    /**
     * Method untuk add order detail
     * @param orderDetailDTO
     * @param request
     * @return
     */
    @PostMapping(value = "/detail/add", consumes = "application/json")
    public ResponseEntity<String> addOrderDetail(@RequestBody OrderDetailDTO orderDetailDTO,
                                                 HttpServletRequest request) {
        String username = authExtractor.extractorUsernameFromHeaderCookie(request);
        try {
            if (!orderDetailService.checkOrderDetailAvailability(orderDetailDTO, username)) {
                orderDetailService.addOrderDetail(orderDetailDTO, username);
                log.info("Order detail not available with username = {}, product name = {}, and merchant name = {}",
                        username, orderDetailDTO.getProductName(), orderDetailDTO.getMerchantName());
                return ResponseEntity.ok("Add Order Detail Successful");
            } else {
                log.info("Order detail not available with username = {}, product name = {}, and merchant name = {}",
                        username, orderDetailDTO.getProductName(), orderDetailDTO.getMerchantName());
                orderDetailService.updateOrderDetail(orderDetailDTO, username);
                return ResponseEntity.ok("Update Order Detail Successful");
            }
        } catch (DataNotFoundException e) {
            log.error("Failed to add order detail with username = {}, product name = {}, and merchant name = {}",
                    username, orderDetailDTO.getProductName(), orderDetailDTO.getMerchantName());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Failed to add order detail with username = {}, product name = {}, and merchant name = {}",
                    username, orderDetailDTO.getProductName(), orderDetailDTO.getMerchantName());
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    /**
     * Method untuk get semua order detail
     * @param page
     * @param request
     * @return
     */
    @GetMapping(value = "/detail/get", produces = "application/json")
    public ResponseEntity<Response<PaginationDTO<OrderDetailDTO>>> getOrderDetail(@RequestParam("page") Integer page,
                                                                                  HttpServletRequest request) {
        try {
            String username = authExtractor.extractorUsernameFromHeaderCookie(request);
            return ResponseEntity.ok(new Response<>(orderDetailService.getAllOrderDetailWithPagination(page, username), true, null));
        } catch (DataNotFoundException e) {
            log.error("Getting Order Detail with pagination unsuccessful with current page = {}", page);
            return new ResponseEntity<>(new Response<>(null, false, ErrorResponse.builder()
                    .errorMessage(e.getMessage())
                    .errorCode(HttpStatus.NOT_FOUND.value())
                    .build()), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            log.error("Getting Order Detail with pagination unsuccessful with current page = {}", page);
            return new ResponseEntity<>(new Response<>(null, false, ErrorResponse.builder()
                    .errorMessage(e.getMessage())
                    .errorCode(HttpStatus.BAD_REQUEST.value())
                    .build()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Method untuk delete order detail
     * @param productName
     * @param merchantName
     * @param request
     * @return
     */
    @DeleteMapping(value = "/detail/delete")
    public ResponseEntity<String> deleteMerchant(@RequestParam("productName") String productName,
                                                 @RequestParam("merchantName") String merchantName,
                                                 HttpServletRequest request) {
        try {
            String username = authExtractor.extractorUsernameFromHeaderCookie(request);
            orderDetailService.deleteOrderDetail(productName, merchantName, username);
            return ResponseEntity.ok("Delete Successful");
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    /**
     * Method untuk delete order detail
     * @param request
     * @return
     */
    @GetMapping(value = "/print-invoice", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> printOrder(HttpServletRequest request) {
        try {
            String username = authExtractor.extractorUsernameFromHeaderCookie(request);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Invoice.pdf")
                    .body(orderService.printInvoice(username));
        } catch (DataNotFoundException | FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (JRException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(null);
        }
    }
}
