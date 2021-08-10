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

public class CommandRegister implements CommandExecutor {
    private final AuthManager authManager;
    private final StringUtils stringUtils;

    public CommandRegister(AuthManager authManager, StringUtils stringUtils) {
        this.authManager = authManager;
        this.stringUtils = stringUtils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        Boolean registered = authManager.isRegistered(playerUUID);
        if (registered) {
            String alreadyRegisteredMessage = stringUtils.getString("already_registered");
            player.sendMessage(alreadyRegisteredMessage);
            return true;
        }
        Boolean loggedIn = authManager.isOnline(playerUUID);
        if (loggedIn) {
            String alreadyLoggedInAndRegistered = stringUtils.getString("already_registered_logged_in");
            player.sendMessage(alreadyLoggedInAndRegistered);
            return true;
        }
        boolean nothingProvided = args.length == 0;
        if (nothingProvided) {
            String nothingProvidedMessage = stringUtils.getString("no_password_reminder");
            player.sendMessage(nothingProvidedMessage);
            return true;
        }
        boolean reminderNotProvided = args.length == 1;
        if (reminderNotProvided) {
            String reminderNotProvidedMessage = stringUtils.getString("no_reminder");
            player.sendMessage(reminderNotProvidedMessage);
            return true;
        }
        boolean passwordAndReminderProvided = args.length == 2;
        if (passwordAndReminderProvided) {
            String password = args[0];
            String passwordReminder = args[1];
            String successfullRegistrationMessage = stringUtils.getString("successfull_registration");
            GameMode previousGameMode = authManager.restoreGameMode(playerUUID);
            // adding player's uuid, password and reminder to the database
            authManager.setPassword(playerUUID, password, passwordReminder);
            // setting player status to online so the registration command don't add this user in the database again
            authManager.setOnline(playerUUID);
            // restoring previous gamemode which has been written in onplayerjoin event
            player.setGameMode(previousGameMode);
            player.sendMessage(successfullRegistrationMessage);
        }
        else {
            String tooManyArgs = stringUtils.getString("too_many_args");
            player.sendMessage(tooManyArgs);
        }
        return true;
    }
}
