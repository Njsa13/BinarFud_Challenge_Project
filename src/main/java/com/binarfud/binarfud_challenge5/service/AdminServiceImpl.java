package com.binarfud.binarfud_challenge5.service;

import com.binarfud.binarfud_challenge5.dto.AdminDTO;
import com.binarfud.binarfud_challenge5.repository.AdminRepository;
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
     * Method untuk login admin
     * @param adminDTO
     * @return
     */
    @Override
    public Boolean checkAdminLogin(AdminDTO adminDTO) {
        Optional<AdminDTO> adminDTOOptional = Optional.ofNullable(adminDTO);
        if (adminDTOOptional.isPresent()) {
            log.debug("Checking admin data availability with username = {}", adminDTO.getUsername());
            return adminRepository.existsByUsernameAndPassword(adminDTO.getUsername(), adminDTO.getPassword());
        } else {
            throw new IllegalArgumentException("Admin cannot be null");
        }
    }
}
