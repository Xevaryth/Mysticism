# Developer API

The supported package is:

```text
com.xevaryth.mysticism.api
```

Use mana mutations on the logical server.

```java
import com.xevaryth.mysticism.api.ManaApi;

int current = ManaApi.getMana(player);
int maximum = ManaApi.getMaxMana(player);

if (ManaApi.tryConsumeMana(player, 5)) {
    castSpell(player);
}

ManaApi.addMana(player, 10);
```

## ManaApi

| Method | Purpose |
| --- | --- |
| `getMana(player)` | Read current mana. |
| `getMaxMana(player)` | Read effective maximum mana. |
| `getManaRegenPerSecond(player)` | Read effective regeneration. |
| `setMana(player, amount)` | Set current mana within its bounds. |
| `addMana(player, amount)` | Add or remove current mana. |
| `tryConsumeMana(player, amount)` | Spend the full amount or change nothing. |
| `fillMana(player)` | Fill current mana. |
| `emptyMana(player)` | Empty current mana. |

`MysticismApi` exposes registry IDs and handles for `max_mana` and
`mana_regen_per_second`.
