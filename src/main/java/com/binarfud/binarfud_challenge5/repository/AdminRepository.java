package com.binarfud.binarfud_challenge5.repository;

import com.binarfud.binarfud_challenge5.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    boolean existsByUsernameAndPassword(String username, String password);
}
