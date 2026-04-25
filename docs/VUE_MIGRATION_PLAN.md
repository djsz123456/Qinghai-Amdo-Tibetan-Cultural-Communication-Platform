# 雪域安多·藏韵传承 — Vue 工程化迁移方案

## 一、项目现状分析

### 1.1 当前技术栈

| 维度 | 现状 |
|------|------|
| 前端框架 | Vue 3 CDN 全局构建（`vue.global.prod.js`），Options API |
| 组件化 | 无 SFC（单文件组件），所有代码写在各 HTML 文件的 `<script>` 中 |
| CSS | 每个 HTML 文件内联 `<style>`，使用 CSS 变量 |
| 路由 | 纯 `<a href>` 跳转，约 30 个独立 HTML 页面 + 62 个藏历年页 |
| 状态管理 | 无 Vuex/Pinia，依赖 `localStorage` + Vue `data()` |
| 构建工具 | 无（无 npm、无 bundler、无 package.json） |
| 后端 | Spring Boot Java（Maven），预留骨架，`com.tibetan.platform`，目录为空 |
| 数据库 | 无，数据全部内嵌在 HTML 中或存于 localStorage |
| 认证 | localStorage 存储用户信息，后端调用失败时降级为本地模式 |
| 图标 | Font Awesome 4.7.0（CDN） |
| 字体 | Google Fonts: Noto Sans SC、Jomolhari |
| 外部 API | MyMemory 翻译 API、高德地图 API |

### 1.2 现有页面清单

#### 核心功能页（需迁移为 Vue 组件）
| 页面 | 文件 | 说明 |
|------|------|------|
| 首页 | `index.html` | Vue 3 驱动，含轮播、卡片滚动、地图、登录注册 |
| 藏族音乐 | `music/music.html` | Vue 3，音乐播放器 + 视频展示 |
| 藏文经典 | `jingdian/jingdian.html` | Vue 3，经文展示/翻译 |
| 藏汉翻译 | `fanyi/fanyi.html` | 原生 JS，调用 MyMemory API |
| 藏历查询 | `zangli-master/calendar.html` | 原生 JS，62 个年页数据 |
| 藏文键盘 | `jianpan/jianpan.html` | 原生 JS |
| 藏文字符 | `zifu/zifu.html` | 静态页 |
| 藏语短语 | `duanyu/duanyu.html` | 静态页 |
| 笔记 | `biji/biji.html` | Vue 3，localStorage 存储 |
| LMS 学习 | `lms/lms.html` | 学习管理 |
| 民俗文化 | `minsu.html` | 卡片列表 |
| 景点总览 | `jingdian.html`（根目录） | 景点列表 |
| 关于平台 | `guanyupingtai/guanyupingtai.html` | 静态页 |
| 使用指南 | `guide/guide.html` | 静态页 |

#### 地区/景点详情页（约 25 个，可模板化）
塔尔寺、青海湖、日月山、三江源、玉树、拉卜楞寺、桑科草原、黄南、果洛、阿坝、红原、若尔盖、那曲（含7个子页）等。

### 1.3 数据模型（从 localStorage 提取）

```
用户 (User)
├── id, username, password, authToken
├── 收藏列表 (favorites[])
└── 浏览历史 (browseHistory[])

笔记 (Note)
├── id, title, content, createdAt, updatedAt

景点 (TourSpot)
├── id, name, desc, img, link, region, coords[lat, lng]

民俗文化 (FolkCulture)
├── id, title, desc, img

音乐 (Music)
├── id, title, artist, type, duration, cover, audioUrl

经文 (Scripture)
├── id, tibetanText, chineseTranslation, order
```

---

## 二、目标技术栈

| 维度 | 目标方案 |
|------|----------|
| 前端框架 | **Vue 3 + Vite** (SFC + Composition API) |
| UI 组件库 | **Element Plus**（可选，按需引入） |
| 路由 | **Vue Router 4** |
| 状态管理 | **Pinia** |
| CSS 方案 | **SCSS** + CSS Variables（保留现有设计语言） |
| HTTP 请求 | **Axios** |
| 后端框架 | **Spring Boot 3**（保留现有 `com.tibetan.platform` 结构） |
| 数据库 | **PostgreSQL 16** |
| ORM | **Spring Data JPA (Hibernate)** |
| 容器化 | **Docker + Docker Compose** |
| 认证 | **JWT (JSON Web Token)** |
| 包管理 | **pnpm** |
| 代码规范 | **ESLint + Prettier** |

