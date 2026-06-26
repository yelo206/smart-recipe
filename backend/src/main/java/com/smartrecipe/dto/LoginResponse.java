package com.smartrecipe.dto;

import lombok.Data;

/**
 * 登录响应DTO
 */
@Data
public class LoginResponse {
    private String token;
    private Long userId;
    private String username;
    private String nickname;
    private String avatar;
}
