package com.binarfud.binarfud_challenge6.repository;

import com.binarfud.binarfud_challenge6.entity.Roles;
import com.binarfud.binarfud_challenge6.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByRoleName(ERole roleName);
}