---

## 三、项目目录结构设计

```
project-root/
├── docker-compose.yml              # 编排前端、后端、数据库
├── .env                            # 环境变量
├── README.md
│
├── frontend/                       # Vue 3 前端工程
│   ├── package.json
│   ├── vite.config.ts
│   ├── tsconfig.json
│   ├── index.html                  # Vite 入口
│   ├── public/
│   │   ├── favicon.ico
│   │   └── images/                 # 静态图片资源（从原项目迁移）
│   └── src/
│       ├── main.ts                 # 应用入口
│       ├── App.vue                 # 根组件
│       ├── router/
│       │   └── index.ts            # 路由配置
│       ├── stores/                 # Pinia 状态管理
│       │   ├── user.ts             # 用户认证状态
│       │   ├── favorites.ts        # 收藏
│       │   └── music.ts            # 音乐播放状态
│       ├── api/                    # Axios 请求封装
│       │   ├── request.ts          # Axios 实例 + 拦截器
│       │   ├── auth.ts             # 登录/注册/注销
│       │   ├── spots.ts            # 景点 API
│       │   ├── folk.ts             # 民俗 API
│       │   ├── music.ts            # 音乐 API
│       │   ├── notes.ts            # 笔记 API
│       │   └── translate.ts        # 翻译 API
│       ├── views/                  # 页面级组件
│       │   ├── HomeView.vue        # 首页
│       │   ├── MusicView.vue       # 藏族音乐
│       │   ├── ScriptureView.vue   # 藏文经典
│       │   ├── TranslateView.vue   # 藏汉翻译
│       │   ├── CalendarView.vue    # 藏历查询
│       │   ├── KeyboardView.vue    # 藏文键盘
│       │   ├── CharacterView.vue   # 藏文字符
│       │   ├── PhraseView.vue      # 藏语短语
│       │   ├── NoteView.vue        # 笔记
│       │   ├── LmsView.vue         # 学习管理
│       │   ├── FolkView.vue        # 民俗文化
│       │   ├── SpotListView.vue    # 景点总览
│       │   ├── SpotDetailView.vue  # 景点详情（模板化，动态路由）
│       │   ├── AboutView.vue       # 关于平台
│       │   └── GuideView.vue       # 使用指南
│       ├── components/             # 可复用组件
│       │   ├── layout/
│       │   │   ├── AppHeader.vue   # 顶部导航（统一 topbar）
│       │   │   ├── AppFooter.vue   # 页脚
│       │   │   └── AppSidebar.vue  # 侧边栏（个人中心/收藏/历史）
│       │   ├── home/
│       │   │   ├── HeroBanner.vue  # 轮播图
│       │   │   ├── CardScroller.vue # 卡片横滚组件
│       │   │   └── MapSection.vue  # 地图区域
│       │   ├── music/
│       │   │   ├── SongCard.vue    # 歌曲卡片
│       │   │   └── AudioPlayer.vue # 音频播放器
│       │   ├── auth/
│       │   │   ├── LoginModal.vue  # 登录弹窗
│       │   │   └── RegisterModal.vue
│       │   └── common/
│       │       ├── BackToTop.vue   # 回到顶部
│       │       └── LoadingSpinner.vue
│       ├── composables/            # 组合式函数
│       │   ├── useAuth.ts          # 认证逻辑
│       │   ├── useFavorites.ts     # 收藏逻辑
│       │   └── useTranslate.ts     # 翻译逻辑
│       ├── assets/
│       │   ├── styles/
│       │   │   ├── variables.scss  # CSS 变量定义
│       │   │   ├── global.scss     # 全局样式
│       │   │   └── fonts.scss      # 字体声明
│       │   ├── fonts/              # 本地字体文件（Jomolhari 等）
│       │   └── icons/
│       └── types/                  # TypeScript 类型定义
│           ├── user.ts
│           ├── spot.ts
│           ├── music.ts
│           └── note.ts
│
├── backend/                        # Spring Boot 后端
│   ├── pom.xml
│   ├── Dockerfile
│   ├── src/main/java/com/tibetan/platform/
│   │   ├── TibetanPlatformApplication.java
│   │   ├── config/
│   │   │   ├── SecurityConfig.java       # Spring Security + JWT
│   │   │   ├── CorsConfig.java           # CORS 配置
│   │   │   └── JwtConfig.java            # JWT 工具类
│   │   ├── controller/
│   │   │   ├── AuthController.java       # POST /api/auth/login, /register
│   │   │   ├── UserController.java       # GET /api/user/profile
│   │   │   ├── SpotController.java       # CRUD /api/spots
│   │   │   ├── FolkController.java       # CRUD /api/folk
│   │   │   ├── MusicController.java      # CRUD /api/music
│   │   │   ├── NoteController.java       # CRUD /api/notes
│   │   │   ├── FavoriteController.java   # /api/favorites
│   │   │   └── TranslateController.java  # /api/translate (代理 MyMemory)
│   │   ├── entity/
│   │   │   ├── User.java
│   │   │   ├── TourSpot.java
│   │   │   ├── FolkCulture.java
│   │   │   ├── Music.java
│   │   │   ├── Note.java
│   │   │   ├── Favorite.java
│   │   │   └── BrowseHistory.java
│   │   ├── repository/
│   │   │   ├── UserRepository.java
│   │   │   ├── SpotRepository.java
│   │   │   ├── FolkRepository.java
│   │   │   ├── MusicRepository.java
│   │   │   ├── NoteRepository.java
│   │   │   └── FavoriteRepository.java
│   │   ├── service/
│   │   │   ├── AuthService.java
│   │   │   ├── UserService.java
│   │   │   ├── SpotService.java
│   │   │   ├── FolkService.java
│   │   │   ├── MusicService.java
│   │   │   ├── NoteService.java
│   │   │   └── FavoriteService.java
│   │   └── dto/
│   │       ├── LoginRequest.java
│   │       ├── RegisterRequest.java
│   │       └── ApiResponse.java
│   └── src/main/resources/
│       ├── application.yml
│       ├── application-dev.yml
│       ├── application-prod.yml
│       └── db/
│           └── migration/                # Flyway 数据库迁移
│               ├── V1__init_schema.sql
│               └── V2__seed_data.sql
│
└── database/                       # 数据库相关
    ├── Dockerfile                  # PostgreSQL 镜像定制
    └── init/
        └── 01_create_tables.sql    # 初始化表结构（备选 Flyway）
```

