# VConomy

> 一个现代化, 轻量, 具有多货币支持的 Minecraft 经济插件

![VConomy](icon.png)

[English](README.md) | 简体中文

---

## 概述

VConomy 是一个高性能 Minecraft 经济插件, 专为需要多种货币的服务器而设计, 它支持多个独立的货币类型, 并提供大量可配置的选项以满足你的需求

## 特性

- **多货币**: 你可以通过简单的 YAML 文件定义每种货币类型的显示格式, 小数精度, 初始金额和交易税率等
- **多数据库支持**: 支持 H2(嵌入式,零配置), MySQL, PostgreSQL 和 SQLite 数据库, 未来将支持更多
- **轻量**: 插件体积仅 1 MB, 仅打包了 HikariCP 和 SnakeYAML, JDBC 数据库驱动在首次使用时自动下载
- **配置简单**: 配置文件中包含大量注释, 易于理解
- **Vault 集成**: 你可通过配置文件定义 Vault 所用的货币类型, 作为商店等其它基于 Vault 的插件的货币
- **PlaceholderAPI 支持**: 你可以在计分板, 聊天或其它支持 PlaceholderAPI 的插件中使用 `%vconomy_coin%` 等占位符来表示玩家的余额
- **权限细化**: 每条命令都有独立的权限节点, 方便管理
- **自定义命令别名**: 所有命令均可配置别名, 让玩家能以最熟悉的方式使用它们

## 安装

### 前置要求

- 服务端核心 (Spigot / Paper / Purpur) >= 1.8
- Java >= 8

### 步骤

1. 从 [Releases](https://github.com/Vaelow233/VConomy/releases) 页面下载最新的 `vconomy-spigot-x.x.x.jar`
2. 将 JAR 放入服务器的 `plugins/` 文件夹
3. 重启服务器
4. 完成! 执行 `/vconomy help` 验证插件是否正常运行

### 可选插件

- **[Vault](https://www.spigotmc.org/resources/vault.34315/)** -- 与更多依赖 Vault 的插件兼容
- **[PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)** -- 占位符支持

## 使用

### 命令

| 命令                                           | 描述           | 权限                                   |
|----------------------------------------------|--------------|--------------------------------------|
| `/balance <type> [player]`                   | 查看自己(或他人)的余额 | `vconomy.get` / `vconomy.get.others` |
| `/balancetop <type> [page]`                  | 显示余额排行榜      | `vconomy.top`                        |
| `/pay <player> <type> <amount>`              | 向玩家转账        | `vconomy.pay`                        |
| `/vconomy get/show <type> [player]`          | 查看特定货币的余额    | `vconomy.get`                        |
| `/vconomy top <type> [page]`                 | 余额排行榜(详细)    | `vconomy.top`                        |
| `/vconomy give/add <player> <type> <amount>` | 给玩家增加余额(管理)  | `vconomy.admin.give`                 |
| `/vconomy take <player> <type> <amount>`     | 从玩家扣除余额(管理)  | `vconomy.admin.take`                 |
| `/vconomy set <player> <type> <amount>`      | 设置玩家的余额(管理)  | `vconomy.admin.set`                  |
| `/vconomy reset <player> [type]`             | 重置玩家的余额(管理)  | `vconomy.admin.reset`                |
| `/vconomy help`                              | 显示帮助信息       | `vconomy.admin.help` (管理员专用帮助)       |

### 权限

| 权限                    | 描述           | 默认   |
|-----------------------|--------------|------|
| `vconomy.get`         | 查看自己的余额      | 所有玩家 |
| `vconomy.get.others`  | 查看他人的余额      | OP   |
| `vconomy.top`         | 查看余额排行榜      | 所有玩家 |
| `vconomy.pay`         | 向他人转账        | 所有玩家 |
| `vconomy.admin.give`  | 管理 -- 增加余额   | OP   |
| `vconomy.admin.take`  | 管理 -- 扣除余额   | OP   |
| `vconomy.admin.set`   | 管理 -- 设置余额   | OP   |
| `vconomy.admin.help`  | 管理 -- 查看管理帮助 | OP   |
| `vconomy.admin.reset` | 管理 -- 重置余额   | OP   |

## 构建

```bash
git clone https://github.com/Vaelow233/VConomy.git
cd VConomy
./gradlew shadowJar
```

Spigot 平台的构建产物位于 `vconomy-spigot/build/libs/vconomy-spigot-x.x.x-all.jar`

## 许可证

本项目基于 [LGPL-3.0](LICENSE) 协议开源

## 作者

- **Vaelow233** -- [GitHub](https://github.com/Vaelow233)
