# AdminOnlyLoginIP v1.0

Minecraft plugin that binds player login to a specific IP address. Associates a player's UUID with an IP — if they try to log in from a different IP, they get kicked. Works with `online-mode: false`.

---

## Requirements

- Paper 1.20+ (or compatible Spigot/Paper fork)
- Java 17+
- `online-mode: false` in `server.properties`

---

## Installation

1. Copy `AdminOnlyLoginIP-1.0.jar` to the `plugins/` folder
2. Restart the server
3. Configure `plugins/AdminOnlyLoginIP/config.yml` if needed

---

## Quick Start

### 1. Add a player

Stand in-game and run:

```
/aoli add <nickname>
```

This saves your UUID, the specified nickname, and your current IP address.

### 2. Try logging in from a different IP

If the IP doesn't match the stored one — you get kicked with the message from config.

### 3. Management

```
/aoli list           — show all entries
/aoli delete <nick>  — remove an entry
/aoli on             — enable IP check
/aoli off            — disable IP check
/aoli reload         — reload config
```

---

## Configuration

### config.yml

```yaml
# Enable IP check (true/false)
enabled: true

# Kick message on IP mismatch
kick-message: Доступ запрещён

# Saved entries (automatic, do not edit manually)
entries: {}
```

| Parameter | Description |
|-----------|-------------|
| `enabled` | Global toggle for IP checking. `false` — everyone can join from any IP |
| `kick-message` | Message shown to the player when kicked |
| `entries` | Automatic binding list. Managed via commands |

---

## Commands

| Subcommand | Arguments | Description |
|------------|-----------|-------------|
| `/aoli add <nick>` | `<nick>` | Save UUID + IP of the current player |
| `/aoli delete <nick>` | `<nick>` | Remove entry by nickname |
| `/aoli on` | | Enable IP checking |
| `/aoli off` | | Disable IP checking |
| `/aoli reload` | | Reload config.yml |
| `/aoli list` | | Show all entries |

---

## How it works

1. A player runs `/aoli add <nickname>` — the plugin saves their UUID, nickname, and current IP to `data.yml`
2. On every login, the plugin checks: if the player's UUID exists in the database, their IP must match the stored one
3. If the IP doesn't match — the player is kicked
4. You can disable the check with `/aoli off` (useful when changing IP)

---

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `adminonlyloginip.admin` | Manage entries and settings | `op` |

---

## Files

| File | Description |
|------|-------------|
| `plugins/AdminOnlyLoginIP/config.yml` | Plugin configuration |
| `plugins/AdminOnlyLoginIP/data.yml` | Entry storage (automatic) |
| `plugins/AdminOnlyLoginIP/AdminOnlyLoginIP-1.0.jar` | Plugin |

---

## Build

Requirements: Java 17, Gradle 8.9+

```bash
./gradlew clean build
```

Output: `build/libs/AdminOnlyLoginIP-1.0.jar`
