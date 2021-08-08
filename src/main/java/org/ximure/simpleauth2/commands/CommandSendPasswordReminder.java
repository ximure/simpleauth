package org.ximure.simpleauth2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth2.SqlManager;

import java.util.UUID;

public class CommandSendPasswordReminder implements CommandExecutor {
    private final SqlManager sqlManager;

    public CommandSendPasswordReminder(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        String passwordReminder = sqlManager.getPasswordReminder(playerUUID);
        player.sendMessage("[Pure] Ваша подсказка к паролю: " + passwordReminder);
        return true;
    }
}
