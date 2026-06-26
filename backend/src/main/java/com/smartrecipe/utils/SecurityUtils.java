package com.smartrecipe.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 安全工具类
 * 获取当前登录用户信息
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户名
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return null;
    }

    /**
     * 获取当前登录用户ID
     * 注意：用户ID存储在JWT的subject中，这里从用户名解析
     * 实际使用时从authentication的credentials中获取
     */
    public static Long getCurrentUserId() {
        // 在JwtAuthenticationFilter中，subject设置为userId
        // 但Spring Security的UserDetails中username是用户名
        // 这里通过name获取（在filter中设置了userId作为name的一部分）
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                // 从token中解析的username实际是用户名，需要通过其他方式获取userId
                // 这里返回null，由controller层通过service获取
                return null;
            }
        }
        return null;
    }
}
