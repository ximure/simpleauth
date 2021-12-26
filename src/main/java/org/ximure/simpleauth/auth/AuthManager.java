package org.ximure.simpleauth.auth;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.ximure.simpleauth.Utils;

import java.util.UUID;

public class AuthManager extends DatabaseManager {
    private final AuthManager authManager = this;
    private final Utils utils;

    public AuthManager(Utils utils) {
        this.utils = utils;
    }

    public void loginPlayer(Player player, UUID playerUUID, String password) {
        boolean validPassword = authManager.verifyPassword(playerUUID, password);
        // if password is valid - we'll let player on a server
        if (validPassword) {
            GameMode previousGameMode = authManager.restoreGameMode(playerUUID);
            String successfullLogin = utils.getString("successfull_login");
            // setting playerStatus to online. Player won't be able to use /register or /login command after that
            authManager.setOnline(playerUUID);
            // restoring previous gamemode which has been written in onplayerjoin event
            player.setGameMode(previousGameMode);
            // additional check to not teleport player to the location of death if he tried to relog
            if (!authManager.isDeadOnLogin(playerUUID)) {
                player.teleport(authManager.restorePlayerLocation(playerUUID));
            }
            player.sendMessage(successfullLogin);
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
        GameMode previousGameMode = authManager.restoreGameMode(playerUUID);
        // setting player status to online so the registration command don't add this user in the database again
        authManager.setOnline(playerUUID);
        // restoring previous gamemode which has been written in onplayerjoin event
        player.setGameMode(previousGameMode);
        player.teleport(authManager.restorePlayerLocation(playerUUID));
        player.sendMessage(successfullRegistration);
        // additional check to not teleport player to the location of death if he tried to relog
        if (!authManager.isDeadOnLogin(playerUUID)) {
            player.teleport(authManager.restorePlayerLocation(playerUUID));
        }
    }
}
