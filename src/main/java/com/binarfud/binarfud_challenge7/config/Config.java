package com.binarfud.binarfud_challenge7.config;

import com.binarfud.binarfud_challenge7.entity.Roles;
import com.binarfud.binarfud_challenge7.enums.ERole;
import com.binarfud.binarfud_challenge7.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

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