---

## 四、数据库设计 (PostgreSQL)

### 4.1 ER 关系概览

```
users ──< favorites >── tour_spots
  │                        │
  ├──< notes               │
  ├──< browse_history >────┘
  │
folk_cultures (独立)
music (独立)
scriptures (独立)
calendar_data (独立，可选)
```

### 4.2 表结构定义

```sql
-- 用户表
CREATE TABLE users (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nickname      VARCHAR(100),
    avatar_url    VARCHAR(500),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 景点表
CREATE TABLE tour_spots (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    region      VARCHAR(50) NOT NULL,       -- qinghai / gannan / chuanxi / zangbei
    description TEXT,
    image_url   VARCHAR(500),
    detail_url  VARCHAR(500),
    latitude    DECIMAL(10, 7),
    longitude   DECIMAL(10, 7),
    sort_order  INT DEFAULT 0,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 民俗文化表
CREATE TABLE folk_cultures (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    description TEXT,
    image_url   VARCHAR(500),
    sort_order  INT DEFAULT 0,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 音乐表
CREATE TABLE music (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    artist      VARCHAR(100),
    category    VARCHAR(50),                -- 安多民歌 / 曼陀铃弹唱 / 器乐独奏 / 酒歌
    duration    VARCHAR(10),
    cover_url   VARCHAR(500),
    audio_url   VARCHAR(500),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 经文表
CREATE TABLE scriptures (
    id                  BIGSERIAL PRIMARY KEY,
    tibetan_text        TEXT NOT NULL,
    chinese_translation TEXT,
    sort_order          INT DEFAULT 0,
    scripture_name      VARCHAR(200)        -- 经名，如"二十一度母礼赞"
);

-- 用户笔记表
CREATE TABLE notes (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT REFERENCES users(id) ON DELETE CASCADE,
    title       VARCHAR(200),
    content     TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 用户收藏表
CREATE TABLE favorites (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT REFERENCES users(id) ON DELETE CASCADE,
    spot_id     BIGINT REFERENCES tour_spots(id) ON DELETE CASCADE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, spot_id)
);

-- 浏览历史表
CREATE TABLE browse_history (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT REFERENCES users(id) ON DELETE CASCADE,
    spot_id     BIGINT REFERENCES tour_spots(id) ON DELETE CASCADE,
    visited_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_favorites_user ON favorites(user_id);
CREATE INDEX idx_notes_user ON notes(user_id);
CREATE INDEX idx_history_user ON browse_history(user_id);
CREATE INDEX idx_spots_region ON tour_spots(region);
CREATE INDEX idx_music_category ON music(category);
```

