package org.ximure.simpleauth.auth;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.ximure.simpleauth.Utils;

import java.util.UUID;

public class AuthManager extends DatabaseManager {
    private final Utils utils;

    public AuthManager(Utils utils) {
        this.utils = utils;
    }

    public void loginPlayer(Player player, UUID playerUUID, String password) {
        boolean validPassword = this.verifyPassword(playerUUID, password);
        // if password is valid - we'll let player on a server
        if (validPassword) {
            GameMode previousGameMode = this.getPlayerStats(playerUUID).getLoginGamemode();
            String successfulLogin = utils.getString("successful_login");
            // setting playerStatus to online. Player won't be able to use /register or /login commands
            this.setOnline(playerUUID);
            // restoring previous gamemode which has been written in onplayerjoin event
            player.setGameMode(previousGameMode);
            // additional check to not teleport player to the location of death if he tried to relog
            if (!this.getPlayerStats(playerUUID).isDead()) {
                player.teleport(this.getPlayerStats(playerUUID).getLoginLocation());
            }
            player.sendMessage(successfulLogin);
            // restoring player fall velocity (because he can abuse that)
            // TODO: doesn't working
            player.setVelocity(this.getPlayerStats(playerUUID).getLoginVelocity());
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
        if (!this.insertPassword(playerUUID, password, passwordReminder)) {
            String notSuccessfulRegistration = utils.getString("not_successful_registration");
            player.sendMessage(notSuccessfulRegistration);
        }
        String successfulRegistration = utils.getString("successful_registration");
        GameMode previousGameMode = this.getPlayerStats(playerUUID).getLoginGamemode();
        // setting player status to online so the registration command don't add this user in the database again
        this.setOnline(playerUUID);
        // restoring previous gamemode which has been written in onplayerjoin event
        player.setGameMode(previousGameMode);
        player.sendMessage(successfulRegistration);
        // additional check to not teleport player to the location of death if he tried to relog
        if (!this.getPlayerStats(playerUUID).isDead()) {
            player.teleport(this.getPlayerStats(playerUUID).getLoginLocation());
        }
        // setting player velocity so player cannot abuse this in any way (just in case)
        // TODO: doesn't working
        player.setVelocity(this.getPlayerStats(playerUUID).getLoginVelocity());
    }
}
