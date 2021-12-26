package org.ximure.simpleauth.auth;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.ximure.simpleauth.Utils;

import java.util.UUID;

public class AuthManager extends DatabaseManager {
    private final AuthManager authManager = this;
    private final Utils utils;
    private final PlayerStatsManager playerStatsManager;

    public AuthManager(Utils utils, PlayerStatsManager playerStatsManager) {
        this.utils = utils;
        this.playerStatsManager = playerStatsManager;
    }

    public void loginPlayer(Player player, UUID playerUUID, String password) {
        boolean validPassword = authManager.verifyPassword(playerUUID, password);
        // if password is valid - we'll let player on a server
        if (validPassword) {
            GameMode previousGameMode = playerStatsManager.getPlayerStats(playerUUID).getLoginGamemode();
            String successfullLogin = utils.getString("successfull_login");
            // setting playerStatus to online. Player won't be able to use /register or /login commands
            playerStatsManager.setOnline(playerUUID);
            // restoring previous gamemode which has been written in onplayerjoin event
            player.setGameMode(previousGameMode);
            // additional check to not teleport player to the location of death if he tried to relog
            if (!playerStatsManager.getPlayerStats(playerUUID).isDead()) {
                player.teleport(playerStatsManager.getPlayerStats(playerUUID).getLoginLocation());
            }
            player.sendMessage(successfullLogin);
            // restoring player fall velocity (because he can abuse that)
            player.setVelocity(playerStatsManager.getPlayerStats(playerUUID).getLoginVelocity());
        } else {
            // if password is not valid - we won't let player on a server
            String wrongPassword = utils.getString("wrong_password");
            player.sendMessage(wrongPassword);
        }
    }

    public void registerPlayer(Player player, UUID playerUUID, String password, String[] args) {
        String passwordReminder = utils.getAllArgsString(args, true);
        // adding player's uuid, password and reminder to the database and checking
        // if inserting data to database goes wrong
        if (!authManager.insertPassword(playerUUID, password, passwordReminder)) {
            String notSuccessfullRegistration = utils.getString("not_successfull_registration");
            player.sendMessage(notSuccessfullRegistration);
        }
        String successfullRegistration = utils.getString("successfull_registration");
        GameMode previousGameMode = playerStatsManager.getPlayerStats(playerUUID).getLoginGamemode();
        // setting player status to online so the registration command don't add this user in the database again
        playerStatsManager.setOnline(playerUUID);
        // restoring previous gamemode which has been written in onplayerjoin event
        player.setGameMode(previousGameMode);
        player.sendMessage(successfullRegistration);
        // additional check to not teleport player to the location of death if he tried to relog
        if (!playerStatsManager.getPlayerStats(playerUUID).isDead()) {
            player.teleport(playerStatsManager.getPlayerStats(playerUUID).getLoginLocation());
        }
        // setting player velocity so player cannot abuse this in any way (just in case)
        player.setVelocity(playerStatsManager.getPlayerStats(playerUUID).getLoginVelocity());
    }
}