---

## 五、Docker 容器化方案

### 5.1 docker-compose.yml

```yaml
version: '3.8'

services:
  # PostgreSQL 数据库
  db:
    image: postgres:16-alpine
    container_name: tibetan-db
    restart: unless-stopped
    environment:
      POSTGRES_DB: tibetan_platform
      POSTGRES_USER: ${DB_USER:-tibetan}
      POSTGRES_PASSWORD: ${DB_PASSWORD:-tibetan123}
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data
      - ./database/init:/docker-entrypoint-initdb.d
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U tibetan -d tibetan_platform"]
      interval: 10s
      timeout: 5s
      retries: 5

  # Spring Boot 后端
  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    container_name: tibetan-backend
    restart: unless-stopped
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/tibetan_platform
      SPRING_DATASOURCE_USERNAME: ${DB_USER:-tibetan}
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD:-tibetan123}
      JWT_SECRET: ${JWT_SECRET:-your-secret-key-change-in-production}
    ports:
      - "8080:8080"
    depends_on:
      db:
        condition: service_healthy

  # Vue 前端（生产模式用 Nginx 托管）
  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    container_name: tibetan-frontend
    restart: unless-stopped
    ports:
      - "80:80"
    depends_on:
      - backend

volumes:
  pgdata:
```

### 5.2 后端 Dockerfile

```dockerfile
# backend/Dockerfile
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 5.3 前端 Dockerfile

```dockerfile
# frontend/Dockerfile
FROM node:20-alpine AS build
RUN corepack enable && corepack prepare pnpm@latest --activate
WORKDIR /app
COPY package.json pnpm-lock.yaml ./
RUN pnpm install --frozen-lockfile
COPY . .
RUN pnpm build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
```

### 5.4 Nginx 配置

```nginx
# frontend/nginx.conf
server {
    listen 80;
    server_name localhost;
    root /usr/share/nginx/html;
    index index.html;

    # Vue Router history 模式
    location / {
        try_files $uri $uri/ /index.html;
    }

    # 后端 API 反向代理
    location /api/ {
        proxy_pass http://backend:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff2?)$ {
        expires 30d;
        add_header Cache-Control "public, immutable";
    }
}
```

---

## 六、Vue Router 路由设计

```typescript
// frontend/src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/',            name: 'Home',       component: () => import('@/views/HomeView.vue') },
  { path: '/music',       name: 'Music',      component: () => import('@/views/MusicView.vue') },
  { path: '/scripture',   name: 'Scripture',   component: () => import('@/views/ScriptureView.vue') },
  { path: '/translate',   name: 'Translate',   component: () => import('@/views/TranslateView.vue') },
  { path: '/calendar',    name: 'Calendar',    component: () => import('@/views/CalendarView.vue') },
  { path: '/keyboard',    name: 'Keyboard',    component: () => import('@/views/KeyboardView.vue') },
  { path: '/characters',  name: 'Characters',  component: () => import('@/views/CharacterView.vue') },
  { path: '/phrases',     name: 'Phrases',     component: () => import('@/views/PhraseView.vue') },
  { path: '/notes',       name: 'Notes',       component: () => import('@/views/NoteView.vue'),      meta: { requiresAuth: true } },
  { path: '/lms',         name: 'Lms',         component: () => import('@/views/LmsView.vue') },
  { path: '/folk',        name: 'Folk',        component: () => import('@/views/FolkView.vue') },
  { path: '/spots',       name: 'Spots',       component: () => import('@/views/SpotListView.vue') },
  { path: '/spots/:id',   name: 'SpotDetail',  component: () => import('@/views/SpotDetailView.vue') },
  { path: '/about',       name: 'About',       component: () => import('@/views/AboutView.vue') },
  { path: '/guide',       name: 'Guide',       component: () => import('@/views/GuideView.vue') },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior: () => ({ top: 0 })
})

