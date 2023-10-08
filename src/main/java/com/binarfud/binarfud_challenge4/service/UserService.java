package com.binarfud.binarfud_challenge4.service;

import com.binarfud.binarfud_challenge4.dto.UserDTO;
import com.binarfud.binarfud_challenge4.dto.UserPageDTO;
import com.binarfud.binarfud_challenge4.entity.User;

public interface UserService {
    Boolean checkUserLogin(UserDTO userDTO);
    Boolean checkUsernameAvailability(UserDTO userDTO);
    Boolean validateUserWithRegex(UserDTO userDTO);
    UserPageDTO getAllUser(Integer page);
    String getUserIdByIndexAndPage(Integer index, Integer page);
    Boolean addUserAccount(UserDTO userDTO);
    Boolean updateUser(UserDTO userDTO);
    User convertUserDTOToUser(UserDTO userDTO);
    void deleteUser(UserDTO userDTO);

}
