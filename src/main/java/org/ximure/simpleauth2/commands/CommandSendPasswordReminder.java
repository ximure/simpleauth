package org.ximure.simpleauth2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth2.StringUtils;
import org.ximure.simpleauth2.auth.AuthManager;

import java.util.UUID;

public class CommandSendPasswordReminder implements CommandExecutor {
    private final AuthManager AuthManager;
    private final StringUtils stringUtils;

    public CommandSendPasswordReminder(AuthManager AuthManager, StringUtils stringUtils) {
        this.AuthManager = AuthManager;
        this.stringUtils = stringUtils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        String passwordReminder = AuthManager.getPasswordReminder(playerUUID);
        String passwordReminderMessage = stringUtils.getString("reminder_message");
        player.sendMessage(passwordReminderMessage + passwordReminder);
        return true;
    }
}