// 路由守卫：需要登录的页面
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('authToken')
  if (to.meta.requiresAuth && !token) {
    next({ name: 'Home', query: { login: '1' } })
  } else {
    next()
  }
})

export default router
```

---

## 七、API 接口设计

### 7.1 认证接口

| 方法 | 路径 | 说明 | 请求体 |
|------|------|------|--------|
| POST | `/api/auth/register` | 用户注册 | `{ username, password }` |
| POST | `/api/auth/login` | 用户登录 | `{ username, password }` |
| POST | `/api/auth/logout` | 退出登录 | — |
| GET  | `/api/auth/profile` | 获取当前用户信息 | — (Header: Bearer Token) |

### 7.2 业务接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/spots` | 景点列表（支持 `?region=qinghai` 筛选） |
| GET | `/api/spots/:id` | 景点详情 |
| GET | `/api/folk` | 民俗文化列表 |
| GET | `/api/music` | 音乐列表（支持 `?category=安多民歌` 筛选） |
| GET | `/api/scriptures` | 经文列表 |
| GET/POST/DELETE | `/api/notes` | 笔记 CRUD |
| GET/POST/DELETE | `/api/favorites` | 收藏 CRUD |
| GET | `/api/history` | 浏览历史 |
| POST | `/api/translate` | 翻译代理（后端调用 MyMemory，避免前端跨域） |

### 7.3 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { }
}
```

---

## 八、迁移实施步骤

### 第一阶段：基础搭建（预计 2-3 天）

1. **初始化前端工程**
   ```bash
   cd project-root
   pnpm create vite frontend --template vue-ts
   cd frontend
   pnpm add vue-router@4 pinia axios element-plus
   pnpm add -D sass @types/node
   ```

2. **初始化后端工程**
   - 在 `backend/` 添加 `pom.xml`，引入：
     - `spring-boot-starter-web`
     - `spring-boot-starter-data-jpa`
     - `spring-boot-starter-security`
     - `postgresql` 驱动
     - `jjwt`（JWT 库）
     - `flyway-core`（数据库迁移）
   - 编写 `application.yml` 连接 PostgreSQL

3. **Docker 环境搭建**
   ```bash
   docker-compose up db    # 先启动数据库
   ```

4. **创建数据库表结构**（Flyway 迁移脚本或手动执行 SQL）

### 第二阶段：核心组件迁移（预计 5-7 天）

按优先级迁移：

| 优先级 | 组件 | 来源文件 | 迁移要点 |
|--------|------|----------|----------|
| P0 | 布局框架 | 新建 | AppHeader + AppFooter + Router，统一导航 |
| P0 | 首页 | `index.html` | 拆分为 HeroBanner、CardScroller、MapSection 等子组件 |
| P0 | 认证 | `index.html` | 登录/注册弹窗 → LoginModal、JWT 集成 |
| P1 | 藏族音乐 | `music/music.html` | SongCard + AudioPlayer 组件 |
| P1 | 藏文经典 | `jingdian/jingdian.html` | 直接迁移 Vue data，改为 SFC |
| P1 | 藏汉翻译 | `fanyi/fanyi.html` | 改为后端代理调用 MyMemory |
| P1 | 藏历查询 | `zangli-master/` | 引入 zangli.js 库，整合 62 个年页数据 |
| P2 | 笔记 | `biji/biji.html` | localStorage → API + 数据库 |
| P2 | 收藏/历史 | `index.html` | localStorage → API + 数据库 |
| P2 | 景点详情 | 各区域 HTML | 模板化为动态路由 `/spots/:id` |
| P3 | 其他工具 | 键盘/字符/短语 | 低改动，直接包裹为 SFC |

### 第三阶段：数据迁移（预计 2-3 天）

1. 提取 `index.html` 中内嵌的景点数据（`qinghaiPlaces`、`gannanPlaces` 等）→ 写入 `V2__seed_data.sql`
2. 提取音乐数据 → 写入数据库
3. 提取经文数据 → 写入数据库
4. 提取民俗文化数据 → 写入数据库

### 第四阶段：联调测试 & 部署（预计 2-3 天）

1. 前后端联调
2. Docker Compose 全容器启动测试
3. 数据库持久化验证
4. 生产环境部署

---

## 九、后端 Spring Boot 配置参考

### application.yml

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tibetan_platform
    username: tibetan
    password: tibetan123
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: validate    # 生产环境用 validate，开发用 update
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
  flyway:
    enabled: true
    locations: classpath:db/migration

jwt:
  secret: ${JWT_SECRET:default-secret-change-me}
  expiration: 86400000    # 24 小时

# MyMemory 翻译 API 代理配置
translate:
  api-url: https://api.mymemory.translated.net/get
```

