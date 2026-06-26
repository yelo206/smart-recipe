package com.smartrecipe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 智能菜谱管理平台 - 启动类
 */
@SpringBootApplication
@MapperScan("com.smartrecipe.mapper")
public class SmartRecipeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartRecipeApplication.class, args);
    }
}
