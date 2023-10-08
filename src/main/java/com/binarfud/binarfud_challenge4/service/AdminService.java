package com.binarfud.binarfud_challenge4.service;

import com.binarfud.binarfud_challenge4.dto.UserDTO;
import com.binarfud.binarfud_challenge4.entity.Admin;

public interface AdminService {
    Boolean checkAdminLogin(Admin admin);
    Boolean checkUsernameAvailability(UserDTO userDTO);
}
