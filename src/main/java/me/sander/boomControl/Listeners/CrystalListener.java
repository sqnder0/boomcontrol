package me.sander.boomControl.Listeners;

import me.sander.boomControl.BoomControl;
import me.sander.boomControl.Cache.ExplosionData;
import me.sander.boomControl.Cache.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CrystalListener implements Listener {

    private final List<ExplosionData> activeExplosions = new ArrayList<>();

    private final BoomControl plugin;
    private final Settings settings;

    public CrystalListener() {
        this.plugin = BoomControl.getInstance();
        this.settings = BoomControl.getInstance().getSettings();
    }

    //Notify the player if crystals are disabled
    private void notifyPlayer(Player player) { if (settings.showCrystalDenyMessage()) player.sendMessage(settings.getCrystalDenyMessage()); }

    @EventHandler
    public void onCrystalDamage(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.END_CRYSTAL) return;

        Entity crystal = event.getEntity();

        Entity damager = event.getDamager();

        String mode = plugin.getSettings().getCrystalMode(); // e.g. "disabled", "self", "vanilla"

        Player player = getDamagerPlayer(damager);
        if (!settings.isBoomControlEnabled(crystal.getLocation(), player)) return;

        // Disabled mode: cancel all damage and notify player
        if ("disabled".equalsIgnoreCase(mode)) {


            if (player != null) notifyPlayer(player); //Notify player

            crystal.remove(); // Delete the crystal after being hit

            event.setCancelled(true); // Cancel explosion caused by any entities
        }

        // Self mode: only the player who broke crystal can get damage; others are immune
        if ("self".equalsIgnoreCase(mode)) {
            if (player == null) {
                event.setCancelled(true);
                return;
            }

            Location explosionLocation = crystal.getLocation();

            World world = explosionLocation.getWorld();

            if (world != null) {
                addExplosionData(new ExplosionData(explosionLocation, player));
                world.createExplosion(explosionLocation, 6.0f, settings.canCrystalStartFire(), settings.canCrystalBreakBlocks());
            }

            crystal.remove();

            // For now, cancel crystal damage (explosion) but handle damage to players in another listener
            event.setCancelled(true);
        }

        // Vanilla mode or unknown mode - allow everything (no cancellation)
        // But if breakBlocks is false, cancel block damage events elsewhere (not here)

        // No message needed in vanilla mode
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!"self".equalsIgnoreCase(plugin.getSettings().getCrystalMode())) return;

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


    // Helper method: get Player from damager Entity or projectile shooter
    private Player getDamagerPlayer(Entity damager) {
        if (damager instanceof Player player) {
            return player;
        }
        if (damager instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Player player) {
                return player;
            }
        }
        return null;
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
            direction = new Vector(0, 1, 0);  // bijvoorbeeld omhoog
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
