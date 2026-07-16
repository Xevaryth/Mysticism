# Server testing

## Single-player

A single-player world runs an integrated server. It can test normal mana
regeneration, saving, commands, configuration, and gameplay.

## Dedicated server

A dedicated server also checks that the mod can start without client-only
classes. Start the development server with:

```powershell
gradlew.bat runServer
```

The first run creates `run/eula.txt` and stops. Read the EULA, set `eula=true`
if you agree, then start the server again.

When the console reports `Done`, connect to `localhost` from a Minecraft 1.21.1
NeoForge client containing the same mod jar.
