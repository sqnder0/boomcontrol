package me.sander.boomControl;

import me.sander.boomControl.Cache.Settings;
import me.sander.boomControl.Commands.Reload;
import me.sander.boomControl.Listeners.AnchorListener;
import me.sander.boomControl.Listeners.CrystalListener;
import me.sander.boomControl.Misc.FlagRegistrar;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class BoomControl extends JavaPlugin {

    private Settings settings;

    private static BoomControl plugin;

    @Override
    public void onLoad() {
        settings = new Settings(this);
        settings.setup();

        if (!settings.isPluginEnabled()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Register the worldguard flag
        Plugin worldGuard = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (worldGuard != null) {
            getLogger().info("WorldGuard found, registering boomcontrol flag...");
            FlagRegistrar.register(this);
        } else {
            getLogger().info("WorldGuard was not found, continuing without additional flags.");
        }
    }

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info("BoomControl v" + getDescription().getVersion() + " enabled.");

        // Register listeners only if enabled in config
        if (settings.isCrystalEnabled()) {
            getServer().getPluginManager().registerEvents(new CrystalListener(), this);
            getLogger().info("Registered CrystalListener");
        } else {
            getLogger().info("CrystalListener NOT registered because crystals are disabled");
        }

        if (settings.isAnchorEnabled()) {
            getServer().getPluginManager().registerEvents(new AnchorListener(), this);
            getLogger().info("Registered AnchorListener");
        } else {
            getLogger().info("AnchorListener NOT registered because anchors are disabled");
        }

        // Initialize commands
        Objects.requireNonNull(getCommand("boomcontrol")).setExecutor(new Reload());
    }

    @Override
    public void onDisable() {
        getLogger().info("BoomControl disabled.");
    }

    public Settings getSettings() {
        return settings;
    }

    public static BoomControl getInstance() { return plugin; }
}
