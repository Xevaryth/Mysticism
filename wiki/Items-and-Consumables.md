# Items and consumables

The library does not currently register items.

## Equipment bonuses

Equipment from another mod can apply modifiers to:

- `simper_mana_library:max_mana`
- `simper_mana_library:mana_regen_per_second`

The attribute handles are available from `SimperManaLibraryApi`.

## Instant mana restoration

A consumable can restore mana from its server-side use handler:

```java
ManaApi.addMana(player, amount);
```

The resulting value is clamped to the player's maximum mana.
