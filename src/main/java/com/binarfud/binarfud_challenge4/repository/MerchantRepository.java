package com.binarfud.binarfud_challenge4.repository;

import com.binarfud.binarfud_challenge4.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, String> {
    Merchant findByMerchantName(String merchantName);
}
