package org.ximure.simpleauth.auth;

import org.bukkit.GameMode;
import org.bukkit.Location;

import java.util.*;

public class PlayerStatus {
    final Map<UUID, Location> playerLoginLocation = new HashMap<>();
    final Map<UUID, GameMode> gameModeStatus = new HashMap<>();
    final List<UUID> loginStatus = new ArrayList<>();

    /**
     * Adds player login location,so we'll teleport him back later (fix for very laggy players)
     * @param playerUUID        UUID which player location to store
     * @param playerLocation    Location of a player after joining a server
     */
    public void setPlayerLoginLocation(UUID playerUUID, Location playerLocation) {
        playerLoginLocation.put(playerUUID, playerLocation);
    }

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

    public Location restorePlayerLocation(UUID playerUUID) {
        return playerLoginLocation.get(playerUUID);
    }

    public void removePlayerLocation(UUID playerUUID) {
        playerLoginLocation.remove(playerUUID);
    }

    /**
     * This method can return method which has been saved via saveGameMode method
     * @param playerUUID    UUID key to retrieve gamemode
     * @return              gamemode which has been saved using saveGameMode
     */
    public GameMode restoreGameMode(UUID playerUUID) {
        return gameModeStatus.get(playerUUID);
    }

    /**
     * clears list with saved gamemodes
     */
    public void clearGameModes() {
        gameModeStatus.clear();
    }

    /**
     * clears list with saves online statuses
     */
    public void clearOnlineStatuses() {
        loginStatus.clear();
    }
}
