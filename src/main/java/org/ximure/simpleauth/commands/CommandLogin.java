package org.ximure.simpleauth.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth.SqlManager;

import java.util.UUID;

public class CommandLogin implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // TODO: login logic
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        String enteredPassword = args[0];
        SqlManager sqlManager = new SqlManager();
        if (sqlManager.getPassword(playerUUID, enteredPassword)) {
            player.sendMessage("right password");
            return true;
        } else {
            player.sendMessage("wrong password");
            return false;
        }
    }
}
