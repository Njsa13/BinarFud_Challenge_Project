package com.binarfud.binarfud_challenge4.repository;

import com.binarfud.binarfud_challenge4.entity.Admin;
import com.binarfud.binarfud_challenge4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    Admin findByUsernameAndPassword(String username, String password);
    Admin findByUsername(String username);
}
