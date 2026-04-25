# Changelog

All notable changes to AlaLoot will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

> **Single source of truth:** `AlaLoot/gradle.properties` → `mod_version`.
> The same value is rendered into the jar's `neoforge.mods.toml` AND shown in
> Minecraft's in-game **Mods** screen. Do NOT bump versions in two places —
> always edit `gradle.properties`, then `./gradlew build`.

## [Unreleased]

## [1.3.0] — 2026-04-25

### Added
- Curse Roll: 5% passive chance on mob kill to drop a random damaged tool (wooden through netherite, 1 durability remaining) when the XP-based loot roll fails.

### Changed
- Mob kill XP reward increased from 10 to 30 points per kill.
- `/alaloot stats` now shows the Curse Roll probability.

### Fixed
- Config screen displayed raw `alaloot.configuration.*` translation keys — added readable labels for all sections and entries across all 8 locales (en, ru, de, es, fr, pt, uk, zh).

### Internal
- Remove dead hierarchical translation keys from lang files.

## [1.2.4] — 2026-04-25

### Fixed
- Remove dead hierarchical translation keys from all 8 lang files (leftover from v1.2.1 first-pass attempt).

### Changed
- Add Curse Roll probability line to `/alaloot stats` output.

## [1.2.3] — 2026-04-25

### Added
- Add Curse Roll: 5% passive chance on mob kill to drop a random tool (wooden–netherite) at 1 durability when the XP-based loot roll fails.

## [1.2.2] — 2026-04-25

### Fixed
- Config screen entries now display translated labels (flat `alaloot.configuration.*` key format) for all 8 locales.

## [1.2.1] — 2026-04-25

### Changed
- Increase mob kill XP reward from 10 to 30 points per kill.

### Fixed
- Config screen displayed raw `alaloot.configuration.*` keys instead of readable text — added translations for all sections and entries across all 8 locales.

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
