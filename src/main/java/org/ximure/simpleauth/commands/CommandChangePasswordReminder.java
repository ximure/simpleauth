package org.ximure.simpleauth.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth.MessagesUtils;
import org.ximure.simpleauth.auth.AuthManager;

import java.util.UUID;

public class CommandChangePasswordReminder implements CommandExecutor {
    private final AuthManager authManager;
    private final MessagesUtils messagesUtils;

    public CommandChangePasswordReminder(AuthManager AuthManager, MessagesUtils messagesUtils) {
        this.authManager = AuthManager;
        this.messagesUtils = messagesUtils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        if (!authManager.changePasswordReminder(playerUUID, args[0])) {
            String notSuccessfullReminderChange = messagesUtils.getString("not_successfull_cpr");
            player.sendMessage(notSuccessfullReminderChange);
            return false;
        }
        // TODO: write reminder change logic
        return true;
    }
}
