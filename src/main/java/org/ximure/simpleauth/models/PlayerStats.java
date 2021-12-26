package org.ximure.simpleauth.models;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class PlayerStats {
    private final Vector loginVelocity;
    private final GameMode loginGamemode;
    private final Boolean isDead;
    private final Location loginLocation;

    public PlayerStats(Vector loginVelocity, GameMode loginGamemode, Boolean isDead, Location loginLocation) {
        this.loginVelocity = loginVelocity;
        this.loginGamemode = loginGamemode;
        this.isDead = isDead;
        this.loginLocation = loginLocation;
    }

    public Vector getLoginVelocity() {
        return this.loginVelocity;
    }

    public GameMode getLoginGamemode() {
        return this.loginGamemode;
    }

    public Boolean isDead() {
        return this.isDead;
    }

    public Location getLoginLocation() {
        return this.loginLocation;
    }
}
