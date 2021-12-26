package org.ximure.simpleauth.auth;

import org.ximure.simpleauth.models.PlayerStats;

import java.util.*;

/**
 * All of this are self-explanatory
 */
public class PlayerStatsManager {
    private final Map<UUID, PlayerStats> playerStats = new HashMap<>();
    private final Set<UUID> loginStatus = new HashSet<>();

    public void addPlayerStats(UUID playerUUID, PlayerStats playerStats) {
        this.playerStats.put(playerUUID, playerStats);
    }

    public PlayerStats getPlayerStats(UUID playerUUID) {
        return playerStats.get(playerUUID);
    }

    public void removePlayerStats(UUID playerUUID) {
        playerStats.remove(playerUUID);
    }

    public void setOnline(UUID playerUUID) {
        loginStatus.add(playerUUID);
    }

    public void setOffline(UUID playerUUID) {
        loginStatus.remove(playerUUID);
    }

    public boolean isOnline(UUID playerUUID) {
        return loginStatus.contains(playerUUID);
    }

    public void removeAllPlayerStats() {
        playerStats.clear();
    }

    public void removeAllPlayerLogins() {
        loginStatus.clear();
    }
}
