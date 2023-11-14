package com.binarfud.binarfud_challenge7.service;

import com.binarfud.binarfud_challenge7.dto.MerchantDTO;
import com.binarfud.binarfud_challenge7.dto.PaginationDTO;
import com.binarfud.binarfud_challenge7.entity.Merchant;
import com.binarfud.binarfud_challenge7.entity.User;
import com.binarfud.binarfud_challenge7.enums.ERole;
import com.binarfud.binarfud_challenge7.enums.MerchantStatus;
import com.binarfud.binarfud_challenge7.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge7.repository.MerchantRepository;
import com.binarfud.binarfud_challenge7.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MerchantServiceImpl implements  MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Method untuk mengecek merchant
     * @param merchantDTO
     * @return
     */
    @Override
    public Boolean checkMerchantAvailability(MerchantDTO merchantDTO) {
        Optional<MerchantDTO> merchantDTOOptional = Optional.ofNullable(merchantDTO);
        if (merchantDTOOptional.isPresent()) {
            log.debug("Checking merchant data availability with merchant name = {}", merchantDTO.getMerchantName());
            return merchantRepository.existsByMerchantNameAndUsername(merchantDTO.getMerchantName(), merchantDTO.getUsername());
        } else {
            throw new IllegalArgumentException("Merchant cannot be null");
        }
    }

    @Override
    public MerchantStatus toMerchantStatus(String merchantStatus) {
        if (merchantStatus.equals(MerchantStatus.OPEN.name()) || merchantStatus.equals(MerchantStatus.CLOSED.name())) {
            return MerchantStatus.valueOf(merchantStatus);
        } else {
            throw new IllegalArgumentException("Merchant status is invalid");
        }
    }

    @Override
    public Merchant convertMerchantDTOToMerchant(MerchantDTO merchantDTO) throws IllegalArgumentException {
        log.debug("Converting merchantDTO to Merchant with merchant name = {}", merchantDTO.getMerchantName());
        return Merchant.builder()
                .merchantName(merchantDTO.getMerchantName())
                .merchantLocation(merchantDTO.getMerchantLocation())
                .merchantStatus(toMerchantStatus(merchantDTO.getMerchantStatus()))
                .build();
    }

    @Override
    public List<MerchantDTO> convertMerchantPageToMerchantDTOList(Page<Merchant> merchantPage) {
        log.debug("Converting merchantPage to merchantDTOList");
        return merchantPage.getContent().stream()
                .map(val -> MerchantDTO.builder()
                        .merchantName(val.getMerchantName())
                        .merchantLocation(val.getMerchantLocation())
                        .merchantStatus(val.getMerchantStatus().name())
                        .username(val.getUser().getUsername())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Method untuk get semua merchant
     * @param page
     * @return
     */
    @Override
    public PaginationDTO<MerchantDTO> getAllMerchantWithPagination(Integer page) {
        log.debug("Getting Merchant with pagination with current page = {}", page);
        Page<Merchant> merchantPage = Optional.ofNullable(page)
                .map(val -> {
                    if (val < 1) {
                        throw new IllegalArgumentException("Page index must not be less than one");
                    }
                    return merchantRepository.findAll(PageRequest.of(val-1, 5));
                })
                .orElse(merchantRepository.findAll(PageRequest.of(0, 5)));
        if (merchantPage.isEmpty()) {
            throw new DataNotFoundException("Merchants");
        }
        List<MerchantDTO> merchantDTOS = convertMerchantPageToMerchantDTOList(merchantPage);
        log.info("Getting Merchant with pagination successful with current page = {}", page);
        return new PaginationDTO<>(merchantDTOS, page, merchantPage.getTotalPages());
    }

    /**
     * Method untuk get semua merchant yang statusnya open
     * @param page
     * @return
     */
    @Override
    public PaginationDTO<MerchantDTO> getAllMerchantByMerchantStatusWithPagination(Integer page) {
        log.debug("Getting Merchant with pagination with current page = {}", page);
        Page<Merchant> merchantPage = Optional.ofNullable(page)
                .map(val -> {
                    if (val < 1) {
                        throw new IllegalArgumentException("Page index must not be less than one");
                    }
                    return merchantRepository.findByMerchantStatusWithPagination(PageRequest.of(val-1, 5), MerchantStatus.OPEN);
                })
                .orElse(merchantRepository.findByMerchantStatusWithPagination(PageRequest.of(0, 5), MerchantStatus.OPEN));
        if (merchantPage.isEmpty()) {
            throw new DataNotFoundException("Merchants");
        }
        List<MerchantDTO> merchantDTOS = convertMerchantPageToMerchantDTOList(merchantPage);
        log.info("Getting Merchant with pagination successful with current page = {}", page);
        return new PaginationDTO<>(merchantDTOS, page, merchantPage.getTotalPages());
    }

    /**
     * Method untuk menambahkan merchant
     * @param merchantDTO
     * @throws IllegalArgumentException
     */
    @Override
    public void addMerchant(MerchantDTO merchantDTO) throws IllegalArgumentException {
        Optional<MerchantDTO> merchantDTOOptional = Optional.ofNullable(merchantDTO);
        if (merchantDTOOptional.isPresent()) {
            Optional<String> merchantNameOptional = Optional.ofNullable(merchantDTO.getMerchantName());
            Optional<String> merchantLocationOptional = Optional.ofNullable(merchantDTO.getMerchantLocation());
            Optional<String> merchantStatusOptional = Optional.ofNullable(merchantDTO.getMerchantStatus());
            Optional<String> usernameOptional = Optional.ofNullable(merchantDTO.getUsername());
            log.debug("Saving Merchant with merchant name = {}", merchantDTO.getMerchantName());
            if (merchantNameOptional.isPresent() && merchantLocationOptional.isPresent()
                    && merchantStatusOptional.isPresent() && usernameOptional.isPresent()) {
                User user = userRepository.findByUsernameAndRoleName(merchantDTO.getUsername(), ERole.ROLE_MERCHANT)
                        .orElseThrow(() -> new DataNotFoundException("User with username = "+merchantDTO.getUsername()));
                Merchant merchant = convertMerchantDTOToMerchant(merchantDTO);
                merchant.setUser(user);
                merchantRepository.save(merchant);
                log.info("Saving Merchant successful with merchant name = {}", merchantDTO.getMerchantName());
            } else {
                log.error("Saving Merchant unsuccessful with merchant name = {}", merchantDTO.getMerchantName());
                throw new IllegalArgumentException("Merchant name, Merchant location, and Merchant status cannot be null");
            }
        } else {
            throw new IllegalArgumentException("Merchant cannot be null");
        }
    }

    /**
     * Method untuk menambahkan merchant
     * @param merchantDTO
     * @param oldMerchantName
     * @throws IllegalArgumentException
     */
    @Override
    public void updateMerchant(MerchantDTO merchantDTO, String oldMerchantName) throws IllegalArgumentException {
        Optional<MerchantDTO> merchantDTOOptional = Optional.ofNullable(merchantDTO);
        Optional<String> oldMerchantNameOptional = Optional.ofNullable(oldMerchantName);
        if (merchantDTOOptional.isPresent() && oldMerchantNameOptional.isPresent()) {
            Optional<Merchant> merchantOptional = Optional.ofNullable(merchantRepository.findByMerchantName(oldMerchantName));
            log.debug("Updating Merchant with merchant name = {}", merchantDTO.getMerchantName());
            if (merchantOptional.isPresent()) {
                Merchant merchant = merchantOptional.get();
                merchant.setMerchantName(merchantDTO.getMerchantName());
                merchant.setMerchantLocation(merchantDTO.getMerchantLocation());
                merchant.setMerchantStatus(toMerchantStatus(merchantDTO.getMerchantStatus() == null ? "null" : merchantDTO.getMerchantStatus()));
                merchantRepository.save(merchant);
                log.info("Updating Merchant successful with merchant name = {}", merchantDTO.getMerchantName());
            } else {
                log.error("Updating Merchant unsuccessful with merchant name = {}", merchantDTO.getMerchantName());
                throw new DataNotFoundException("Merchant with merchant name = "+oldMerchantName);
            }
        } else {
            throw new IllegalArgumentException("Merchant and old merchant name cannot be null");
        }
    }

    /**
     * Method untuk update status merchant
     * @param merchantName
     * @param merchantStatus
     * @throws IllegalArgumentException
     */
    @Override
    public void updateMerchantStatus(String merchantName, String merchantStatus) throws IllegalArgumentException {
        Optional<String> merchantNameOptional = Optional.ofNullable(merchantName);
        Optional<String> merchantStatusOptional = Optional.ofNullable(merchantStatus);
        if (merchantNameOptional.isPresent() && merchantStatusOptional.isPresent()) {
            Optional<Merchant> merchantOptional = Optional.ofNullable(merchantRepository.findByMerchantName(merchantName));
            log.debug("Updating Merchant status with merchant name = {}", merchantName);
            if (merchantOptional.isPresent()) {
                Merchant merchant = merchantOptional.get();
                merchant.setMerchantStatus(toMerchantStatus(merchantStatus));
                merchantRepository.save(merchant);
                log.info("Updating Merchant status successful with merchant name = {}", merchantName);
            } else {
                log.error("Updating Merchant status unsuccessful with merchant name = {}", merchantName);
                throw new DataNotFoundException("Merchant with merchant name = "+merchantName);
            }
        } else {
            throw new IllegalArgumentException("Merchant name & merchant status cannot be null");
        }
    }

    /**
     * Method untuk hapus merchant
     * @param merchantName
     */
    @Override
    public void deleteMerchant(String merchantName) {
        Optional<String> merchantNameOptional = Optional.ofNullable(merchantName);
        if (merchantNameOptional.isPresent()) {
            Optional<Merchant> merchantOptional = Optional.ofNullable(merchantRepository.findByMerchantName(merchantName));
            log.debug("Deleting Merchant with merchant name = {}", merchantName);
            if (merchantOptional.isPresent()) {
                merchantRepository.deleteById(merchantOptional.get().getMerchantId());
                log.info("Deleting Merchant successful with merchant name = {}", merchantName);
            } else {
                log.error("Deleting Merchant unsuccessful with merchant name = {}", merchantName);
                throw new DataNotFoundException("Merchant with merchant name = "+merchantName);
            }
        } else {
            throw new IllegalArgumentException("Merchant name cannot be null");
        }
    }
}
