package com.binarfud.binarfud_challenge7.repository;

import com.binarfud.binarfud_challenge7.entity.User;
import com.binarfud.binarfud_challenge7.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("select u from User u join u.roles r where u.username = :username and r.roleName = :role")
    Optional<User> findByUsernameAndRoleName(String username, ERole role);
}
