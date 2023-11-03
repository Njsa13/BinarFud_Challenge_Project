package com.binarfud.binarfud_challenge6.service;

import com.binarfud.binarfud_challenge6.dto.MerchantDTO;
import com.binarfud.binarfud_challenge6.dto.PaginationDTO;
import com.binarfud.binarfud_challenge6.entity.Merchant;
import com.binarfud.binarfud_challenge6.enums.MerchantStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MerchantService {
    Boolean checkMerchantAvailability(MerchantDTO merchantDTO);
    MerchantStatus toMerchantStatus(String merchantStatus);
    Merchant convertMerchantDTOToMerchant(MerchantDTO merchantDTO);
    List<MerchantDTO> convertMerchantPageToMerchantDTOList(Page<Merchant> merchantPage);
    PaginationDTO<MerchantDTO> getAllMerchantWithPagination(Integer page);
    PaginationDTO<MerchantDTO> getAllMerchantByMerchantStatusWithPagination(Integer page);
    void addMerchant(MerchantDTO merchantDTO);
    void updateMerchant(MerchantDTO merchantDTO, String oldMerchantName);
    void updateMerchantStatus(String merchantName, String merchantStatus);
    void deleteMerchant(String merchantName);
}
