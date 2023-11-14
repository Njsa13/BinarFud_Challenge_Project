package com.binarfud.binarfud_challenge7.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthExtractor {

    @Autowired
    private JwtUtils jwtTokenUtil;

    public String extractorUsernameFromHeaderCookie(HttpServletRequest request){
        String token = jwtTokenUtil.extractToken(request);
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token tidak ditemukan");
        }
        return jwtTokenUtil.getUsernameFromJwtToken(token);
    }
}
