# VConomy

> A modern, lightweight, multi-currency economy plugin for Minecraft Spigot/Paper servers.

![VConomy](icon.png)

English | [简体中文](README_zh.md)

---

## Overview

VConomy is a high-performance Minecraft economy plugin designed for servers that need more than a single virtual currency. It supports multiple independent currency types, and also provides lots of configurable options to suit your needs.

## Features

- **Multi-Currency**: You can define any number of currency types via simple YAML files. Each type has its own balance column, display format, decimal precision, initial value, and tax rate.
- **Multiple Databases Support**: Supports H2 (embedded, zero-config), MySQL, PostgreSQL, and SQLite. In the future, more databases will be added.
- **Lightweight**: The size of the plugin is less than 1 MB. Just HikariCP and SnakeYAML were bundled. JDBC drivers are auto-downloaded on first use.
- **Simple Configuration**: There are plenty of comments in the config files so that they are easy to understand. 
- **Vault Integration**: Exposes a configurable economy type through the Vault API, enabling compatibility with shops, auctions, and other Vault-dependent plugins.
- **PlaceholderAPI Support**: You're able to use placeholders like `%vconomy_coin%` in scoreboards, chat formatting, and custom plugins.
- **Granular Permissions**: Every command has its own permission node, allowing fine-grained control over who can do what.
- **Customizable Command Aliases**: All commands support configurable aliases so that players can use them in a most familiar way.

## Installation

### Requirements

- Server Core (Spigot / Paper / Purpur) >= 1.8
- Java >= 8

### Steps

1. Download the latest `vconomy-spigot-x.x.x.jar` from the [Releases](https://github.com/Vaelow233/VConomy/releases) page
2. Place the JAR into your server's `plugins/` folder
3. Restart your server
4. Finished! Run `/vconomy help` to verify whether it's working

### Optional Plugins

- **[Vault](https://www.spigotmc.org/resources/vault.34315/)** -- compatibility with Vault-dependent economy plugins
- **[PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)** -- placeholder expansion

## Usage

### Commands

| Command                                      | Description                      | Permission                           |
|----------------------------------------------|----------------------------------|--------------------------------------|
| `/balance <type> [player]`                   | View your (or another's) balance | `vconomy.get` / `vconomy.get.others` |
| `/balancetop <type> [page]`                  | Show balance leaderboard         | `vconomy.top`                        |
| `/pay <player> <type> <amount>`              | Send money to a player           | `vconomy.pay`                        |
| `/vconomy get/show <type> [player]`          | View specific balance            | `vconomy.get`                        |
| `/vconomy top <type> [page]`                 | Balance leaderboard (detailed)   | `vconomy.top`                        |
| `/vconomy give/add <player> <type> <amount>` | Give money to a player (admin)   | `vconomy.admin.give`                 |
| `/vconomy take <player> <type> <amount>`     | Take money from a player (admin) | `vconomy.admin.take`                 |
| `/vconomy set <player> <type> <amount>`      | Set a player's balance (admin)   | `vconomy.admin.set`                  |
| `/vconomy reset <player> [type]`             | Reset a player's balance (admin) | `vconomy.admin.reset`                |
| `/vconomy reload`                            | Reload configuration             | `vconomy.admin.reload`               |
| `/vconomy help`                              | Show help message                | `vconomy.admin.help` (for admins)    |

### Permissions

| Permission             | Description              | Default     |
|------------------------|--------------------------|-------------|
| `vconomy.get`          | View your own balance    | All players |
| `vconomy.get.others`   | View others' balance     | OP          |
| `vconomy.top`          | View balance leaderboard | All players |
| `vconomy.pay`          | Send money to others     | All players |
| `vconomy.admin.give`   | Admin — give money       | OP          |
| `vconomy.admin.take`   | Admin — take money       | OP          |
| `vconomy.admin.set`    | Admin — set balance      | OP          |
| `vconomy.admin.help`   | Admin — view admin help  | OP          |
| `vconomy.admin.reset`  | Admin — reset balance    | OP          |
| `vconomy.admin.reload` | Admin — reload config    | OP          |

## Building from Source

```bash
git clone https://github.com/Vaelow233/VConomy.git
cd VConomy
./gradlew shadowJar
```

The output JAR of the Spigot build will be at `vconomy-spigot/build/libs/vconomy-spigot-x.x.x-all.jar`.

## License

This project is licensed under the [LGPL-3.0 License](LICENSE)

## Author

- **Vaelow233** — [GitHub](https://github.com/Vaelow233)
