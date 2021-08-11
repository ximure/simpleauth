package org.ximure.simpleauth.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth.Utils;
import org.ximure.simpleauth.auth.AuthManager;

import java.util.UUID;

public class CommandChangePasswordReminder implements CommandExecutor {
    private final AuthManager authManager;
    private final Utils utils;

    public CommandChangePasswordReminder(AuthManager AuthManager, Utils utils) {
        this.authManager = AuthManager;
        this.utils = utils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        // if no reminder was provided we'll warn a player
        boolean reminderProvided = args.length > 0;
        if (!reminderProvided) {
            String noReminder = utils.getString("no_reminder");
            player.sendMessage(noReminder);
            return true;
        }
        // if total length not higher than 230, we'll write it to the database
        int argsLength = utils.getArgsLength(args);
        if (argsLength >= 230) {
            player.sendMessage("too_many_args");
            return true;
        }
        // creating string with all args
        String allArgs = utils.getAllArgsString(args, false);
        // writing string with all args to the database only if it was successfull
        if (!authManager.changePasswordReminder(playerUUID, allArgs)) {
            String notSuccessfullReminderChange = utils.getString("not_successfull_cpr");
            player.sendMessage(notSuccessfullReminderChange);
            return true;
        }
        String successfullReminderChange = utils.getString("successfull_password_reminder_change");
        player.sendMessage(successfullReminderChange);
        return true;
    }
}
