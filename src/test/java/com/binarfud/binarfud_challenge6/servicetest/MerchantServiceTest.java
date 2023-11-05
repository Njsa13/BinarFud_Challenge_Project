package com.binarfud.binarfud_challenge6.servicetest;

import com.binarfud.binarfud_challenge6.dto.MerchantDTO;
import com.binarfud.binarfud_challenge6.dto.PaginationDTO;
import com.binarfud.binarfud_challenge6.entity.Merchant;
import com.binarfud.binarfud_challenge6.entity.User;
import com.binarfud.binarfud_challenge6.enums.ERole;
import com.binarfud.binarfud_challenge6.enums.MerchantStatus;
import com.binarfud.binarfud_challenge6.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge6.repository.MerchantRepository;
import com.binarfud.binarfud_challenge6.repository.UserRepository;
import com.binarfud.binarfud_challenge6.service.MerchantService;
import com.binarfud.binarfud_challenge6.service.MerchantServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MerchantServiceTest {
    @InjectMocks
    private MerchantServiceImpl merchantService;
    @Mock
    private MerchantRepository merchantRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void checkMerchantNameAvailabilityTest() {
        User user = User.builder()
                .username("UsernameTest")
                .password("UserPassword")
                .email("user@gmail.com")
                .build();
        Merchant merchant = Merchant.builder()
                .merchantName("TestMerchantName")
                .merchantLocation("TestMerchantLocation")
                .merchantStatus(MerchantStatus.CLOSED)
                .user(user)
                .build();
        Mockito.when(merchantRepository.existsByMerchantNameAndUsername(merchant.getMerchantName(), user.getUsername()))
                .thenReturn(true);
        MerchantDTO merchantDTO = MerchantDTO.builder()
                .merchantName("TestMerchantName")
                .merchantLocation("TestMerchantLocation")
                .merchantStatus("CLOSED")
                .username("UsernameTest")
                .build();
        Boolean result = merchantService.checkMerchantAvailability(merchantDTO);
        Mockito.verify(merchantRepository, Mockito.times(1))
                        .existsByMerchantNameAndUsername(merchant.getMerchantName(), user.getUsername());
        Assertions.assertTrue(result);
    }

    @Test
    void checkMerchantNameAvailabilityTest_DataNotAvailable() {
        Merchant merchant = Merchant.builder()
                .merchantName("TestMerchantName")
                .merchantLocation("TestMerchantLocation")
                .merchantStatus(MerchantStatus.CLOSED)
                .build();
        MerchantDTO merchantDTO = MerchantDTO.builder()
                .merchantName("TestMerchantName1")
                .merchantLocation("TestMerchantLocation2")
                .merchantStatus("CLOSED")
                .username("UsernameTest")
                .build();
        Mockito.when(merchantRepository.existsByMerchantNameAndUsername(merchant.getMerchantName(), merchantDTO.getUsername()))
                .thenReturn(false);
        Boolean result = merchantService.checkMerchantAvailability(merchantDTO);
        Assertions.assertFalse(result);
    }

    @Test
    void checkMerchantNameAvailabilityTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.checkMerchantAvailability(null));
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
        Page<Merchant> merchantPage = new PageImpl<>(merchants, PageRequest.of(0, 5), 1);
        Mockito.when(merchantRepository.findAll(PageRequest.of(0, 5))).thenReturn(merchantPage);
        PaginationDTO<MerchantDTO> merchantDTOPaginationDTO = merchantService.getAllMerchantWithPagination(1);
        Mockito.verify(merchantRepository, Mockito.times(2)).findAll(PageRequest.of(0, 5));
        Assertions.assertEquals(2, merchantDTOPaginationDTO.getData().size());
        Assertions.assertEquals(1, merchantDTOPaginationDTO.getCurrentPage());
        Assertions.assertEquals(1, merchantDTOPaginationDTO.getTotalPages());
    }

    @Test
    void getAllMerchantWithPaginationTest_throwDataNotFoundException() {
        Mockito.when(merchantRepository.findAll(PageRequest.of(0, 5))).thenReturn(Page.empty());
        Assertions.assertThrows(DataNotFoundException.class, () -> merchantService.getAllMerchantWithPagination(1));
    }

    @Test
    void getAllMerchantWithPaginationTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.getAllMerchantWithPagination(0));
    }

    @Test
    void getAllMerchantByMerchantStatusWithPaginationTest() {
        List<Merchant> merchants = Collections.singletonList(
                Merchant.builder()
                        .merchantName("TestMerchantName1")
                        .merchantLocation("TestMerchantLocation1")
                        .merchantStatus(MerchantStatus.OPEN)
                        .build()
        );
        Page<Merchant> merchantPage = new PageImpl<>(merchants, PageRequest.of(0, 5), 1);
        Mockito.when(merchantRepository.findByMerchantStatusWithPagination(PageRequest.of(0, 5), MerchantStatus.OPEN))
                .thenReturn(merchantPage);
        PaginationDTO<MerchantDTO> merchantDTOPaginationDTO = merchantService.getAllMerchantByMerchantStatusWithPagination(1);
        Mockito.verify(merchantRepository, Mockito.times(2))
                .findByMerchantStatusWithPagination(PageRequest.of(0, 5), MerchantStatus.OPEN);
        Assertions.assertEquals(1, merchantDTOPaginationDTO.getData().size());
        Assertions.assertEquals(1, merchantDTOPaginationDTO.getCurrentPage());
        Assertions.assertEquals(1, merchantDTOPaginationDTO.getTotalPages());
    }

    @Test
    void getAllMerchantByMerchantStatusWithPaginationTest_throwDataNotFoundException() {
        Mockito.when(merchantRepository.findByMerchantStatusWithPagination(PageRequest.of(0, 5), MerchantStatus.OPEN))
                .thenReturn(Page.empty());
        Assertions.assertThrows(DataNotFoundException.class, () -> merchantService.getAllMerchantByMerchantStatusWithPagination(1));
    }

    @Test
    void getAllMerchantByMerchantStatusWithPaginationTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.getAllMerchantByMerchantStatusWithPagination(0));
    }

    @Test
    void addMerchantTest() {
        User user = User.builder()
                .username("UsernameTest")
                .password("UserPassword")
                .email("user@gmail.com")
                .build();
        MerchantDTO merchantDTO = MerchantDTO.builder()
                .merchantName("TestMerchantName")
                .merchantLocation("TestMerchantLocation")
                .merchantStatus("CLOSED")
                .username("UsernameTest")
                .build();
        Mockito.when(userRepository.findByUsernameAndRoleName(user.getUsername(), ERole.ROLE_MERCHANT))
                .thenReturn(Optional.of(user));
        Mockito.when(merchantRepository.save(Mockito.any(Merchant.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        merchantService.addMerchant(merchantDTO);
        Mockito.verify(userRepository, Mockito.times(1))
                .findByUsernameAndRoleName(user.getUsername(), ERole.ROLE_MERCHANT);
        Mockito.verify(merchantRepository, Mockito.times(1)).save(Mockito.any(Merchant.class));
        Assertions.assertDoesNotThrow(() -> merchantService.addMerchant(merchantDTO));
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
        String merchantName = "TestMerchantName";
        MerchantDTO newMerchant = MerchantDTO.builder()
                .merchantName("NewTestMerchantName")
                .merchantLocation("NewTestMerchantLocation")
                .merchantStatus("OPEN")
                .build();
        Mockito.when(merchantRepository.findByMerchantName(merchantName)).thenReturn(merchant);
        Mockito.when(merchantRepository.save(Mockito.any(Merchant.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        merchantService.updateMerchant(newMerchant, merchantName);
        Mockito.verify(merchantRepository, Mockito.times(1)).findByMerchantName(merchantName);
        Mockito.verify(merchantRepository, Mockito.times(1)).save(Mockito.any(Merchant.class));
        Assertions.assertDoesNotThrow(() -> merchantService.updateMerchant(newMerchant, merchantName));
    }

    @Test
    void updateMerchantTest_throwDataNotFoundException() {
        MerchantDTO merchantDTO = MerchantDTO.builder()
                .merchantName("NewTestMerchantName")
                .merchantLocation("NewTestMerchantLocation")
                .merchantStatus("OPEN")
                .build();
        Mockito.when(merchantRepository.findByMerchantName(merchantDTO.getMerchantName())).thenReturn(null);
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
        MerchantDTO merchantDTO = MerchantDTO.builder()
                .merchantName("NewTestMerchantName")
                .merchantLocation("NewTestMerchantLocation")
                .merchantStatus(null)
                .build();
        Mockito.when(merchantRepository.findByMerchantName("TestMerchantName")).thenReturn(merchant);
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.updateMerchant(merchantDTO, "TestMerchantName"));
    }

    @Test
    void updateMerchantStatus() {
        Merchant merchant = Merchant.builder()
                .merchantName("TestMerchantName")
                .merchantStatus(MerchantStatus.CLOSED)
                .build();
        String merchantName = "TestMerchantName";
        Mockito.when(merchantRepository.findByMerchantName(merchantName)).thenReturn(merchant);
        Mockito.when(merchantRepository.save(Mockito.any(Merchant.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        merchantService.updateMerchantStatus(merchantName, "OPEN");
        Mockito.verify(merchantRepository, Mockito.times(1)).findByMerchantName(merchantName);
        Mockito.verify(merchantRepository, Mockito.times(1)).save(Mockito.any(Merchant.class));
        Assertions.assertDoesNotThrow(() -> merchantService.updateMerchantStatus(merchantName, "OPEN"));

    }

    @Test
    void updateMerchantStatus_throwDataNotFoundException() {
        Mockito.when(merchantRepository.findByMerchantName("TestMerchantName")).thenReturn(null);
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
        Mockito.when(merchantRepository.findByMerchantName("TestMerchantName")).thenReturn(merchant);
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.updateMerchantStatus("TestMerchantName", "null"));
    }

    @Test
    void deleteMerchantTest() {
        Merchant merchant = Merchant.builder()
                .merchantName("TestMerchantName")
                .merchantId("1")
                .merchantStatus(MerchantStatus.CLOSED)
                .build();
        String merchantName = "TestMerchantName";
        Mockito.when(merchantRepository.findByMerchantName(merchantName)).thenReturn(merchant);
        merchantService.deleteMerchant(merchant.getMerchantName());
        Mockito.verify(merchantRepository, Mockito.times(1)).findByMerchantName(merchantName);
        Mockito.verify(merchantRepository, Mockito.times(1)).deleteById(merchant.getMerchantId());
    }

    @Test
    void deleteMerchantTest_throwDataNotFoundException() {
        Mockito.when(merchantRepository.findByMerchantName("TestMerchantName")).thenReturn(null);
        Assertions.assertThrows(DataNotFoundException.class, () -> merchantService.deleteMerchant("WrongMerchantName"));
    }

    @Test
    void deleteMerchantTest_throwIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> merchantService.deleteMerchant(null));
    }
}
