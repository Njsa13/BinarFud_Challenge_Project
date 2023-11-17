package com.binarfud.binarfud_challenge6.service;

import com.binarfud.binarfud_challenge6.entity.User;
import com.binarfud.binarfud_challenge6.entity.UserDetailsImpl;
import com.binarfud.binarfud_challenge6.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username " + username));
        return UserDetailsImpl.build(user);
    }
}
