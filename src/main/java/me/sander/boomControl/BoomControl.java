package me.sander.boomControl;

import me.sander.boomControl.Cache.Settings;
import me.sander.boomControl.Commands.Reload;
import me.sander.boomControl.Listeners.AnchorListener;
import me.sander.boomControl.Listeners.CrystalListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BoomControl extends JavaPlugin {

    private Settings settings;

    private static BoomControl plugin;

    @Override
    public void onEnable() {
        plugin = this;
        settings = new Settings(this);
        settings.setup();

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
        getCommand("boomcontrol").setExecutor(new Reload());
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
