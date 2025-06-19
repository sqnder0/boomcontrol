# 💥 BoomControl

**BoomControl** is a lightweight and focused plugin designed specifically for **SMP servers** that want to **limit or nerf Crystal PvP**. It offers fine-tuned control over End Crystal and Respawn Anchor explosions to prevent unfair advantages or griefing, while keeping gameplay fun and balanced.

- - -

## 🎯 Why BoomControl?

On many SMPs, End Crystals and Respawn Anchors are used in PvP for massive burst damage. BoomControl gives you **full control** over how these items behave:

*   Disable or weaken crystal PvP
*   Prevent griefing through explosion block damage
*   Make explosions only damage the attacker
*   Keep your SMP fun and fair without banning crystals entirely

- - -

## 🔧 Features

*   ✅ Toggle the plugin globally
*   🔄 Reload settings on the fly with `/boomcontrol reload`
*   🧨 Full control over **End Crystal** behavior
*   🌋 Full control over **Respawn Anchors**
*   ❌ Block explosion grief (block breaking & fire)
*   📢 Custom denial messages for blocked actions

- - -

## 🧨 Crystal Settings

```
crystal:
  enabled: true           # REQUIRES server restart to change
  mode: "self"            # Options: "disabled", "self", "vanilla"
  break-blocks: false     # Prevents block damage
  start-fire: false       # Stops fire on explosion
  show-deny-message: true # Inform players when blocked
```

### Modes:

*   `"disabled"` – Crystals won't explode at all.
*   `"self"` – Only the player who triggered the explosion takes damage.
*   `"vanilla"` – Default Minecraft behavior.

- - -

## 🌋 Respawn Anchor Settings

```
anchor:
  enabled: true
  mode: "self"            # Same options as crystal
  break-blocks: true
  start-fire: true
  show-deny-message: true
```

Perfect for controlling Nether-based PvP or creative mechanics using anchors.

- - -

## 🔄 Command

```
/boomcontrol reload
```

Reloads the plugin configuration **without restarting the server**.  
⚠️ Note: The `enabled` toggle and `crystal.enabled` **require a restart**.

- - -

## 🗨️ Custom Messages

```
messages:
  crystal-denied: "&cCrystal PvP is disabled on this server."
  anchor-denied: "&cRespawn anchor damage is not allowed."
```

Use color codes (`&c`, `&7`, etc.) to style your messages.

- - -

## 🛑 World Blacklist

BoomControl now supports **disabling the plugin entirely in specific worlds** via a blacklist configured in `settings.yml`. This is useful if you want BoomControl to be completely inactive in certain worlds (like the Nether, End, or lobby worlds), regardless of region flags or other settings.

### Example configuration:

```yaml
blacklisted_worlds:
- world_nether
- world_the_end
  ```

### How it works:

- If a world is listed in `blacklisted_worlds`, BoomControl will be **fully disabled in that world**.
- This means that any region-based controls or WorldGuard flags are ignored for that world.
- Vanilla Minecraft explosion behavior applies in those worlds instead.

### Usage tips:

- Leave the list empty to enable BoomControl in *all* worlds.
- Add worlds where you want BoomControl disabled completely, such as lobby or event worlds.
- Combine this blacklist with WorldGuard region flags to achieve fine-grained control elsewhere.

---

## 🏳️ WorldGuard Integration (Optional)

BoomControl integrates with [WorldGuard](https://enginehub.org/worldguard/) to support **region-based control** using a custom flag.

### 🔖 `boomcontrol` Flag

If WorldGuard is installed, BoomControl registers a custom region flag:

```
boomcontrol
```

This flag allows you to **disable BoomControl in specific regions**, even if the plugin is globally enabled.

### 🧰 How It Works

- If the flag is set to `DENY` in a region, **BoomControl will be disabled there completely**.  
  Explosion behavior in that region will follow normal Minecraft mechanics.
- If the flag is **not set** or explicitly `ALLOW`, BoomControl will apply as configured in `settings.yml`.

### 🧪 Example

To disable BoomControl inside a region:

```bash
/rg flag spawn boomcontrol deny
```

To enable it again:

```bash
/rg flag spawn boomcontrol allow
```

To remove the flag entirely:

```bash
/rg flag spawn -r boomcontrol
```

### ❗ Default Behavior

- If the flag is **not set**, BoomControl assumes `ALLOW`.
- This means BoomControl is **active everywhere** by default, unless explicitly denied via region flag.

---

## 🧠 Perfect For

*   🏰 SMP servers
*   🔒 Anti-grief setups
*   ⚔️ PvP practice worlds with restricted meta
*   🛡️ Survival servers with balanced combat

- - -

## ✅ Lightweight & Reliable

No bloat. No complex dependencies. Just simple, effective explosion control.
Make your SMP safer and more balanced with **BoomControl**.

## 👷🏻 For Developers

Developer documentation is currently under development and will be available soon.  
In the meantime, feel free to explore the source code and submit issues or pull requests on GitHub.

- - -

## 📜 License

**BoomControl** is released under a custom license with the following terms:

- You may use and modify BoomControl freely for running Minecraft servers, including servers that generate revenue via ads, donations, or other non-sale means.
- You **may NOT** sell, sublicense, or redistribute the original unmodified source code or compiled plugin binaries for commercial purposes.
- Redistribution of the original unmodified plugin (source or binary) is strictly prohibited.
- You may redistribute **modified versions** only if:
  - You retain this license and copyright notice.
  - You give clear credit to the original author (sqnder0).
  - You clearly state what modifications you made.
  - The redistributed plugin’s name includes “BoomControl” to preserve association with the original.
  - Every redistribution includes a link to the official download page:  
    https://www.spigotmc.org/resources/boomcontrol.7921/
- Server owners are free to run the plugin on their servers regardless of whether their servers generate income.
- No permission is granted to use the author’s name or trademarks beyond attribution.
- The software is provided “as is” without warranty.

If you want to use BoomControl commercially beyond these terms, please contact the author for a commercial license.

---

Thank you for respecting these terms and supporting ongoing development.
