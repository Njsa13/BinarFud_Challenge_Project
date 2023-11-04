package com.binarfud.binarfud_challenge6.service;

import com.binarfud.binarfud_challenge6.dto.PaginationDTO;
import com.binarfud.binarfud_challenge6.dto.UserDTO;
import com.binarfud.binarfud_challenge6.dto.request.SignupRequest;
import com.binarfud.binarfud_challenge6.entity.User;

public interface UserService {
    Boolean checkUsernameAvailability(SignupRequest signupRequest);
    Boolean checkEmailAvailability(SignupRequest signupRequest);
    User convertUserDTOToUser(SignupRequest signupRequest);
    PaginationDTO<UserDTO> getAllUserWithPagination(Integer page);
    void addUser(SignupRequest signupRequest);
    void updateUser(UserDTO userDTO, String oldUsername);
    void deleteUser(String username);
}
