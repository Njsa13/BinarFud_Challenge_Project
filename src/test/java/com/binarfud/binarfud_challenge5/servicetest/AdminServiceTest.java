package com.binarfud.binarfud_challenge5.servicetest;

import com.binarfud.binarfud_challenge5.dto.AdminDTO;
import com.binarfud.binarfud_challenge5.entity.Admin;
import com.binarfud.binarfud_challenge5.repository.AdminRepository;
import com.binarfud.binarfud_challenge5.service.AdminService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminServiceTest {
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;

    @BeforeEach
    void init() {
        Admin admin = Admin.builder()
                .username("TestAdminUsername")
                .password("TestAdminPassword")
                .build();
        adminRepository.save(admin);
    }

    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
    }

    @Test
    void checkAdminLoginTest() {
        AdminDTO adminDTO = AdminDTO.builder()
                .username("TestAdminUsername")
                .password("TestAdminPassword")
                .build();
        Boolean result = adminService.checkAdminLogin(adminDTO);
        Assertions.assertTrue(result);
    }

    @Test
    void checkAdminLoginTest_DataNotAvailable() {
        AdminDTO adminDTO = AdminDTO.builder()
                .username("TestAdminUsername1")
                .password("TestAdminPassword1")
                .build();
        Boolean result = adminService.checkAdminLogin(adminDTO);
        Assertions.assertFalse(result);
    }

    @Test
    void checkAdminLoginTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> adminService.checkAdminLogin(null));
    }
}