### pom.xml 核心依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-core</artifactId>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.5</version>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

---

## 十、关键迁移决策说明

### 10.1 景点详情页模板化

现有约 25 个区域/景点详情页结构高度相似（标题 + 图片轮播 + 文字介绍 + 地图）。迁移时**不需要 25 个独立 View**，而是：

```
/spots/:id  →  SpotDetailView.vue（动态路由）
```

通过 `spot_id` 从数据库加载数据，一个组件覆盖所有景点。

### 10.2 藏历数据处理

`zangli-master/` 包含 62 个年页 HTML（1991-2050），本质是 `zangli.js` 库计算生成的。迁移方案：

- **不需要导入 62 个 HTML 文件**
- 将 `zangli.js` 作为工具库引入前端
- 由前端实时计算藏历数据，无需存入数据库

### 10.3 翻译 API 代理

现有翻译直接从前端调用 MyMemory API。迁移后改为后端代理：
- 前端 → 后端 `/api/translate` → MyMemory API
- 好处：隐藏 API 密钥、便于切换翻译引擎、统一错误处理

### 10.4 认证迁移

现有的 localStorage 认证需平滑迁移到 JWT：
- 注册/登录请求发到后端，后端返回 JWT Token
- 前端 Axios 拦截器自动附加 `Authorization: Bearer <token>`
- Token 过期自动跳转登录

### 10.5 收藏与分享功能

迁移后的收藏功能：
- **收藏**：登录用户 → `POST /api/favorites` 写入数据库；未登录 → 提示登录
- **分享**：优先使用浏览器原生 `navigator.share()` API，降级为复制链接

---

## 十一、风险与注意事项

| 风险 | 应对 |
|------|------|
| 藏文字体渲染 | 确保 Jomolhari 字体本地化打包，不依赖外部 CDN |
| 高德地图 Key | 需申请正式 Key，配置到 `.env` 环境变量 |
| MyMemory API 限制 | 免费版有每日限额，可考虑后续替换为其他翻译引擎 |
| 62 个藏历年页 | 不迁移静态 HTML，用 zangli.js 实时计算 |
| 图片资源迁移 | 统一迁移到 `public/images/`，更新所有引用路径 |
| 数据种子 | 首次部署需执行 seed 脚本填充景点、音乐、经文等基础数据 |

---

## 十二、快速启动（开发环境）

```bash
# 1. 启动 PostgreSQL（Docker）
docker-compose up -d db

# 2. 启动后端（开发模式）
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 3. 启动前端（开发模式）
cd frontend
pnpm dev
```

前端默认运行在 `http://localhost:5173`，后端在 `http://localhost:8080`，Vite 开发服务器会自动代理 `/api` 请求到后端。

```typescript
// vite.config.ts
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

---

## 十三、生产部署（一键启动）

```bash
# 构建并启动所有服务
docker-compose up -d --build

# 查看日志
docker-compose logs -f

# 停止
docker-compose down
```

访问 `http://localhost` 即可使用完整平台。
