package com.binarfud.binarfud_challenge5.repository;

import com.binarfud.binarfud_challenge5.entity.Merchant;
import com.binarfud.binarfud_challenge5.enums.MerchantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {
    boolean existsByMerchantName(String merchantName);

    Merchant findByMerchantName(String merchantName);

    @Query("select m from Merchant m where m.merchantStatus = :merchantStatus")
    Page<Merchant> findByMerchantStatusWithPagination(Pageable pageable, MerchantStatus merchantStatus);
}
