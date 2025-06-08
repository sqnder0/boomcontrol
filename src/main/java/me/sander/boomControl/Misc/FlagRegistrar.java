package me.sander.boomControl.Misc;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.plugin.java.JavaPlugin;

public class FlagRegistrar {
    public static StateFlag BOOM_CONTROL;

    public static void register(JavaPlugin plugin) {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

        try {
            // Create and register the flag
            StateFlag flag = new StateFlag("boomcontrol", true);
            registry.register(flag);
            BOOM_CONTROL = flag;
            plugin.getLogger().info("Flag 'boomcontrol' registered successfully.");
        } catch (FlagConflictException e) {
            // If already registered, reuse existing
            Flag<?> existing = registry.get("boomcontrol");
            if (existing instanceof StateFlag) {
                BOOM_CONTROL = (StateFlag) existing;
                plugin.getLogger().warning("'boomcontrol' flag already exists, using existing flag.");
            } else {
                plugin.getLogger().severe("Conflict with 'boomcontrol' flag and it's not a StateFlag.");
            }
        }
    }
}
