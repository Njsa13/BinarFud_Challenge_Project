package com.binarfud.binarfud_challenge4.service;

import com.binarfud.binarfud_challenge4.dto.MerchantDTO;
import com.binarfud.binarfud_challenge4.dto.MerchantPageDTO;
import com.binarfud.binarfud_challenge4.entity.Merchant;
import com.binarfud.binarfud_challenge4.repository.MerchantRepository;
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
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MerchantRepository merchantRepository;

    /**
     * Method untuk mengambil merchant menggunakan paging
     * @param page
     * @return
     */
    @Override
    public MerchantPageDTO getAllMerchant(Integer page) {
        Page<Merchant> merchantPage = Optional.ofNullable(page)
                .map(val -> merchantRepository.findAll(PageRequest.of(val, 5)))
                .orElse(merchantRepository.findAll(PageRequest.of(0, 5)));

        List<MerchantDTO> merchantDTOS = merchantPage.getContent().stream()
                .map(val -> MerchantDTO.builder()
                        .merchantName(val.getMerchantName())
                        .merchantLocation(val.getMerchantLocation())
                        .open(val.getOpen())
                        .build())
                .collect(Collectors.toList());
        log.info("Getting merchant page successful with page : {} and total page : {}", page, merchantPage.getTotalPages());
        return MerchantPageDTO.builder()
                .merchantDTOS(merchantDTOS)
                .totalPages(merchantPage.getTotalPages())
                .build();
    }

    /**
     * Method untuk menambah atau insert merchant
     * @param merchantDTO
     * @return
     */
    @Override
    public Boolean addMerchant(MerchantDTO merchantDTO) {
        Merchant merchant = Merchant.builder()
                .merchantName(merchantDTO.getMerchantName())
                .merchantLocation(merchantDTO.getMerchantLocation())
                .open(merchantDTO.getOpen())
                .build();
        log.info("Saving merchant successful with merchant name : {}", merchantDTO.getMerchantName());
        return Optional.ofNullable(merchant)
                .map(val -> merchantRepository.save(val))
                .isPresent();
    }

    /**
     * Method untuk mendapatkan id berdasarkan page dan index
     * @param index
     * @param page
     * @return
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    @Override
    public String getMerchantIdByIndexAndPage(Integer index, Integer page) throws NullPointerException, IndexOutOfBoundsException {
        List<Merchant> merchants = merchantRepository.findAll(PageRequest.of(page, 5)).getContent();
        return merchants.get(index - 1).getMerchantId();
    }

    /**
     * Method untuk mengecek username
     * @param merchantDTO
     * @return
     */
    @Override
    public Boolean checkMerchantNameAvailability(MerchantDTO merchantDTO) {
        return Optional.ofNullable(merchantRepository.findByMerchantName(merchantDTO.getMerchantName()))
                .map(val -> {
                    log.info("Merchant name is available with name : {}", val.getMerchantName());
                    return true;
                })
                .orElseGet(() -> {
                    log.info("Merchant name is not available with name : {}", merchantDTO.getMerchantName());
                    return false;
                });
    }

    /**
     * Method untuk update merchant status
     * @param merchantDTO
     * @return
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Boolean updateMerchantStatus(MerchantDTO merchantDTO) throws NullPointerException, IndexOutOfBoundsException {
        String merchantId = getMerchantIdByIndexAndPage(merchantDTO.getIndex(), merchantDTO.getPage());
        Optional<Merchant> merchantOptional = merchantRepository.findById(merchantId);

        if (merchantOptional.isPresent()) {
            Merchant merchant = merchantOptional.get();
            merchant.setOpen(merchantDTO.getOpen());

            merchantRepository.save(merchant);
            log.info("Update merchant status successfull with status : {}", merchantDTO.getOpen() ? "open" : "close");
            return true;
        } else {
            log.error("Update merchant status unsuccessfull with status : {}", merchantDTO.getOpen() ? "open" : "close");
            return false;
        }
    }

    /**
     * Method untuk update merchant
     * @param merchantDTO
     * @return
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    @Override
    public Boolean updateMerchant(MerchantDTO merchantDTO) throws NullPointerException, IndexOutOfBoundsException {
        String merchantId = getMerchantIdByIndexAndPage(merchantDTO.getIndex(), merchantDTO.getPage());
        Optional<Merchant> merchantOptional = merchantRepository.findById(merchantId);

        if (merchantOptional.isPresent()) {
            Merchant merchant = merchantOptional.get();
            merchant.setMerchantName(merchantDTO.getMerchantName());
            merchant.setMerchantLocation(merchantDTO.getMerchantLocation());
            merchant.setOpen(merchantDTO.getOpen());

            merchantRepository.save(merchant);
            log.info("Update merchant successfull with name : {}", merchantDTO.getMerchantName());
            return true;
        } else {
            log.error("Update merchant unsuccessfull with name : {}", merchantDTO.getMerchantName());
            return false;
        }
    }

    /**
     * Method untuk delete merchant
     * @param merchantDTO
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    @Override
    public void deleteMerchant(MerchantDTO merchantDTO) throws NullPointerException, IndexOutOfBoundsException {
        String merchantId = getMerchantIdByIndexAndPage(merchantDTO.getIndex(), merchantDTO.getPage());
        merchantRepository.deleteById(merchantId);
        log.info("Delete merchant successfull with name : {}", merchantDTO.getMerchantName());
    }
}
