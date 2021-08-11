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

public class CommandRegister implements CommandExecutor {
    private final AuthManager authManager;
    private final MessagesUtils messagesUtils;

    public CommandRegister(AuthManager authManager, MessagesUtils messagesUtils) {
        this.authManager = authManager;
        this.messagesUtils = messagesUtils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        Boolean registered = authManager.isRegistered(playerUUID);
        if (registered) {
            String alreadyRegistered = messagesUtils.getString("already_registered");
            player.sendMessage(alreadyRegistered);
            return false;
        }
        Boolean loggedIn = authManager.isOnline(playerUUID);
        if (loggedIn) {
            String alreadyLoggedInAndRegistered = messagesUtils.getString("already_registered_logged_in");
            player.sendMessage(alreadyLoggedInAndRegistered);
            return false;
        }
        boolean nothingProvided = args.length == 0;
        if (nothingProvided) {
            String noPasswordAndReminder = messagesUtils.getString("no_password_and_reminder");
            player.sendMessage(noPasswordAndReminder);
            return false;
        }
        boolean reminderNotProvided = args.length == 1;
        if (reminderNotProvided) {
            String noReminder = messagesUtils.getString("no_reminder");
            player.sendMessage(noReminder);
            return false;
        }
        boolean passwordAndReminderProvided = args.length == 2;
        if (passwordAndReminderProvided) {
            String password = args[0];
            String passwordReminder = args[1];
            // if inserting data to database goes wrong
            if (!authManager.insertPassword(playerUUID, password, passwordReminder)) {
                String notSuccessfullRegistration = messagesUtils.getString("notSuccessfullRegistration");
                player.sendMessage(notSuccessfullRegistration);
                return false;
            }
            String successfullRegistration = messagesUtils.getString("successfull_registration");
            GameMode previousGameMode = authManager.restoreGameMode(playerUUID);
            // adding player's uuid, password and reminder to the database
            // setting player status to online so the registration command don't add this user in the database again
            authManager.setOnline(playerUUID);
            // restoring previous gamemode which has been written in onplayerjoin event
            player.setGameMode(previousGameMode);
            player.sendMessage(successfullRegistration);
        }
        else {
            String tooManyArgs = messagesUtils.getString("too_many_args");
            player.sendMessage(tooManyArgs);
            return false;
        }
        return true;
    }
}
