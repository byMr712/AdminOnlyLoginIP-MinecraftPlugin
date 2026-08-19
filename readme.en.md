# AdminOnlyLoginIP v1.0

> **Язык:** English · [Russian](readme.md)

Minecraft login-to-IP address binding plugin. Binds a player's UUID to a specific IP address; logging in from a different IP causes the player to be kicked. Works with 'online-mode: false'.

---

## Requirements

- Paper 1.20+ (or a compatible Spigot/Paper/Purpur)
- Java 17+

---

## Installation

1. Copy `AdminOnlyLoginIP-1.0.jar` to the `plugins/` folder
2. Restart the server
3. Configure `plugins/AdminOnlyLoginIP/config.yml` if necessary

---

## How it works

1. The player executes `/aoli add <nickname>` — the plugin saves their UUID, nickname, and current IP in `data.yml`
2. Each time the plugin logs in, it checks: if the player's UUID is in the database, their IP must match the saved one.
3. If the IP doesn't match, the player is kicked.
4. This check can be disabled with the command `/aoli off` (useful when changing IP)

---

## Quick Start

### 1. Add Player

Join the game and run:

```
/aoli add <nickname>
```

This will save your UUID, the specified nickname, and your current IP address.

### 2. Try logging in from a different IP

If the IP doesn't match the saved one, you will be kicked with the message from the config.

## Settings

### config.yml

```yaml
# Enable IP check (true/false)
enabled: true

# Message when kicked for IP mismatch
kick-message: Access denied
```

| Parameter | Description |
|----------|----------|
| `enabled` | Global IP check toggle. `false` — everyone can log in from any IP |
| `kick-message` | Message the player will see when kicked |

---

## Commands

| Subcommand | Arguments | Description |
|------------|-----------|----------|
| `/aoli add <nick>` | `<nick>` | Save the current player's UUID + IP |
| `/aoli delete <nick>` | `<nick>` | Delete an entry by nickname |
| `/aoli on` | | Enable IP checking |
| `/aoli off` | | Disable IP checking |
| `/aoli reload` | | Reload config.yml |
| `/aoli list` | | Show all entries |

---

## Permissions

| Permission | Description | Default |
|-------|------------|--------------|
| `adminonlyloginip.admin` | Manage Logins and Settings | `op` |

---

## Files

| File | Description |
|------|----------|
| `plugins/AdminOnlyLoginIP/config.yml` | Plugin Configuration |
| `plugins/AdminOnlyLoginIP/data.yml` | Login Storage (automatic) |
| `plugins/AdminOnlyLoginIP/AdminOnlyLoginIP-1.0.jar` | Plugin |

---

## Build

Requirements: Java 17, Gradle 8.9+

```bash
./gradlew clean build
```

Output: `build/libs/AdminOnlyLoginIP-1.0.jar`