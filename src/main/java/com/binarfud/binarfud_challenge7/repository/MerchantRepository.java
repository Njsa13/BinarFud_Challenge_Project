package com.binarfud.binarfud_challenge7.repository;

import com.binarfud.binarfud_challenge7.entity.Merchant;
import com.binarfud.binarfud_challenge7.enums.MerchantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {
    @Query("select case when count(m) > 0 then true else false end from Merchant m " +
            "join m.user u where m.merchantName = :merchantName and u.username = :username")
    boolean existsByMerchantNameAndUsername(String merchantName, String username);

    Merchant findByMerchantName(String merchantName);

    @Query("select m from Merchant m where m.merchantStatus = :merchantStatus")
    Page<Merchant> findByMerchantStatusWithPagination(Pageable pageable, MerchantStatus merchantStatus);
}
