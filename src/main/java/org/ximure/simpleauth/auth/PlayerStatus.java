package org.ximure.simpleauth.auth;

import org.bukkit.GameMode;

import java.util.*;

public class PlayerStatus {
    final Map<UUID, GameMode> gameModeStatus = new HashMap<>();
    final List<UUID> loginStatus = new ArrayList<>();

    /**
     * Adds player UUID to list. If player in it - he's login status equals to "online"
     * @param playerUUID    UUID which to add to the list
     */
    public void setOnline(UUID playerUUID) {
        loginStatus.add(playerUUID);
    }

    /**
     * Removes player UUID from the list. If player not in it - he's login status equals to "offline"
     * @param playerUUID    UUID which to remove from the list
     */
    public void setOffline(UUID playerUUID) {
        loginStatus.remove(playerUUID);
    }

    /**
     * This method checks if player in the list
     * @param playerUUID    UUID to check if it is in the list
     * @return              true if player in the list (online), false if not (offline)
     */
    public Boolean isOnline(UUID playerUUID) {
        return loginStatus.contains(playerUUID);
    }

    /**
     * This method can store player gamemode to be restored later
     * @param playerUUID    UUID key to save gamemode
     * @param gameMode      gamemode value to save gamemode
     */
    public void saveGameMode(UUID playerUUID, GameMode gameMode) {
        gameModeStatus.put(playerUUID, gameMode);
    }

    /**
     * This method can return method which has been saved via saveGameMode method
     * @param playerUUID    UUID key to retrieve gamemode
     * @return              gamemode which has been saved using saveGameMode
     */
    public GameMode restoreGameMode(UUID playerUUID) {
        return gameModeStatus.get(playerUUID);
    }
}
