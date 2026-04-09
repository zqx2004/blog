```markdown
# Spring Boot 博客系统后端
<div align="center">
  <img src="https://img.shields.io/badge/SpringBoot-3.x-brightgreen" alt="SpringBoot">
  <img src="https://img.shields.io/badge/MyBatis--Plus-3.5.x-orange" alt="MyBatis-Plus">
  <img src="https://img.shields.io/badge/MySQL-8.x-blue" alt="MySQL">
  <img src="https://img.shields.io/badge/JWT-Authentication-red" alt="JWT">
  <img src="https://img.shields.io/badge/License-MIT-yellow" alt="License">
</div>

基于 **Spring Boot 3 + MyBatis-Plus** 开发的前后端分离博客后端系统，遵循 RESTful 设计规范，实现用户认证、文章管理、权限控制等核心功能，代码结构清晰、可扩展性强，适合作为 Java 后端学习项目与求职简历展示项目。

---

## ✨ 项目特性
- 🔐 **JWT 无状态鉴权**：自研 JWT 工具类，结合登录拦截器实现接口权限控制，支持分布式场景
- 📝 **完整文章管理**：文章发布、分页查询、修改、删除全流程，严格数据权限控制（仅作者可操作自身文章）
- 🎯 **统一异常处理**：封装全局异常处理器与统一响应结果，统一接口返回格式，提升系统健壮性
- 📚 **自动接口文档**：集成 Knife4j 自动生成可视化接口文档，支持在线调试，降低前后端联调成本
- 🧩 **分层架构设计**：标准 MVC 分层架构，代码低耦合、高内聚，符合企业级开发规范
- 📦 **开箱即用**：Maven 一键构建，环境配置简单，本地/服务器均可快速部署

---

## 🛠️ 技术栈
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.x | 后端核心框架，提供自动配置、依赖管理等能力 |
| MyBatis-Plus | 3.5.x | 持久层框架，简化 CRUD 操作，提升开发效率 |
| MySQL | 8.x | 关系型数据库，存储用户与文章数据 |
| JWT | - | 无状态身份认证，实现用户登录鉴权 |
| Knife4j | 4.x | 基于 OpenAPI 3 的接口文档生成工具 |
| Lombok | - | 简化 Java 代码，消除冗余的 getter/setter 等 |
| Maven | 3.8+ | 项目构建与依赖管理工具 |
| Git/GitHub | - | 版本控制与代码托管 |

---

## 📁 项目结构
```text
src/main/java/com/example/blog/
├── BlogApplication.java                  # 项目启动入口
├── common/                               # 通用工具与封装
│   ├── exception/
│   │   └── GlobalExceptionHandler.java   # 全局异常处理器
│   ├── result/
│   │   └── Result.java                   # 统一响应结果封装
│   └── util/
│       ├── JwtUtil.java                  # JWT 工具类（生成/解析 Token）
│       └── UserHolder.java               # 用户上下文工具（存储当前登录用户）
├── config/                               # 配置类
│   ├── LoginInterceptor.java             # 登录拦截器（权限校验）
│   └── WebConfig.java                    # Web 配置（注册拦截器、跨域等）
├── controller/                           # 接口控制器
│   ├── UserController.java               # 用户相关接口（注册/登录）
│   └── ArticleController.java            # 文章相关接口（CRUD）
├── entity/                               # 数据库实体类
│   ├── User.java                         # 用户实体
│   └── Article.java                      # 文章实体
├── mapper/                               # MyBatis-Plus Mapper 接口
│   ├── UserMapper.java
│   └── ArticleMapper.java
└── service/                              # 业务逻辑层
    ├── UserService.java                  # 用户服务接口
    ├── ArticleService.java               # 文章服务接口
    └── impl/
        ├── UserServiceImpl.java          # 用户服务实现
        └── ArticleServiceImpl.java       # 文章服务实现
└── resources/
    └── application.yml                   # 项目配置文件（数据库、端口等）

---

## 🚀 快速启动
### 环境要求
- JDK 17+
- Maven 3.8+
- MySQL 8.0+

### 启动步骤
1. **克隆项目**
```bash
git clone https://github.com/zqx2004/blog.git
cd blog
```

2. **配置数据库**
在 `src/main/resources/application.yml` 中修改 MySQL 连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: 你的数据库用户名
    password: 你的数据库密码
    driver-class-name: com.mysql.cj.jdbc.Driver
```

3. **初始化数据库**
执行以下 SQL 语句创建数据库与表：
```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS blog DEFAULT CHARACTER SET utf8mb4;

-- 使用数据库
USE blog;

-- 用户表
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `nickname` varchar(32) DEFAULT NULL COMMENT '昵称',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 文章表
CREATE TABLE `article` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(128) NOT NULL COMMENT '文章标题',
  `content` longtext NOT NULL COMMENT '文章内容',
  `user_id` bigint NOT NULL COMMENT '作者ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';
```

4. **启动项目**
```bash
# Maven 构建项目
mvn clean package

# 运行项目
java -jar target/blog-0.0.1-SNAPSHOT.jar
```
或直接在 IDEA 中运行 `BlogApplication.java` 启动类

5. **访问接口文档**
项目启动后，访问：`http://localhost:8989/doc.html`
即可查看所有接口并在线调试

---

## 📝 核心接口说明
| 接口路径 | 请求方法 | 接口说明 | 权限要求 |
|----------|----------|----------|----------|
| `/user/register` | POST | 用户注册 | 无 |
| `/user/login` | POST | 用户登录，返回 JWT Token | 无 |
| `/user/info` | GET | 获取当前登录用户信息 | 需登录 |
| `/article/add` | POST | 发布文章 | 需登录 |
| `/article/list` | GET | 分页查询文章列表 | 无 |
| `/article/detail/{id}` | GET | 查询文章详情 | 无 |
| `/article/update` | PUT | 修改文章 | 仅作者本人 |
| `/article/delete/{id}` | DELETE | 删除文章 | 仅作者本人 |

---
