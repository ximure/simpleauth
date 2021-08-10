package org.ximure.simpleauth2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth2.auth.AuthManager;

import java.util.UUID;

public class CommandChangePasswordReminder implements CommandExecutor {
    // TODO: write reminder change logic
    private final AuthManager AuthManager;

    public CommandChangePasswordReminder(AuthManager AuthManager) {
        this.AuthManager = AuthManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        AuthManager.changePasswordReminder(playerUUID, args[0]);
        return true;
    }
}
