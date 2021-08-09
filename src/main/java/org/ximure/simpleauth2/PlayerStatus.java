package org.ximure.simpleauth2;

import org.bukkit.GameMode;

import java.util.*;

public class PlayerStatus {
    final Map<UUID, GameMode> gameModeStatus = new HashMap<>();
    final List<UUID> loginStatus = new ArrayList<>();

    public void setOnline(UUID playerUUID) {
        loginStatus.add(playerUUID);
    }

    public void setOffline(UUID playerUUID) {
        loginStatus.remove(playerUUID);
    }

    public Boolean isOnline(UUID playerUUID) {
        return loginStatus.contains(playerUUID);
    }

    public void setGameMode(UUID playerUUID, GameMode gameMode) {
        gameModeStatus.put(playerUUID, gameMode);
    }

    public GameMode getGameMode(UUID playerUUID) {
        return gameModeStatus.get(playerUUID);
    }
}
