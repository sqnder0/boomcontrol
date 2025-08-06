package me.sander.boomControl.Cache;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class Settings {

    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration config;

    public Settings(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setup() {
        configFile = new File(plugin.getDataFolder(), "settings.yml");

        // Save default config if not present
        if (!configFile.exists()) {
            plugin.saveResource("settings.yml", false);
            plugin.getLogger().info("Created default settings.yml");
        }

        // Load config from file
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    // Reload config from disk
    public void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    // Save current config to disk
    public void save() {
        try {
            config.save(configFile);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to save settings.yml: " + e.getMessage());
        }
    }

    // Getter for raw config if needed
    public FileConfiguration getConfig() {
        return config;
    }

    // Example convenience getters for specific keys
    public boolean isPluginEnabled() {
        return config.getBoolean("enabled", true);
    }

    public boolean isCrystalEnabled() {
        return config.getBoolean("crystal.enabled", true);
    }

    public String getCrystalMode() {
        return config.getString("crystal.mode", "disabled");
    }

    public boolean canCrystalBreakBlocks() {
        return config.getBoolean("crystal.break-blocks", false);
    }

    public boolean canCrystalStartFire() {
        return config.getBoolean("crystal.start-fire", false);
    }

    public boolean showCrystalDenyMessage() {
        return config.getBoolean("crystal.show-deny-message", true);
    }

    public String getCrystalDenyMessage() {
        return config.getString("messages.crystal-denied", "&cPlease define a crystal deny message!");
    }

    // Similarly for anchors
    public boolean isAnchorEnabled() {
        return config.getBoolean("anchor.enabled", true);
    }

    public String getAnchorMode() {
        return config.getString("anchor.mode", "vanilla");
    }

    public boolean canAnchorBreakBlocks() {
        return config.getBoolean("anchor.break-blocks", false);
    }

    public boolean canAnchorStartFire() {
        return config.getBoolean("anchor.start-fire", false);
    }

    public boolean showAnchorDenyMessage() {
        return config.getBoolean("anchor.show-deny-message", true);
    }

    public Component getAnchorDenyMessageComponent() {
        String raw = config.getString("messages.anchor-denied", "&cPlease define an anchor deny message!");
        return LegacyComponentSerializer.legacyAmpersand().deserialize(raw);
    }

    public boolean isWorldEnabled(World world) {
        List<String> world_list = config.getStringList("blacklisted_worlds");
        return !world_list.contains(world.getName());
    }

    public boolean isRegionEnabled(Location location, Player player) {
            return true;
    }

    public boolean isBoomControlEnabled(Location location, Player player) {
        if (isWorldEnabled(location.getWorld())) return isRegionEnabled(location, player);
        return false;
    }
}
