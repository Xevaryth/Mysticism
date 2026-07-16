# Getting started

Mysticism targets Minecraft 1.21.1, NeoForge 21.1, and Java 21.

## Players

Place the Mysticism jar in the `mods` folder. Servers and connecting
players both need the mod.

## Mod authors

Publish the library locally with:

```powershell
gradlew.bat publish
```

The Maven repository is written to `build/repo`.

```groovy
repositories {
    maven { url = uri('../Mysticism/build/repo') }
}

dependencies {
    implementation 'com.xevaryth.mysticism:mysticism:0.1.0'
}
```

Declare the mod dependency in `neoforge.mods.toml`:

```toml
[[dependencies.your_mod_id]]
modId="mysticism"
type="required"
versionRange="[0.1.0,)"
ordering="AFTER"
side="BOTH"
```
