package com.binarfud.binarfud_challenge7.repository;

import com.binarfud.binarfud_challenge7.entity.Product;
import com.binarfud.binarfud_challenge7.enums.MerchantStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("select case when count(p) > 0 then true else false end from Product p " +
            "join p.merchant m where p.productName = :productName and m.merchantName = :merchantName")
    boolean existsByProductNameAndMerchantName(String productName, String merchantName);

    @Query("select p from Product p join p.merchant m where m.merchantStatus = :merchantStatus")
    Page<Product> findByMerchantStatusWithPagination(Pageable pageable, MerchantStatus merchantStatus);

    @Query("select p from Product p join p.merchant m " +
            "where p.productName = :productName and m.merchantName = :merchantName")
    Product findByProductNameAndMerchantName(String productName, String merchantName);
}
