package com.binarfud.binarfud_challenge7.controller;

import com.binarfud.binarfud_challenge7.dto.PaginationDTO;
import com.binarfud.binarfud_challenge7.dto.UserDTO;
import com.binarfud.binarfud_challenge7.dto.request.SignupRequest;
import com.binarfud.binarfud_challenge7.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge7.dto.response.ErrorResponse;
import com.binarfud.binarfud_challenge7.dto.response.Response;
import com.binarfud.binarfud_challenge7.service.UserService;
import com.binarfud.binarfud_challenge7.utils.AuthExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthExtractor authExtractor;

    /**
     * Method untuk get semua user
     * @param page
     * @return
     */
    @GetMapping(value = "/get", produces = "application/json")
    public ResponseEntity<Response<PaginationDTO<UserDTO>>> getUser(@RequestParam("page") Integer page) {
        try {
            return ResponseEntity.ok(new Response<>(userService.getAllUserWithPagination(page), true, null));
        } catch (DataNotFoundException e) {
            log.error("Getting User with pagination unsuccessful with current page = {}", page);
            return new ResponseEntity<>(new Response<>(null, false, ErrorResponse.builder()
                    .errorMessage(e.getMessage())
                    .errorCode(HttpStatus.NOT_FOUND.value())
                    .build()), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            log.error("Getting User with pagination unsuccessful with current page = {}", page);
            return new ResponseEntity<>(new Response<>(null, false, ErrorResponse.builder()
                    .errorMessage(e.getMessage())
                    .errorCode(HttpStatus.BAD_REQUEST.value())
                    .build()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Method untuk update user
     * @param userDTO
     * @param request
     * @return
     */
    @PutMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<String> updateUser(@RequestBody UserDTO userDTO,
                                             HttpServletRequest request) {
        try {
            String username = authExtractor.extractorUsernameFromHeaderCookie(request);
            SignupRequest signupRequest = SignupRequest.builder()
                    .username(userDTO.getUsername())
                    .password(userDTO.getPassword())
                    .email(userDTO.getEmail())
                    .build();
            if (!(userService.checkUsernameAvailability(signupRequest) &&
                    userService.checkUsernameAvailability(SignupRequest.builder()
                            .username(username)
                            .build()) &&
                    !userDTO.getUsername().equals(username))) {
                log.info("Username not available with username = {}", userDTO.getUsername());
                userService.updateUser(userDTO, username);
                return ResponseEntity.ok("Update Successful");
            } else {
                log.info("Username available with username = {}", userDTO.getUsername());
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Username is already in use");
            }
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    /**
     * Method untuk hapus user
     * @param request
     * @return
     */
    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        try {
            String username = authExtractor.extractorUsernameFromHeaderCookie(request);
            userService.deleteUser(username);
            return ResponseEntity.ok("Delete Successful");
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}
