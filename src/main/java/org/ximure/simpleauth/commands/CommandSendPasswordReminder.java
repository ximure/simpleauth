package org.ximure.simpleauth.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth.Utils;
import org.ximure.simpleauth.auth.AuthManager;

import java.util.UUID;

public class CommandSendPasswordReminder implements CommandExecutor {
    private final AuthManager AuthManager;
    private final Utils utils;

    public CommandSendPasswordReminder(AuthManager AuthManager, Utils utils) {
        this.AuthManager = AuthManager;
        this.utils = utils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        String passwordReminder = AuthManager.getPasswordReminder(playerUUID);
        // if player associated uuid does not contain reminder yet
        if (passwordReminder == null) {
            String noReminder = utils.getString("no_reminder_yet");
            player.sendMessage(noReminder);
            return true;
        }
        // if everything goes ok we'll send a player his password reminder
        String passwordReminderMessage = utils.getString("reminder_message");
        player.sendMessage(passwordReminderMessage + passwordReminder);
        return true;
    }
}
