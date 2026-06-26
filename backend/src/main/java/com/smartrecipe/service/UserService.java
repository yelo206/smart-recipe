package com.smartrecipe.service;

import com.smartrecipe.dto.LoginDTO;
import com.smartrecipe.dto.LoginResponse;
import com.smartrecipe.dto.RegisterDTO;
import com.smartrecipe.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {
    LoginResponse login(LoginDTO loginDTO);
    void register(RegisterDTO registerDTO);
    User getUserById(Long id);
    User updateUser(User user);
    void changePassword(Long userId, String oldPassword, String newPassword);
}
