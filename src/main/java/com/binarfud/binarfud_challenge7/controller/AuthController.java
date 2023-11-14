package com.binarfud.binarfud_challenge7.controller;

import com.binarfud.binarfud_challenge7.utils.JwtUtils;
import com.binarfud.binarfud_challenge7.dto.request.LoginRequest;
import com.binarfud.binarfud_challenge7.dto.request.SignupRequest;
import com.binarfud.binarfud_challenge7.dto.response.JwtResponse;
import com.binarfud.binarfud_challenge7.dto.response.MessageResponse;
import com.binarfud.binarfud_challenge7.entity.UserDetailsImpl;
import com.binarfud.binarfud_challenge7.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge7.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;

    @Value("${jwt.expiration.ms}")
    private int jwtExpirationMs;

    public AuthController(AuthenticationManager authenticationManager, UserService userService,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest login,
                                                        HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        Cookie cookie = new Cookie("token", jwt);
        cookie.setMaxAge((int) TimeUnit.MILLISECONDS.toSeconds(jwtExpirationMs));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUserId(), userDetails.getUsername(),
                userDetails.getEmail(), roles));
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            Boolean usernameExist = userService.checkUsernameAvailability(signupRequest);
            if(Boolean.TRUE.equals(usernameExist)) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Error: Username is already taken!"));
            }
            if (signupRequest.getRole().contains("ROLE_ADMIN")) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Error: Can't register as admin!"));
            }
            Boolean emailExist = userService.checkEmailAvailability(signupRequest);
            if(Boolean.TRUE.equals(emailExist)) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Error: Email is already taken!"));
            }
            userService.addUser(signupRequest);
            return ResponseEntity.ok(new MessageResponse("User registered successfully"));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse(e.getMessage()));
        }
    }
}
