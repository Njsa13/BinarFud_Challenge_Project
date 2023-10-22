package com.binarfud.binarfud_challenge5.controller;

import com.binarfud.binarfud_challenge5.dto.PaginationDTO;
import com.binarfud.binarfud_challenge5.dto.UserDTO;
import com.binarfud.binarfud_challenge5.exception.DataNotFoundException;
import com.binarfud.binarfud_challenge5.response.ErrorResponse;
import com.binarfud.binarfud_challenge5.response.Response;
import com.binarfud.binarfud_challenge5.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Method untuk login user
     * @param userDTO
     * @return
     */
    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<String> checkUserLogin(@RequestBody UserDTO userDTO) {
        try {
            if (userService.checkUserLogin(userDTO)) {
                log.info("User data available with username = {}", userDTO.getUsername());
                return ResponseEntity.ok("Login Successful");
            } else {
                log.info("User data not available with username = {}", userDTO.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Login Failed");
            }
        } catch (IllegalArgumentException e) {
            log.error("Failed to check username availability with username = {}",userDTO.getUsername());
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    /**
     * Method untuk register user
     * @param userDTO
     * @return
     */
    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<String> registration(@RequestBody UserDTO userDTO) {
        try {
            if (userService.validateUserWithRegex(userDTO)) {
                log.info("User data is valid with username = {}", userDTO.getUsername());
                if (!userService.checkUsernameAvailability(userDTO)) {
                    log.info("Username not available with username = {}", userDTO.getUsername());
                    userService.addUser(userDTO);
                    return ResponseEntity.ok("Registration Successful");
                } else {
                    log.info("Username available with username = {}", userDTO.getUsername());
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Username is already in use");
                }
            } else {
                log.info("User data is invalid with username = {}", userDTO.getUsername());
               return ResponseEntity.badRequest()
                       .body("Username or Password or Email is invalid");
            }
        }  catch (IllegalArgumentException e) {
            log.error("Failed to register with username = {}",userDTO.getUsername());
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

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
     * Methid untuk update user
     * @param userDTO
     * @param username
     * @return
     */
    @PutMapping(value = "/update/{username}", consumes = "application/json")
    public ResponseEntity<String> updateUser(@RequestBody UserDTO userDTO,
                                             @PathVariable("username") String username) {
        try {
            if (!(userService.checkUsernameAvailability(userDTO) &&
                    userService.checkUsernameAvailability(UserDTO.builder()
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
     * @param username
     * @return
     */
    @DeleteMapping(value = "/delete/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable("username") String username) {
        try {
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
