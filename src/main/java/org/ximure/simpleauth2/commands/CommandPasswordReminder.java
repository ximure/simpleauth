package org.ximure.simpleauth2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ximure.simpleauth2.SqlManager;

public class CommandPasswordReminder implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TODO: password reminder logic
        SqlManager sqlManager = new SqlManager();
        Player player = (Player) sender;
        String reminder = sqlManager.getPasswordReminder(player.getUniqueId());
        player.sendMessage("Your password reminder: " + reminder);
        return true;
    }
}
