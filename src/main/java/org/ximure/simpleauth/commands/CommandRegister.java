package org.ximure.simpleauth.commands;

import org.apache.commons.codec.digest.DigestUtils;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth.misc.Utils;

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

        // these 3 are self-explanatory
        Boolean registered = authManager.isRegistered(playerUUID);
        if (registered) {
            String alreadyRegistered = utils.getStringFromYml("already_registered");
            player.sendMessage(alreadyRegistered);
            return true;
        }
        boolean nothingProvided = args.length == 0;
        if (nothingProvided) {
            String noPasswordAndReminder = utils.getStringFromYml("no_password_and_reminder");
            player.sendMessage(noPasswordAndReminder);
            return true;
        }
        boolean reminderNotProvided = args.length == 1;
        if (reminderNotProvided) {
            String noReminder = utils.getStringFromYml("no_reminder");
            player.sendMessage(noReminder);
            return true;
        }
        boolean passwordAndReminderProvided = args.length > 2;
        if (passwordAndReminderProvided) {
            String hashedPassword = DigestUtils.sha256Hex(args[0]);
            String passwordReminder = utils.getAllArgsString(args, true);
            // adding player's uuid, password and reminder to the database and checking
            // if inserting data to database goes wrong
            if (!authManager.insertPassword(playerUUID, hashedPassword, passwordReminder)) {
                String notSuccessfullRegistration = utils.getStringFromYml("not_successfull_registration");
                player.sendMessage(notSuccessfullRegistration);
                return true;
            }
            String successfullRegistration = utils.getStringFromYml("successfull_registration");
            GameMode previousGameMode = authManager.restoreGameMode(playerUUID);
            authManager.setOnline(playerUUID);
            // restoring previous gamemode which has been written in onplayerjoin event
            player.setGameMode(previousGameMode);
            player.sendMessage(successfullRegistration);
            return true;
        }
        else {
            // if player entered too many arguments
            String tooManyArgs = utils.getStringFromYml("too_many_args");
            player.sendMessage(tooManyArgs);
            return true;
        }
    }
}
