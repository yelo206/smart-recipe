-- 智能菜谱管理平台 数据库建表脚本（H2 兼容 MySQL 模式）

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20),
    avatar VARCHAR(255),
    gender TINYINT DEFAULT 0,
    dietary_preference VARCHAR(255),
    daily_calorie_goal INT DEFAULT 2000,
    deleted TINYINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 菜谱分类表
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    sort_order INT DEFAULT 0,
    deleted TINYINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 菜谱表
CREATE TABLE IF NOT EXISTS recipes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    cover_image VARCHAR(255),
    difficulty TINYINT DEFAULT 1,
    cook_time INT DEFAULT 30,
    servings INT DEFAULT 2,
    calories INT DEFAULT 0,
    protein DECIMAL(8,2) DEFAULT 0,
    fat DECIMAL(8,2) DEFAULT 0,
    carbohydrate DECIMAL(8,2) DEFAULT 0,
    deleted TINYINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 食材表
CREATE TABLE IF NOT EXISTS ingredients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    unit VARCHAR(20),
    calories_per_unit DECIMAL(8,2) DEFAULT 0,
    protein_per_unit DECIMAL(8,2) DEFAULT 0,
    fat_per_unit DECIMAL(8,2) DEFAULT 0,
    carb_per_unit DECIMAL(8,2) DEFAULT 0,
    deleted TINYINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 菜谱-食材关联表
CREATE TABLE IF NOT EXISTS recipe_ingredients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recipe_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    amount DECIMAL(8,2) NOT NULL,
    unit VARCHAR(20)
);

-- 菜谱步骤表
CREATE TABLE IF NOT EXISTS recipe_steps (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recipe_id BIGINT NOT NULL,
    step_number INT NOT NULL,
    description TEXT NOT NULL,
    image_url VARCHAR(255)
);

-- 膳食计划表
CREATE TABLE IF NOT EXISTS meal_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    plan_date DATE NOT NULL,
    meal_type TINYINT NOT NULL,
    note VARCHAR(255),
    completed TINYINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 购物清单表
CREATE TABLE IF NOT EXISTS shopping_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    ingredient_id BIGINT,
    name VARCHAR(100) NOT NULL,
    amount DECIMAL(8,2),
    unit VARCHAR(20),
    purchased TINYINT DEFAULT 0,
    deleted TINYINT DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 营养记录表
CREATE TABLE IF NOT EXISTS nutrition_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    log_date DATE NOT NULL,
    total_calories INT DEFAULT 0,
    total_protein DECIMAL(8,2) DEFAULT 0,
    total_fat DECIMAL(8,2) DEFAULT 0,
    total_carb DECIMAL(8,2) DEFAULT 0,
    note VARCHAR(255),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
