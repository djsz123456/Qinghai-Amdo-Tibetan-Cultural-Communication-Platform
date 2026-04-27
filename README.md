# 雪域安多·藏韵传承 — 安多藏族文化传播平台

<p align="center">
  <strong>探寻安多秘境，传承藏韵文化</strong>
</p>

---

## 简介

本平台以"雪域安多·藏韵传承"为主题，围绕青海安多藏族文化，构建了集**藏地旅游展示、安多民俗介绍、藏汉互译工具、藏文字符查询、藏语常用短语学习、藏历查询**等功能于一体的综合性文化传播 Web 平台。

平台覆盖**青海、甘南、川西、藏北**四大安多藏区，共 35+ 景点文化专题页面，采用统一的红金藏式视觉设计语言。

## 在线访问

**GitHub Pages**:  https://djsz123456.github.io/Amdo-Tibetan-Cultural-Communication-Platform/

## 快速启动

```bash
git clone https://github.com/djsz123456/Qinghai-Amdo-Tibetan-Cultural-Communication-Platform.git
cd Qinghai-Amdo-Tibetan-Cultural-Communication-Platform
python -m http.server 8080
# 浏览器打开 http://localhost:8080
```

## 功能模块

| 模块 | 说明 |
|------|------|
| 藏地旅游 | 35+ 景点专题页，覆盖四大安多藏区 |
| 安多民俗 | 传统服饰、饮食、节庆、游牧文化 |
| 藏汉互译 | 基于 MyMemory API 的即时双向翻译 |
| 藏文工具 | 键盘输入、字符查询、常用短语、笔记 |
| 藏历查询 | 1951-2050 年藏历万年历 |
| 景点地图 | 高德地图交互式景点导航 |
| 个人中心 | 收藏管理、浏览历史 |

## 技术栈

| 技术 | 用途 |
|------|------|
| Vue 3 (CDN, Options API) | 前端响应式框架 |
| 高德地图 JS API v1.4.15 | 交互式地图 |
| MyMemory Translation API | 藏汉翻译 |
| Web Storage API | 本地数据持久化 |
| CSS Custom Properties | 自研设计系统 |
| Font Awesome 4.7.0 | 图标库 |
| Noto Sans SC | 中文字体 |

## 项目结构

```
├── index.html              # 首页（主入口）
├── jingdian.html            # 景点详情
├── minsu.html               # 民俗详情
├── docs/                    # 项目文档
│   ├── 2-作品信息概要表.md
│   ├── 3-设计和开发文档.md
│   ├── 4-AI工具使用说明.md
│   └── 5-技术架构文档.md
├── fanyi/                   # 藏汉翻译
├── biji/                    # 藏文笔记
├── jianpan/                 # 藏文键盘
├── zifu/                    # 藏文字符
├── duanyu/                  # 常用短语
├── music/                   # 藏族音乐
├── zangli-master/           # 藏历模块
├── taersi/ qinghahu/ ...    # 景点专题页（35+）
├── naqu/                    # 那曲景点（7个子页面）
└── image/ image1/           # 图片资源
```

## 文档

- [作品信息概要表](docs/2-作品信息概要表.md)
- [设计和开发文档](docs/3-设计和开发文档.md)
- [AI工具使用说明](docs/4-AI工具使用说明.md)
- [技术架构文档](docs/5-技术架构文档.md)

## 许可

本项目为参赛作品，仅供学术交流使用。藏历模块（zangli-master）采用 MIT License。
