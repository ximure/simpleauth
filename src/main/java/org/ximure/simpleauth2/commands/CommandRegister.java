package org.ximure.simpleauth2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ximure.simpleauth2.SqlManager;

import java.util.UUID;

public class CommandRegister implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TODO: registration logic
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        SqlManager sqlManager = new SqlManager();
        if (sqlManager.isRegistered(playerUUID)) {
            player.sendMessage("You're already registered");
        } else {
            player.sendMessage("You've successfully registered");
        }
        sqlManager.setPassword(player.getUniqueId(), args[0], args[1]);
        return true;
    }
}
