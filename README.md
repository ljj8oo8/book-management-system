# 图书管理系统DEMO

## 项目介绍
图书管理系统DEMO是一套基于前后端分离架构开发的系统，支持图书增删改查、PDF在线预览、用户权限管控。支持一键打包运行
### 项目关键特性
- 前后端分离：前端静态资源与后端解耦，前端代码无需编译，部署灵活，支持独立扩展
- 动态权限：权限规则存储于数据库，无需重启项目即可调整权限范围
- 安全防护：密码 BCrypt 加密、JWT 防篡改、接口权限拦截、SQL 注入防护（MyBatis 参数绑定）
- PDF 预览：基于 PDF.js 2.16.x 实现文件流预览，无需依赖第三方插件，支持多页渲染 + 固定头部（关闭按钮不滚动）

### 系统功能
#### 1. 图书管理模块
   - 图书列表：分页查询、多条件筛选（名称 / 作者 / 出版社 / ISBN）、关键词搜索
   - 图书操作：管理员可新增 / 编辑 / 删除图书，上传 PDF 文件、Base64 格式封面图
   - 图书详情：展示完整图书信息，支持 PDF 在线预览（文件流方式）、PDF 下载、封面图查看
   - 数据校验：新增 / 编辑图书时校验必填项、文件类型（仅支持 PDF）
#### 2. 用户管理
   - 角色绑定：为用户分配「管理员 / 普通用户」角色，控制权限边界
#### 3. 权限
   - 权限模型：基于「用户 - 角色 - 权限」三层动态权限体系
   - 菜单权限：控制页面是否可见（如普通用户看不到「用户管理」页面）
   - 接口权限：控制接口是否可访问（如普通用户无法调用图书删除接口）
   - 规则配置：支持 URL 通配符匹配（如/book/**）、请求方法（GET/POST/PUT/DELETE）权限管控
#### 4. 认证与授权模块
   - 登录认证：用户名 + 密码校验，验证通过后生成 JWT Token 返回前端
   - 接口权限：Spring Security 过滤器拦截未授权请求，返回 403
   - 页面权限：前端根据用户权限动态隐藏按钮（如普通用户无「删除图书」按钮）
   

## 技术栈说明
### 1. 后端技术
| 技术/框架                  | 版本     | 用途说明                |
|------------------------|--------|---------------------|
| Spring Boot            | 2.7.x  | 后端核心框架，简化开发流程       |
| Spring Security        | 5.7.x  | 认证与授权，实现动态权限控制      |
| MyBatis + MyBatis-Plus | 3.5.x  | 数据持久层，简化CRUD操作      |
| H2                     | 2.2.x  | 数据库（开发用H2，生产用MySQL） |
| caffeine               | 2.9.3  | 内存缓存                |
| JWT                    | 0.11.5 | 无状态认证，生成用户Token     |
| Lombok                 | 1.18.x | 简化实体类代码，减少模板代码      |
| Swagger / OpenAPI      | 3.0.x  | 自动生成接口文档，便于调试       |

### 2. 前端技术
| 技术/框架               | 版本       | 用途说明                     |
|-------------------------|------------|----------------------------|
| Vue.js                  | 3.x        | 前端核心框架，实现组件化开发 |
| Bootstrap               | 5.2.x      | UI组件库，快速构建响应式界面 |
| Axios                   | 1.4.x      | 前端HTTP请求库，处理接口交互 |
| PDF.js                  | 2.16.x     | PDF文件在线预览，支持多页渲染 |
| Font Awesome           | 6.4.x      | 图标库，丰富界面视觉元素     |


## 系统访问与运行
### 1. 环境要求
- **JDK**：1.8 及以上
- **Maven**：3.6 及以上
- **数据库**： H2 数据库(内嵌)

### 2. 项目克隆
```bash
# 克隆GitHub仓库（替换为你的仓库地址）
git clone https://github.com/ljj8oo8/WrapBootstrapDownload.git
cd 你的仓库名
```

### 3. 修改application.yaml
```yaml
book:
  uploads:
    pdf:
      path: O:\uploads\pdf\  #存储PDF文件绝对路径
    cover:
      path: O:\uploads\pdf\cover\  #存储PDF封面图片绝对路径
```
### 4. 打包运行
```bash
cd 项目根目录
mvn clean package -P dev/test/prod
# 基础运行（默认配置，使用H2内存数据库）
java -jar target/book-management-system-1.0.jar
```

### 5. 访问地址
| 系统         | 地址                                       | 账户                     |
|------------|------------------------------------------|-----------------------------|
| 图书管理系统demo |http://localhost:8080/views/login.html| admin/admin123 user/user123 |
| H2 数据库控制台  | http://localhost:8080/h2-console       | sa/空                     |
| Swagger 接口文档 | http://localhost:8080/swagger-ui/index.html |                     |
| druid      | http://localhost:8080/druid              | admin/admin123              |

### 6. 系统角色和权限
| 角色           | 账户             | 功能            |
|--------------|----------------|---------------|
| 管理员          | admin/admin123 | 图书CURD、PDF 预览 |
| 普通用户         | user/user123   | 仅图书查询、PDF 预览  |

## 常见问题排查
### 1. 登录提示「Bad credentials」（凭证错误）
 - 现象：用户名密码正确但登录失败，控制台提示Empty encoded password
 - 排查步骤：
   1. 检查数据库user表password字段：需为 BCrypt 加密格式（以$2a$开头），非空
   2.  确认CustomUserDetailsService中loadUserByUsername方法正确返回user.getPassword()（非 null）
   3.  检查用户状态：status=1为启用，status=0禁用（禁用用户会伪装成凭证错误）
### 2. 权限失效（管理员 / 普通用户权限不符）
   - 现象：管理员看不到用户管理页面，或普通用户能删除图书
   - 排查步骤：
     1. 检查user_role、role_permission表：确保管理员角色（role_id=1）绑定全量权限，普通用户（role_id=2）仅绑定图书查询权限
### 3. H2 数据库无法访问
   - 现象：访问h2-console提示连接失败
   - 排查步骤：
     1. 检查application.yml：确保spring.h2.console.enabled=true，且无 IP 限制（spring.h2.console.settings.web-allow-others=true）
     2. 确认 JDBC URL：jdbc:h2:mem:book_db（与配置文件中spring.datasource.url一致）

## 待完善
 - 通过md5摘要算法校验图书是否重复
 - 实现用户，角色，菜单的管理接口以及界面
 - 实现数据权限的管理
 - 权限相关库表需要根据功能进行字段扩展，比如增加菜单表，按钮表
 - 实现游客相关功能及界面
 - 实现token的过期刷新以及长时间未操作自动退出逻辑
 - 需调整前端代码设计通过vue组件的形式重构代码