package me.sander.boomControl.Misc;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public class WG7FlagRegistrar implements FlagRegistrar {
    public static StateFlag BOOM_CONTROL;

    @Override
    public void register(JavaPlugin plugin) {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

        try {
            StateFlag flag = new StateFlag("boomcontrol", true);
            registry.register(flag);
            BOOM_CONTROL = flag;
            plugin.getLogger().info("Flag 'boomcontrol' registered successfully.");
        } catch (FlagConflictException e) {
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
