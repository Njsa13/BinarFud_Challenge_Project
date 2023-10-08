package com.binarfud.binarfud_challenge4.repository;

import com.binarfud.binarfud_challenge4.entity.Merchant;
import com.binarfud.binarfud_challenge4.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("select p from Product p right join p.merchant m where m.merchantName = :merchantName and p.productName = :productName")
    List<Product> getProductsByMerchantNameAndProductName(String merchantName, String productName);
}
