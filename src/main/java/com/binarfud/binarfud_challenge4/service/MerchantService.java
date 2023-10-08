package com.binarfud.binarfud_challenge4.service;

import com.binarfud.binarfud_challenge4.dto.MerchantDTO;
import com.binarfud.binarfud_challenge4.dto.MerchantPageDTO;
import com.binarfud.binarfud_challenge4.entity.Merchant;
import org.springframework.data.domain.Page;

public interface MerchantService {
    MerchantPageDTO getAllMerchant(Integer page);
    Boolean addMerchant(MerchantDTO merchantDTO);
    String getMerchantIdByIndexAndPage(Integer index, Integer page);
    Boolean checkMerchantNameAvailability(MerchantDTO merchantDTO);
    Boolean updateMerchantStatus(MerchantDTO merchantDTO);
    Boolean updateMerchant(MerchantDTO merchantDTO);
    void deleteMerchant(MerchantDTO merchantDTO);
}
