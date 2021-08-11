package org.ximure.simpleauth.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth.MessagesUtils;
import org.ximure.simpleauth.auth.AuthManager;

import java.util.UUID;

public class CommandLogin implements CommandExecutor {
    private final AuthManager authManager;
    private final MessagesUtils messagesUtils;

    public CommandLogin(AuthManager authManager, MessagesUtils messagesUtils) {
        this.authManager = authManager;
        this.messagesUtils = messagesUtils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        boolean loggedIn = authManager.isOnline(playerUUID);
        if (loggedIn) {
            String alreadyLoggedIn = messagesUtils.getString("already_logged_in");
            player.sendMessage(alreadyLoggedIn);
            return true;
        }
        Boolean registered = authManager.isRegistered(playerUUID);
        if (registered == null || !registered) {
            String notRegistered = messagesUtils.getString("not_registered");
            player.sendMessage(notRegistered);
            return true;
        }
        boolean tooManyStrings = args.length > 1;
        if (tooManyStrings) {
            String tooManyArgs = messagesUtils.getString("too_many_args");
            player.sendMessage(tooManyArgs);
            return true;
        }
        boolean passwordProvided = args.length == 1;
        if (!passwordProvided) {
            String passwordNotProvided = messagesUtils.getString("password_not_provided");
            player.sendMessage(passwordNotProvided);
            return true;
        }
        String password = args[0];
        boolean validPassword = authManager.verifyPassword(playerUUID, password);
        if (validPassword) {
            GameMode previousGameMode = authManager.restoreGameMode(playerUUID);
            String successfullLogin = messagesUtils.getString("successfull_login");
            // setting player status to online so the registration command don't add this user in the database again
            authManager.setOnline(playerUUID);
            // restoring previous gamemode which has been written in onplayerjoin event
            player.setGameMode(previousGameMode);
            player.sendMessage(successfullLogin);
            return true;
        } else {
            String wrongPassword = messagesUtils.getString("wrong_password");
            player.sendMessage(wrongPassword);
        }
        return true;
    }
}
