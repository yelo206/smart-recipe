# 智能菜谱管理平台（Smart Recipe Management Platform）

## 项目简介

智能菜谱管理平台是一个面向个人用户的生活辅助类Web应用，提供菜谱管理、膳食计划安排、购物清单生成和营养统计等功能，帮助用户实现科学饮食和健康管理。

## 技术栈

- **后端**：Spring Boot 3.2.5 + MyBatis-Plus 3.5.6 + Spring Security + JWT
- **前端**：Vue 3.4 + Element Plus 2.7 + Pinia + Vue Router + ECharts
- **数据库**：MySQL 8（生产）/ H2（开发）
- **构建工具**：Maven / Vite

## 快速开始

### 后端启动

```bash
cd backend
# 开发模式（使用H2内存数据库，无需安装MySQL）
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 生产模式（需配置MySQL）
# 修改 application-prod.yml 中的数据库连接信息
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

后端服务启动在 http://localhost:8080，接口前缀为 `/api`

> 若Maven依赖下载缓慢，可配置阿里云镜像：在 `~/.m2/settings.xml` 中添加阿里云仓库地址。

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端服务启动在 http://localhost:5173

> 若npm install出现权限问题，可使用 `npm install --cache /tmp/npm-cache` 指定临时缓存目录。

### 默认账号

- 用户名：admin
- 密码：123456

## 项目结构

```
smart-recipe/
├── backend/                 # 后端Spring Boot项目
│   ├── src/main/java/com/smartrecipe/
│   │   ├── config/          # 配置类
│   │   ├── controller/      # 控制器层
│   │   ├── service/         # 服务层
│   │   ├── mapper/          # 数据访问层
│   │   ├── entity/          # 实体类
│   │   ├── dto/             # 数据传输对象
│   │   ├── filter/          # 过滤器
│   │   ├── exception/       # 异常处理
│   │   └── utils/           # 工具类
│   └── src/main/resources/  # 配置文件和SQL脚本
├── frontend/                # 前端Vue项目
│   └── src/
│       ├── api/             # API接口模块
│       ├── router/          # 路由配置
│       ├── stores/          # Pinia状态管理
│       ├── views/           # 页面组件
│       ├── layout/          # 布局组件
│       └── utils/           # 工具函数
├── project-doc.md           # 项目文档（Markdown源文件）
├── diagrams/                # UML图（Mermaid源文件+PNG）
└── 智能菜谱管理平台_项目文档.docx  # 项目文档（Word格式）
```

## 功能模块

1. **用户认证** - 注册、登录、JWT认证、密码修改
2. **菜谱管理** - 菜谱增删改查、分类管理、食材管理
3. **食材找菜谱** - 按已有食材搜索匹配菜谱，按匹配数量排序
4. **膳食计划** - 周历视图、按日按餐次安排计划
5. **购物清单** - 手动添加、根据膳食计划自动生成
6. **营养统计** - 每日营养概览、7天趋势图表
7. **个人中心** - 资料管理、卡路里目标设置
