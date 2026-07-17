# Items and consumables

The library does not currently register items.

## Equipment bonuses

Equipment from another mod can apply modifiers to:

- `mysticism:max_mana`
- `mysticism:mana_regen_per_second`

The attribute handles are available from `MysticismApi`.

## Sustained mana costs

An active item can apply a negative modifier to
`mysticism:mana_regen_per_second`. The normal regeneration tick then drains
mana at that rate until the modifier is removed. Positive and negative
modifiers combine, so an item draining `3` mana per second on a player
regenerating `1` mana per second produces a net drain of `2` mana per second.

Mana never falls below zero. The item remains responsible for disabling its
effect when the player can no longer pay its cost.

To test the behavior in game:

```mcfunction
/mana fill
/attribute @s mysticism:mana_regen_per_second base set -5
```

Restore the normal value afterward:

```mcfunction
/attribute @s mysticism:mana_regen_per_second base set 1
```

## Instant mana restoration

A consumable can restore mana from its server-side use handler:

```java
ManaApi.addMana(player, amount);
```

The resulting value is clamped to the player's maximum mana.

## Testing a mana cost

Use the operator command:

```mcfunction
/mana consume <amount>
```

The command uses `ManaApi.tryConsumeMana`. It removes the full amount when the
player can afford it. If the player has too little mana, it removes nothing.
