package org.ximure.simpleauth.auth;

import org.bukkit.GameMode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthManager extends DatabaseDAO {
    private final Map<UUID, GameMode> playerStatus = new HashMap<>();

    public GameMode getGamemodeBeforeLogin(UUID playerUUID) {
        return playerStatus.get(playerUUID);
    }

    public void setGamemodeBeforeLogin(UUID playerUUID, GameMode playerGamemode) {
        playerStatus.put(playerUUID, playerGamemode);
    }

    public void setOnline(UUID playerUUID, GameMode playerGamemode) {
        playerStatus.put(playerUUID, playerGamemode);
    }

    public void setOffline(UUID playerUUID) {
        playerStatus.remove(playerUUID);
    }

    public boolean isOnline(UUID playerUUID) {
        return playerStatus.containsKey(playerUUID);
    }
}
