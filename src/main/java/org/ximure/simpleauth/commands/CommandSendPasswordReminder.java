package org.ximure.simpleauth.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth.MessagesUtils;
import org.ximure.simpleauth.auth.AuthManager;

import java.util.UUID;

public class CommandSendPasswordReminder implements CommandExecutor {
    private final AuthManager AuthManager;
    private final MessagesUtils messagesUtils;

    public CommandSendPasswordReminder(AuthManager AuthManager, MessagesUtils messagesUtils) {
        this.AuthManager = AuthManager;
        this.messagesUtils = messagesUtils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        String passwordReminder = AuthManager.getPasswordReminder(playerUUID);
        String passwordReminderMessage = messagesUtils.getString("reminder_message");
        player.sendMessage(passwordReminderMessage + passwordReminder);
        return true;
    }
}
