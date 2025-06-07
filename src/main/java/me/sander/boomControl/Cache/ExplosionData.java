package me.sander.boomControl.Cache;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ExplosionData {
    private final Location location;
    private final Player owner;

    public ExplosionData(Location location, Player owner) {
        this.location = location;
        this.owner = owner;
    }

    public Location getLocation() { return location; }
    public Player getOwner() { return owner; }
}
