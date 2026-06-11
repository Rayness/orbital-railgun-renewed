# Orbital Railgun: Renewed

An **unofficial NeoForge port** of the [Orbital Railgun](https://modrinth.com/mod/orbital-railgun) mod by [Mishkis](https://github.com/Mishkis/orbital-railgun) (MIT).

Craft the Orbital Railgun, aim anywhere within 300 blocks, and call down a devastating orbital strike: a full-screen targeting shader, a beam from the sky, and a 24-block-radius crater carved down to bedrock.

## Versions

Each Minecraft version lives on its own git branch:

| Branch | Minecraft | Loader | Status |
|---|---|---|---|
| `1.21.1` | 1.21.1 | NeoForge 21.1 | ✅ done |
| `1.21.4` | 1.21.4 | NeoForge 21.4 | ✅ done |
| `1.21.5` | 1.21.5 | NeoForge 21.5 | ✅ done |
| `1.21.8` | 1.21.8 | NeoForge 21.8 | 🔜 planned |
| `1.21.11` | 1.21.11 | NeoForge 21.11 | 🔜 planned |
| `26.1` | 26.1 | NeoForge 26.1 | 🔜 planned |

The original mod is Fabric 1.20.1 only.

## Requirements

- NeoForge for the matching Minecraft version
- [GeckoLib](https://modrinth.com/mod/geckolib) 4.7+

## Differences from the original

- **Ported to NeoForge / modern Minecraft.** The Fabric-only [Satin](https://github.com/Ladysnake/Satin) shader library was replaced with the vanilla `PostChain` pipeline (custom depth sampler bound manually), so the port has no extra rendering dependencies.
- **Sounds!** The original shipped silent — its iconic sounds existed only in the repo history and were never wired up. This port restores them: equip, scope-on, and the full orbital strike sound (audible up to 500 blocks, positional at the strike site).
- **Faster post-explosion fade.** The screen darkening/vignette clears in ~8 s after the blast (originally ~20 s) and world brightness recovers in ~11 s (originally ~30 s). The light pillar lingers for 25 s. The pre-explosion build-up and beam are untouched.

## Building

```
./gradlew build
```

The jar lands in `build/libs/`. Requires JDK 21 (JDK 22+ for the 26.1 branch if required by that toolchain).

## Credits & license

- **[Mishkis](https://github.com/Mishkis)** — the original Orbital Railgun mod (MIT)
- **Rayness** — NeoForge port, sound restoration, shader timing tweaks

MIT License — see [LICENSE](LICENSE). This is an unofficial port; please report port-specific bugs here, not to the original author.

---

## Русский

Неофициальный порт мода **Orbital Railgun** (автор — Mishkis, лицензия MIT) на NeoForge и современные версии Minecraft. Оригинал существует только для Fabric 1.20.1.

Отличия от оригинала: заменена Fabric-библиотека Satin на ванильный `PostChain`; добавлены звуки (в оригинале их не было — файлы восстановлены из истории репозитория автора); ускорено затухание экранных эффектов после взрыва (виньетка ~8 с вместо 20, яркость ~11 с вместо 30, столб света 25 с).

Зависимости: NeoForge соответствующей версии + GeckoLib 4.7+.
