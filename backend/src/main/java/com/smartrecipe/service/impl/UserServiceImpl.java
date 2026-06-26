package com.smartrecipe.service.impl;

import com.smartrecipe.dto.LoginDTO;
import com.smartrecipe.dto.LoginResponse;
import com.smartrecipe.dto.RegisterDTO;
import com.smartrecipe.entity.User;
import com.smartrecipe.exception.BusinessException;
import com.smartrecipe.mapper.UserMapper;
import com.smartrecipe.service.UserService;
import com.smartrecipe.utils.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public LoginResponse login(LoginDTO loginDTO) {
        // 根据用户名查询用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, loginDTO.getUsername())
        );

        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "密码错误");
        }

        // 生成JWT令牌
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());

        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        response.setAvatar(user.getAvatar());
        return response;
    }

    @Override
    public void register(RegisterDTO registerDTO) {
        // 检查用户名是否已存在
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, registerDTO.getUsername())
        );

        if (count > 0) {
            throw new BusinessException(400, "用户名已存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setGender(registerDTO.getGender() != null ? registerDTO.getGender() : 0);
        user.setDailyCalorieGoal(2000);
        userMapper.insert(user);
    }

    @Override
    public User getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        user.setPassword(null); // 清除密码
        return user;
    }

    @Override
    public User updateUser(User user) {
        User existing = userMapper.selectById(user.getId());
        if (existing == null) {
            throw new BusinessException(404, "用户不存在");
        }
        // 不允许修改密码和用户名
        user.setPassword(null);
        user.setUsername(existing.getUsername());
        userMapper.updateById(user);
        return getUserById(user.getId());
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(400, "原密码错误");
        }
        User update = new User();
        update.setId(userId);
        update.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(update);
    }
}
