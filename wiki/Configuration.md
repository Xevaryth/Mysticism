# Configuration

Server settings are stored in:

```text
<world>/serverconfig/simper_mana_library-server.toml
```

Use the [config generator](config-generator.html), or edit the file directly:

```toml
[mana]
startingMaxMana = 40
startingManaRegenPerSecond = 1.0
```

| Setting | Default | Range |
| --- | ---: | ---: |
| `startingMaxMana` | `40` | `0` to `9999` |
| `startingManaRegenPerSecond` | `1.0` | `0.0` to `1024.0` |

The starting values are assigned when a player's mana attributes are first
initialized.
