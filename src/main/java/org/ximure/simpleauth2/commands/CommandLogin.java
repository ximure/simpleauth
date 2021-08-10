package org.ximure.simpleauth2.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth2.StringUtils;
import org.ximure.simpleauth2.auth.AuthManager;

import java.util.UUID;

public class CommandLogin implements CommandExecutor {
    private final AuthManager authManager;
    private final StringUtils stringUtils;

    public CommandLogin(AuthManager authManager, StringUtils stringUtils) {
        this.authManager = authManager;
        this.stringUtils = stringUtils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        boolean loggedIn = authManager.isOnline(playerUUID);
        if (loggedIn) {
            String alreadyLoggedInMessage = stringUtils.getString("already_logged_in");
            player.sendMessage(alreadyLoggedInMessage);
            return true;
        }
        Boolean registered = authManager.isRegistered(playerUUID);
        if (registered == null || !registered) {
            String notRegisteredMessage = stringUtils.getString("not_registered");
            player.sendMessage(notRegisteredMessage);
            return true;
        }
        boolean tooManyStrings = args.length > 1;
        if (tooManyStrings) {
            String tooManyArgsMessage = stringUtils.getString("too_many_args");
            player.sendMessage(tooManyArgsMessage);
            return true;
        }
        boolean passwordProvided = args.length == 1;
        if (!passwordProvided) {
            String passwordNotEnteredMessage = stringUtils.getString("password_not_provided");
            player.sendMessage(passwordNotEnteredMessage);
            return true;
        }
        String password = args[0];
        boolean validPassword = authManager.checkPassword(playerUUID, password);
        if (validPassword) {
            GameMode previousGameMode = authManager.restoreGameMode(playerUUID);
            String successfulLoginMessage = stringUtils.getString("successfull_login");
            // setting player status to online so the registration command don't add this user in the database again
            authManager.setOnline(playerUUID);
            // restoring previous gamemode which has been written in onplayerjoin event
            player.setGameMode(previousGameMode);
            player.sendMessage(successfulLoginMessage);
            return true;
        } else {
            String wrongPasswordMessage = stringUtils.getString("wrong_password");
            player.sendMessage(wrongPasswordMessage);
        }
        return true;
    }
}
