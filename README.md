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

## 🧠 Perfect For

*   🏰 SMP servers
*   🔒 Anti-grief setups
*   ⚔️ PvP practice worlds with restricted meta
*   🛡️ Survival servers with balanced combat

- - -

## ✅ Lightweight & Reliable

No bloat. No complex dependencies. Just simple, effective explosion control.

- - -

Make your SMP safer and more balanced with **BoomControl**.

## 👷🏻 For Developers

Developer documentation is currently under development and will be available soon.  
In the meantime, feel free to explore the source code and submit issues or pull requests on GitHub.
