package com.binarfud.binarfud_challenge5.repository;

import com.binarfud.binarfud_challenge5.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    boolean existsByUsernameAndPassword(String username, String password);

    User findByUsername(String username);
}
