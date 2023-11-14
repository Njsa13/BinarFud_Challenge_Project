package com.binarfud.binarfud_challenge7.repository;

import com.binarfud.binarfud_challenge7.entity.Roles;
import com.binarfud.binarfud_challenge7.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByRoleName(ERole roleName);
}
