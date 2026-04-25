# AlaLoot

A NeoForge mod for Minecraft Java Edition that replaces vanilla loot with random items from the full item registry. The drop chance equals your XP level — at level 100 every block break and every mob kill gives you something. Boss kills always reward fixed legendary stacks.

- **Minecraft:** 1.21.10 (26.1.2)
- **Loader:** NeoForge 26.1.2.28-beta
- **Side:** server-side only — vanilla clients can join servers running AlaLoot
- **License:** MIT
- **Languages:** English, Русский, Українська, 简体中文, Deutsch, Español, Français, Português (BR)

## How it works

| Action | Effect |
|---|---|
| Player breaks a block | Vanilla drop is replaced by a random item; chance = `min(player.xpLevel, 100) / 100`. +1 XP granted regardless of the roll. |
| Player kills a mob | Same chance formula. +10 XP granted regardless of the roll. |
| Boss kill (Ender Dragon, Wither, Warden, Elder Guardian) | Fixed legendary stack — always drops. |

Blocks are sorted into 6 categories (`COMMON`, `RARE`, `NETHER`, `END`, `MOB`, `BOSS`). Nether and End categories only fire in their respective dimensions. Each category can be toggled, blacklisted, or limited per-mod from in-game commands.

## Commands

All commands require operator permission (`COMMANDS_GAMEMASTER`).

```
/alaloot reload          Reload the server config without a restart.
/alaloot stats           Show item-pool stats.
/alaloot info            Show the AlaLoot category of the block you are standing on.
/alaloot toggle <area> <on|off>   Toggle drops for blocks / mobs / bosses.
/alaloot blacklist <add|remove> <item|mod> <id>   Manage the blacklist live.
```

## Configuration

Server config: `config/alaloot-server.toml` (auto-generated on first world load). All mechanics — drop chance multipliers, category gates, blacklists, boss reward sizes — are editable there. Use `/alaloot reload` to apply changes without restarting.

## Installation

1. Install [NeoForge 26.1.2.28-beta](https://neoforged.net) for Minecraft 1.21.10.
2. Download the latest `alaloot-26.1.2-<version>.jar` from the [Releases](../../releases) page or [CurseForge](https://www.curseforge.com/minecraft/mc-mods/alaloot).
3. Drop the jar into `.minecraft/mods/` (client) or `<server>/mods/` (server).
4. Launch.

## Building from source

Requires JDK 25 (bundled with the Minecraft 1.21.10 launcher).

```bash
git clone https://github.com/Ma3auka/AlaLoot.git
cd AlaLoot
./gradlew build
# Output: build/libs/alaloot-26.1.2-<version>.jar
```

## Issues / contributing

Bug reports and feature requests: [GitHub Issues](../../issues). Pull requests welcome — please target `main` and describe the change.

## License

[MIT](LICENSE) © Artem Pavelko (Ma3auka).
