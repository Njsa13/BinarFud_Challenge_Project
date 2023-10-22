package com.binarfud.binarfud_challenge5.service;

import com.binarfud.binarfud_challenge5.dto.PaginationDTO;
import com.binarfud.binarfud_challenge5.dto.UserDTO;
import com.binarfud.binarfud_challenge5.entity.User;

import java.util.Optional;

public interface UserService {
    Boolean checkUserLogin(UserDTO userDTO);
    Boolean checkUsernameAvailability(UserDTO userDTO);
    Boolean validateUserWithRegex(UserDTO userDTO);
    User convertUserDTOToUser(UserDTO userDTO);
    PaginationDTO<UserDTO> getAllUserWithPagination(Integer page);
    void addUser(UserDTO userDTO);
    void updateUser(UserDTO userDTO, String oldUsername);
    void deleteUser(String username);
}
