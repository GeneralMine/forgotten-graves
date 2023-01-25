## 1.19-3.0.0

- New: Updated to Minecraft v1.19.
- New: Added commands to manipulate the client and server configurations.
- New: Graves can now "sink" in different mediums (Air/Water/Lava). Read about that [here](https://github.com/ginsm/forgotten-graves/wiki/Graves#q-why-does-my-grave-sink-when-i-die-in-the-air-or-water).
- Update: Some configuration option names have been reworked.
- Update: Some configuration option values have been reworked.
- Update: The wiki has been updated in its entirety.
- Fix: Your inventory should no longer drop when retrieving a game with `dropType` set to `"DROP"`. Only the items in the grave will drop.
- Fix: There was an edge case where armor could sometimes be duplicated. This has been fixed.
- Fix: Graves should no longer lose GraveEntity data on the client; this was occurring when you didn't have permission to break the grave.