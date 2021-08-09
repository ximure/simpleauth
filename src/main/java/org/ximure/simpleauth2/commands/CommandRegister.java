package org.ximure.simpleauth2.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth2.PlayerStatus;
import org.ximure.simpleauth2.SqlManager;

import java.util.UUID;

public class CommandRegister implements CommandExecutor {
    private final PlayerStatus playerStatus;
    private final SqlManager sqlManager;

    public CommandRegister(PlayerStatus playerStatus, SqlManager sqlManager) {
        this.playerStatus = playerStatus;
        this.sqlManager = sqlManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        Boolean registered = sqlManager.isRegistered(playerUUID);
        if (registered) {
            player.sendMessage("[Pure] Вы уже зарегистрированы");
            return true;
        }
        Boolean loggedIn = playerStatus.isOnline(playerUUID);
        if (loggedIn) {
            player.sendMessage("[Pure] Вы уже залогинены и зарегистрированы");
            return true;
        }
        boolean nothingProvided = args.length == 0;
        if (nothingProvided) {
            player.sendMessage("[Pure] Вы не ввели пароль и подсказку к паролю");
            return true;
        }
        boolean reminderNotProvided = args.length == 1;
        if (reminderNotProvided) {
            player.sendMessage("[Pure] Вы не ввели подсказку к паролю");
            return true;
        }
        boolean passwordAndReminderProvided = args.length == 2;
        if (passwordAndReminderProvided) {
            String password = args[0];
            String passwordReminder = args[1];
            GameMode previousGameMode = playerStatus.getGameMode(playerUUID);
            // adding player's uuid, password and reminder to the database
            sqlManager.setPassword(playerUUID, password, passwordReminder);
            // setting player status to online so the registration command don't add this user in the database again
            playerStatus.setOnline(playerUUID);
            // restoring previous gamemode which has been written in onplayerjoin event
            player.setGameMode(previousGameMode);
            player.sendMessage("[Pure] Вы зарегистрировались");
        }
        else {
            player.sendMessage("[Pure] Вы ввели слишком много информации. Разделяйте пароль и подсказку к нему пробелом");
        }
        return true;
    }
}
