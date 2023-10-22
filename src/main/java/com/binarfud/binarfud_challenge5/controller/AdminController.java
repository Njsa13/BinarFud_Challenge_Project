package com.binarfud.binarfud_challenge5.controller;

import com.binarfud.binarfud_challenge5.dto.AdminDTO;
import com.binarfud.binarfud_challenge5.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * Method untuk login admin
     * @param adminDTO
     * @return
     */
    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<String> checkAdminLogin(@RequestBody AdminDTO adminDTO) {
        try {
            if (adminService.checkAdminLogin(adminDTO)) {
                log.info("Admin data available with username = {}", adminDTO.getUsername());
                return ResponseEntity.ok("Login Successful");
            } else {
                log.info("Admin data not available with username = {}", adminDTO.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Login Failed");
            }
        } catch (IllegalArgumentException e) {
            log.error("Failed to check username availability with username = {}",adminDTO.getUsername());
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}
