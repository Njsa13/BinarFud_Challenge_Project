package com.binarfud.binarfud_challenge6.config;

import com.binarfud.binarfud_challenge6.entity.Roles;
import com.binarfud.binarfud_challenge6.entity.User;
import com.binarfud.binarfud_challenge6.enums.ERole;
import com.binarfud.binarfud_challenge6.repository.RoleRepository;
import com.binarfud.binarfud_challenge6.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
public class Config {

    @Autowired
    private PasswordEncoder passwordEncoder;

    Config(RoleRepository roleRepository) {
        log.info("Checking roles presented");
        for(ERole c : ERole.values()) {
            try {
                Roles roles = roleRepository.findByRoleName(c)
                        .orElseThrow(() -> new RuntimeException("Roles not found"));
                log.info("Role {} has been found!", roles.getRoleName());
            } catch (RuntimeException rte) {
                log.info("Role {} is not found, inserting to DB . . .", c.name());
                Roles roles = new Roles();
                roles.setRoleName(c);
                roleRepository.save(roles);
            }
        }
    }
}
