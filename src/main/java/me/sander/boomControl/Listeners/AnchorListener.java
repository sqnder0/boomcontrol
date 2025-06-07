package me.sander.boomControl.Listeners;

import me.sander.boomControl.BoomControl;
import me.sander.boomControl.Cache.ExplosionData;
import me.sander.boomControl.Cache.Settings;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class AnchorListener  implements Listener {

    private final BoomControl plugin;
    private final Settings settings;

    private final List<ExplosionData> activeExplosions = new ArrayList<>();

    private static final int MAX_CHARGES = 4;

    //Notify the player if crystals are disabled
    private void notifyPlayer(Player player) { if (settings.showAnchorDenyMessage()) player.sendMessage(settings.getAnchorDenyMessage()); }

    public AnchorListener() {
        this.plugin = BoomControl.getInstance();
        this.settings = plugin.getSettings();
    }

    @EventHandler
    public void onRespawnAnchorCharge(PlayerInteractEvent event) {
        // Only handle right-click on blocks
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.RESPAWN_ANCHOR) return;

        Player player = event.getPlayer();

        String mode = settings.getAnchorMode(); // e.g. "disabled", "self", "vanilla"

        ItemStack item = event.getItem();

        // Only handle if player holds glowstone dust (charging item)
        if (item == null || item.getType() != Material.GLOWSTONE_DUST) return;

        RespawnAnchor anchorData = (RespawnAnchor) block.getBlockData();
        int currentCharge = anchorData.getCharges();

        if (currentCharge + 1 >= anchorData.getMaximumCharges()) {

            Location explosionLocation = block.getLocation();
            World world = explosionLocation.getWorld();

            if (world.getEnvironment() == World.Environment.NETHER ) {
                event.setCancelled(true);
                return;
            }

            if (player.getGameMode() != org.bukkit.GameMode.CREATIVE) {
                item.setAmount(item.getAmount() - 1);
                player.getInventory().setItemInMainHand(item);
            }

            if ( "disabled".equalsIgnoreCase(mode) ) {
                notifyPlayer(player);
                event.setCancelled(true);
                return;
            }

            if ( "self".equalsIgnoreCase(mode) ) {
                block.setType(Material.AIR); // Remove the respawn anchor

                if (world != null ) {
                    world.createExplosion(explosionLocation, 6.0f, settings.canAnchorStartFire(), settings.canAnchorBreakBlocks());
                    addExplosionData(new ExplosionData(explosionLocation, player));
                    // Send a message - customizable via settings if you want
                    notifyPlayer(player);
                    event.setCancelled(true);
                    return;
                }
            }

        }

        if (player.getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
            player.getInventory().setItemInMainHand(item);
        }

        anchorData.setCharges(currentCharge + 1);
        block.setBlockData(anchorData, true);

        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1f, 1f);
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!"self".equalsIgnoreCase(plugin.getSettings().getAnchorMode())) return;

        // Only handle explosion damage
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION &&
                event.getCause() != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            return;
        }

        Entity damaged = event.getEntity();

        // Check if this explosion is from a controlled crystal explosion
        for (ExplosionData data : activeExplosions) {
            if (damaged.getWorld().equals(data.getLocation().getWorld()) &&
                    damaged.getLocation().distanceSquared(data.getLocation()) <= 16 * 16) { // 16 blocks radius squared

                // If damaged is a player and is the owner, allow damage
                if (damaged instanceof Player damagedPlayer) {
                    if (damagedPlayer.equals(data.getOwner())) {
                        // Owner gets damage and knockback, do nothing
                        return;
                    } else {
                        // Other players: cancel damage, apply knockback manually
                        event.setCancelled(true);
                        applyKnockback(damaged, data.getLocation());
                        return;
                    }
                } else {
                    // Non-player: cancel damage, apply knockback manually
                    event.setCancelled(true);
                    applyKnockback(damaged, data.getLocation());
                    return;
                }
            }
        }
    }

    private void addExplosionData(ExplosionData data) {
        activeExplosions.add(data);

        // Schedule removal after 5 seconds (100 ticks)
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            activeExplosions.remove(data);
        }, 100L);
    }

    private void applyKnockback(Entity entity, Location explosionLocation) {
        // Vector from explosion to entity
        var direction = entity.getLocation().toVector().subtract(explosionLocation.toVector());

        if (direction.lengthSquared() == 0 || !isFinite(direction)) {
            // fallback velocity
            direction = new Vector(0, 1, 0);  // Up if in center of block
        } else {
            direction = direction.normalize();
        }

        // Adjust knockback strength here
        double knockbackStrength = 1.1;

        var velocity = direction.multiply(knockbackStrength);

        // Set Y velocity a bit higher to simulate explosion lift
        velocity.setY(0.5);

        entity.setVelocity(velocity);
    }

    public boolean isFinite(Vector v) {
        return Double.isFinite(v.getX()) && Double.isFinite(v.getY()) && Double.isFinite(v.getZ());
    }
}
