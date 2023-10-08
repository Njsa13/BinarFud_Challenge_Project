package com.binarfud.binarfud_challenge4.service;

import com.binarfud.binarfud_challenge4.dto.UserDTO;
import com.binarfud.binarfud_challenge4.entity.Admin;
import com.binarfud.binarfud_challenge4.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    /**
     * Method untuk mengecek login admin
     * @param admin
     * @return
     */
    @Override
    public Boolean checkAdminLogin(Admin admin) {
        return Optional.ofNullable(adminRepository.findByUsernameAndPassword(admin.getUsername(), admin.getPassword()))
                .map(val -> {
                    log.info("Admin is available with username : {}", val.getUsername());
                    return true;
                })
                .orElseGet(() -> {
                    log.info("Admin is not available with username : {}", admin.getUsername());
                    return false;
                });
    }

    /**
     * Method untuk mengecek admin username
     * @param userDTO
     * @return
     */
    public Boolean checkUsernameAvailability(UserDTO userDTO) {
        return Optional.ofNullable(adminRepository.findByUsername(userDTO.getUsername()))
                .map(val -> {
                    log.info("Admin is available with username : {}", val.getUsername());
                    return true;
                })
                .orElseGet(() -> {
                    log.info("Admin is not available with username : {}", userDTO.getUsername());
                    return false;
                });
    }
}
