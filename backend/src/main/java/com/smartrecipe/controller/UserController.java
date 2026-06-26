package com.smartrecipe.controller;

import com.smartrecipe.entity.User;
import com.smartrecipe.service.UserService;
import com.smartrecipe.utils.JwtUtils;
import com.smartrecipe.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 处理用户信息管理
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        return Result.success(userService.getUserById(userId));
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    public Result<User> updateUserInfo(@RequestBody User user, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        user.setId(userId);
        return Result.success("更新成功", userService.updateUser(user));
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Void> changePassword(@RequestParam String oldPassword,
                                       @RequestParam String newPassword,
                                       HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        userService.changePassword(userId, oldPassword, newPassword);
        return Result.success("密码修改成功", null);
    }

    /**
     * 从请求中获取用户ID
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtils.getUserIdFromToken(token);
        }
        throw new RuntimeException("未获取到用户信息");
    }
}
