package com.binarfud.binarfud_challenge5.servicetest;

import com.binarfud.binarfud_challenge5.dto.MerchantDTO;
import com.binarfud.binarfud_challenge5.dto.PaginationDTO;
import com.binarfud.binarfud_challenge5.entity.Merchant;
import com.binarfud.binarfud_challenge5.enums.MerchantStatus;
import com.binarfud.binarfud_challenge5.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge5.repository.MerchantRepository;
import com.binarfud.binarfud_challenge5.service.MerchantService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class MerchantServiceTest {
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private MerchantRepository merchantRepository;

    @AfterEach
    void tearDown() {
        merchantRepository.deleteAll();
    }

    @Test
    void checkMerchantNameAvailabilityTest() {
        Merchant merchant = Merchant.builder()
                .merchantName("TestMerchantName")
                .merchantLocation("TestMerchantLocation")
                .merchantStatus(MerchantStatus.CLOSED)
                .build();
        merchantRepository.save(merchant);
        MerchantDTO merchantDTO = MerchantDTO.builder()
                .merchantName("TestMerchantName")
                .merchantLocation("TestMerchantLocation")
                .merchantStatus("CLOSED")
                .build();
        Boolean result = merchantService.checkMerchantNameAvailability(merchantDTO);
        Assertions.assertTrue(result);
    }

    @Test
    void checkMerchantNameAvailabilityTest_DataNotAvailable() {
        Merchant merchant = Merchant.builder()
                .merchantName("TestMerchantName")
                .merchantLocation("TestMerchantLocation")
                .merchantStatus(MerchantStatus.CLOSED)
                .build();
        merchantRepository.save(merchant);
        MerchantDTO merchantDTO = MerchantDTO.builder()
                .merchantName("TestMerchantName1")
                .merchantLocation("TestMerchantLocation2")
                .merchantStatus("CLOSED")
                .build();
        Boolean result = merchantService.checkMerchantNameAvailability(merchantDTO);
        Assertions.assertFalse(result);
    }

    @Test
    void checkMerchantNameAvailabilityTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.checkMerchantNameAvailability(null));
    }

    @Test
    void convertMerchantDTOToMerchantTest_throwIllegalArgumentException() {
        MerchantDTO merchantDTO = MerchantDTO.builder()
                .merchantName("TestMerchantName1")
                .merchantLocation("TestMerchantLocation2")
                .merchantStatus("null")
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.convertMerchantDTOToMerchant(merchantDTO));
    }

    @Test
    void getAllMerchantWithPaginationTest() {
        List<Merchant> merchants = Arrays.asList(
                Merchant.builder()
                        .merchantName("TestMerchantName1")
                        .merchantLocation("TestMerchantLocation1")
                        .merchantStatus(MerchantStatus.OPEN)
                        .build(),
                Merchant.builder()
                        .merchantName("TestMerchantName2")
                        .merchantLocation("TestMerchantLocation2")
                        .merchantStatus(MerchantStatus.CLOSED)
                        .build()
        );
        merchantRepository.saveAll(merchants);
        PaginationDTO<MerchantDTO> merchantDTOPaginationDTO = merchantService.getAllMerchantWithPagination(1);
        Assertions.assertEquals(2, merchantDTOPaginationDTO.getData().size());
        Assertions.assertEquals(1, merchantDTOPaginationDTO.getCurrentPage());
        Assertions.assertEquals(1, merchantDTOPaginationDTO.getTotalPages());
    }

    @Test
    void getAllMerchantWithPaginationTest_throwDataNotFoundException() {
        Assertions.assertThrows(DataNotFoundException.class, () -> merchantService.getAllMerchantWithPagination(1));
    }

    @Test
    void getAllMerchantWithPaginationTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.getAllMerchantWithPagination(0));
    }

    @Test
    void getAllMerchantByMerchantStatusWithPaginationTest() {
        List<Merchant> merchants = Arrays.asList(
                Merchant.builder()
                        .merchantName("TestMerchantName1")
                        .merchantLocation("TestMerchantLocation1")
                        .merchantStatus(MerchantStatus.OPEN)
                        .build(),
                Merchant.builder()
                        .merchantName("TestMerchantName2")
                        .merchantLocation("TestMerchantLocation2")
                        .merchantStatus(MerchantStatus.CLOSED)
                        .build()
        );
        merchantRepository.saveAll(merchants);
        PaginationDTO<MerchantDTO> merchantDTOPaginationDTO = merchantService.getAllMerchantByMerchantStatusWithPagination(1);
        Assertions.assertEquals(1, merchantDTOPaginationDTO.getData().size());
        Assertions.assertEquals(1, merchantDTOPaginationDTO.getCurrentPage());
        Assertions.assertEquals(1, merchantDTOPaginationDTO.getTotalPages());
    }

    @Test
    void getAllMerchantByMerchantStatusWithPaginationTest_throwDataNotFoundException() {
        Assertions.assertThrows(DataNotFoundException.class, () -> merchantService.getAllMerchantByMerchantStatusWithPagination(1));
    }

    @Test
    void getAllMerchantByMerchantStatusWithPaginationTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.getAllMerchantByMerchantStatusWithPagination(0));
    }

    @Test
    void addMerchantTest() {
        MerchantDTO merchantDTO = MerchantDTO.builder()
                .merchantName("TestMerchantName")
                .merchantLocation("TestMerchantLocation")
                .merchantStatus("CLOSED")
                .build();
        merchantService.addMerchant(merchantDTO);
        Merchant merchant = merchantRepository.findByMerchantName(merchantDTO.getMerchantName());
        Assertions.assertEquals("TestMerchantName", merchant.getMerchantName());
        Assertions.assertEquals("TestMerchantLocation", merchant.getMerchantLocation());
        Assertions.assertEquals(MerchantStatus.CLOSED, merchant.getMerchantStatus());
    }

    @Test
    void addMerchantTest_throwIllegalArgumentException1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.addMerchant(null));
    }

    @Test
    void addMerchantTest_throwIllegalArgumentException2() {
        MerchantDTO merchantDTO = MerchantDTO.builder()
                .merchantName("TestMerchantName")
                .merchantLocation(null)
                .merchantStatus(null)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.addMerchant(merchantDTO));
    }

    @Test
    void updateMerchantTest() {
        Merchant merchant = Merchant.builder()
                .merchantName("TestMerchantName")
                .merchantLocation("TestMerchantLocation")
                .merchantStatus(MerchantStatus.CLOSED)
                .build();
        merchantRepository.save(merchant);
        MerchantDTO newMerchant = MerchantDTO.builder()
                .merchantName("NewTestMerchantName")
                .merchantLocation("NewTestMerchantLocation")
                .merchantStatus("OPEN")
                .build();
        merchantService.updateMerchant(newMerchant, merchant.getMerchantName());
        Merchant merchantCheck = merchantRepository.findByMerchantName(newMerchant.getMerchantName());
        Assertions.assertEquals("NewTestMerchantName", merchantCheck.getMerchantName());
        Assertions.assertEquals("NewTestMerchantLocation", merchantCheck.getMerchantLocation());
        Assertions.assertEquals(MerchantStatus.OPEN, merchantCheck.getMerchantStatus());
    }

    @Test
    void updateMerchantTest_throwDataNotFoundException() {
        MerchantDTO merchantDTO = MerchantDTO.builder()
                .merchantName("NewTestMerchantName")
                .merchantLocation("NewTestMerchantLocation")
                .merchantStatus("OPEN")
                .build();
        Assertions.assertThrows(DataNotFoundException.class, () -> merchantService.updateMerchant(merchantDTO, "WrongMerchantName"));
    }

    @Test
    void updateMerchantTest_throwIllegalArgumentException1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.updateMerchant(null, null));
    }

    @Test
    void updateMerchantTest_throwIllegalArgumentException2() {
        Merchant merchant = Merchant.builder()
                .merchantName("TestMerchantName")
                .merchantLocation("TestMerchantLocation")
                .merchantStatus(MerchantStatus.CLOSED)
                .build();
        merchantRepository.save(merchant);
        MerchantDTO merchantDTO = MerchantDTO.builder()
                .merchantName("NewTestMerchantName")
                .merchantLocation("NewTestMerchantLocation")
                .merchantStatus(null)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.updateMerchant(merchantDTO, "TestMerchantName"));
    }

    @Test
    void updateMerchantStatus() {
        Merchant merchant = Merchant.builder()
                .merchantName("TestMerchantName")
                .merchantStatus(MerchantStatus.CLOSED)
                .build();
        merchantRepository.save(merchant);
        merchantService.updateMerchantStatus("TestMerchantName", "OPEN");
        Merchant merchantCheck = merchantRepository.findByMerchantName(merchant.getMerchantName());
        Assertions.assertEquals(MerchantStatus.OPEN, merchantCheck.getMerchantStatus());
    }

    @Test
    void updateMerchantStatus_throwDataNotFoundException() {
        Assertions.assertThrows(DataNotFoundException.class, () -> merchantService
                .updateMerchantStatus("TestMerchantName", "OPEN"));
    }

    @Test
    void updateMerchantStatus_throwIllegalArgumentException1() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.updateMerchantStatus(null, null));
    }

    @Test
    void updateMerchantStatus_throwIllegalArgumentException2() {
        Merchant merchant = Merchant.builder()
                .merchantName("TestMerchantName")
                .merchantStatus(MerchantStatus.CLOSED)
                .build();
        merchantRepository.save(merchant);
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.updateMerchantStatus("TestMerchantName", "null"));
    }

    @Test
    void deleteMerchantTest() {
        Merchant merchant = Merchant.builder()
                .merchantName("TestMerchantName")
                .merchantStatus(MerchantStatus.CLOSED)
                .build();
        merchantRepository.save(merchant);
        merchantService.deleteMerchant(merchant.getMerchantName());
        Assertions.assertNull(merchantRepository.findByMerchantName("TestMerchantName"));
    }

    @Test
    void deleteMerchantTest_throwDataNotFoundException() {
        Assertions.assertThrows(DataNotFoundException.class, () -> merchantService.deleteMerchant("WrongMerchantName"));
    }

    @Test
    void deleteMerchantTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.deleteMerchant(null));
    }
}
