package me.sander.boomControl.Commands;

import me.sander.boomControl.BoomControl;
import me.sander.boomControl.Cache.Settings;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Reload implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length != 1 ) return false;

        if (args[0].equalsIgnoreCase("reload")) {
            BoomControl.getInstance().getSettings().reload();

            if ( commandSender instanceof Player player) {
                player.sendMessage(Component.text("Settings reloaded!"));
            } else {
                BoomControl.getInstance().getLogger().info("Settings reloaded!");
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length <= 1) {
            return List.of("reload");
        }

        return List.of();
    }
}
