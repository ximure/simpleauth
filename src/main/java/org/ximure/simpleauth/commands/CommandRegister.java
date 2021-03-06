package org.ximure.simpleauth.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth.Utils;
import org.ximure.simpleauth.auth.AuthManager;

import java.util.UUID;

public class CommandRegister implements CommandExecutor {
    private final AuthManager authManager;
    private final Utils utils;

    public CommandRegister(AuthManager authManager, Utils utils) {
        this.authManager = authManager;
        this.utils = utils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        // player is registered already?
        Boolean registered = authManager.isRegistered(playerUUID);
        if (registered) {
            String alreadyRegistered = utils.getString("already_registered");
            player.sendMessage(alreadyRegistered);
            return true;
        }
        // did player just entered nothing at all?
        boolean nothingProvided = args.length == 0;
        if (nothingProvided) {
            String noPasswordAndReminder = utils.getString("no_password_and_reminder");
            player.sendMessage(noPasswordAndReminder);
            return false;
        }
        // player forgot to type in password reminder?
        boolean reminderNotProvided = args.length == 1;
        if (reminderNotProvided) {
            String noReminder = utils.getString("no_reminder");
            player.sendMessage(noReminder);
            return false;
        }
        // else, we'll try to register him
        String password = args[0];
        authManager.registerPlayer(player, playerUUID, password, args);
        // removing his player stats
        authManager.removePlayerStats(playerUUID);
        return true;
    }
}
