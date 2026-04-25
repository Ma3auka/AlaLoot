# Changelog

All notable changes to AlaLoot will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

> **Single source of truth:** `AlaLoot/gradle.properties` → `mod_version`.
> The same value is rendered into the jar's `neoforge.mods.toml` AND shown in
> Minecraft's in-game **Mods** screen. Do NOT bump versions in two places —
> always edit `gradle.properties`, then `./gradlew build`.

## [Unreleased]

## [1.2.0] — 2026-04-25

### Added
- Random vanilla loot replacement on player block break and mob kill.
- Drop chance tied to player XP level (1 level = 1%, capped at 100%).
- Boss loot for Ender Dragon, Wither, Warden, Elder Guardian (fixed legendary stacks).
- 6 block categories (Common / Rare / Nether / End / Mob / Boss) with NETHER/END dimension gates.
- Per-mod and per-item blacklists, in-game configurable.
- Brigadier admin commands: `/alaloot reload | stats | info | toggle | blacklist`.
- Server-side `ModConfigSpec` config (no client install required).
- Localizations: English, Russian, Ukrainian, Simplified Chinese, German, Spanish, French, Brazilian Portuguese.
- XP rewards: +1 XP per block, +10 XP per mob (granted regardless of drop roll).

### Metadata polish (CurseForge release prep)
- Switched license from "All Rights Reserved" to MIT.
- Added `displayURL`, `issueTrackerURL`, `updateJSONURL`, `logoFile`, `credits` to `neoforge.mods.toml`.
- Bundled 256×256 `logo.png` for the in-game Mods screen.
- Jar artifact renamed to `alaloot-26.1.2-1.2.0.jar` (CurseForge convention).

[Unreleased]: https://github.com/Ma3auka/AlaLoot/compare/v1.2.0...HEAD
[1.2.0]: https://github.com/Ma3auka/AlaLoot/releases/tag/v1.2.0
