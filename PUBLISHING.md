# Publishing materials (Modrinth / CurseForge)

> Internal file — not shipped in the jar. Drafts for the project pages.

## Name

**Orbital Railgun: Renewed**

- Modrinth slug: `orbital-railgun-renewed`
- Project type: Mod
- License: MIT
- Client & server: required on both sides
- Categories: equipment, game-mechanics, magic/technology (pick: **technology**, **equipment**)
- Dependencies: GeckoLib (required)

## Supported game versions (all verified in-game)

| Jar (branch) | Mark on Modrinth/CurseForge as | GeckoLib |
|---|---|---|
| `1.21.1` | 1.21.1 | 4.x (4.7+) |
| `1.21.4` | 1.21.4 | 4.x (4.8+) |
| `1.21.5` | 1.21.5 | 5.x |
| `1.21.8` | 1.21.8 | 5.x |
| `1.21.11` | 1.21.11 | 5.x |
| `26.1` | 26.1, 26.1.1, 26.1.2 (jar accepts the whole 26.1.x series) | 5.5+ |

Loader: **NeoForge** only. Environment: client **and** server.

## Summary (short description)

> Call down a devastating orbital strike. Unofficial NeoForge port of Mishkis' Orbital Railgun for modern Minecraft (1.21.1 → 26.1) — now with sound.

## Description (long, Markdown)

```markdown
# Orbital Railgun: Renewed

**An unofficial NeoForge port of [Orbital Railgun](https://modrinth.com/mod/orbital-railgun) by Mishkis** (MIT), brought to modern Minecraft versions — from 1.21.1 all the way to 26.1.

Craft the Orbital Railgun, hold right-click to aim through a full-screen targeting interface, and left-click to call down an orbital strike. Thirty seconds later, everything within a 24-block radius is vaporized down to bedrock.

## Features

- 🛰️ The original orbital strike experience: targeting shader, sky beam, massive crater
- 🔊 **Sound!** The original mod shipped silent — this port restores the equip, scope and strike sounds that were left unused in the original repository (strike audible up to 500 blocks)
- ⚡ Faster post-explosion screen effects: the vignette clears in ~8 s instead of ~20 s
- 🧊 No extra rendering libraries — Satin was replaced with the vanilla post-processing pipeline
- ✅ Sodium compatible

## Requirements

- NeoForge
- [GeckoLib](https://modrinth.com/mod/geckolib) for your Minecraft version (4.x up to MC 1.21.4, 5.x from MC 1.21.5)

## Credits

- **Mishkis** — the original mod ([GitHub](https://github.com/Mishkis/orbital-railgun))
- **Rayness** — NeoForge port ([GitHub](https://github.com/Rayness/orbital-railgun-renewed))

This is an **unofficial** port published under the original MIT license. Please report issues with this port [here](https://github.com/Rayness/orbital-railgun-renewed/issues) — do not bother the original author.
```

## Описание (RU, для галереи/комментария)

```markdown
Неофициальный порт мода Orbital Railgun (автор Mishkis) на NeoForge,
поддерживаются Minecraft 1.21.1 → 26.1.

Скрафти орбитальную рельсовую пушку, прицелься через полноэкранный интерфейс
наведения (ПКМ) и вызови орбитальный удар (ЛКМ): через 30 секунд всё в радиусе
24 блоков испаряется до бедрока.

Отличия от оригинала: добавлены звуки (в оригинале их не было), ускорено
затухание экранных эффектов после взрыва, заменена библиотека Satin на
ванильный пост-процессинг. Требуется GeckoLib. Совместим с Sodium.
```

## Version-file metadata (per upload)

- Version number: `1.2.0+mc<mcver>` (matches the jar name `orbital_railgun-1.2.0+mc<mcver>.jar`)
- Channel: Release
- Loader: NeoForge
- Dependency: GeckoLib — required

Changelog text for the first public release (same for every version file):

```markdown
First public release of the NeoForge port.

- Full port of Orbital Railgun 1.1 (Fabric 1.20.1) to NeoForge
- Restored equip / scope / strike sounds (unused in the original)
- Faster post-explosion screen-effect fade
- Satin replaced with the vanilla post-processing pipeline (no extra rendering deps)
```

## Checklist before publishing

- [ ] Create Modrinth project `orbital-railgun-renewed` (user account)
- [ ] Create CurseForge project (user account)
- [ ] Icon: reuse `assets/orbital_railgun/icon.png` (original MIT asset) or draw a new one
- [ ] Gallery: screenshots of aiming HUD, beam, crater
- [ ] Upload `orbital_railgun-1.2.0+mc<mcver>.jar` per game version (six files, see table above)
- [ ] Link original mod + GitHub repo of the port
- [ ] Mark GeckoLib as required dependency
