# Mysticism

A vanilla-style magical resource foundation for Minecraft 1.21.1 on NeoForge.

## Target

- Minecraft `1.21.1`
- NeoForge `21.1.234`
- Java `21`
- Gradle `8.12.1`
- Required runtime dependency: `overflowingbars` `21.1.1+`

## Current features

- One visible mana row above hunger.
- Vanilla air bubbles render above mana.
- 9x9 star sheet at `assets/mysticism/textures/gui/mana_stars.png`.
- One star equals 4 mana.
- Ten stars equals 40 mana.
- Blue stars are the first bar.
- Purple stars are overflow bars.
- Empty stars render behind every slot.
- Mana is saved per player/world through a player attachment.
- Mana regenerates from the `mysticism:mana_regen_per_second` attribute.

## Commands

All commands currently require permission level 2.

```mcfunction
/mana
/mana get
/mana set <max>
/mana add <amount>
/mana fill
/mana empty
```

`/mana add <amount>` changes maximum mana. Positive values also grant the gained mana immediately so the HUD change is visible.

## Build

Install Java 21 and Gradle 8.12.1, then run:

```bash
gradle build
```

The jar will be created in:

```text
build/libs/
```

For local client testing, place dependency jars in `libs/`:

```text
libs/OverflowingBars-v21.1.1-1.21.1-NeoForge.jar
libs/PuzzlesLib-v21.1.52-1.21.1-NeoForge.jar
```

These jars are not redistributed in this repository.
