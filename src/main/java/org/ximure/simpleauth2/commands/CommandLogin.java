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

public class CommandLogin implements CommandExecutor {
    private final PlayerStatus playerStatus;
    private final SqlManager sqlManager;

    public CommandLogin(PlayerStatus playerStatus, SqlManager sqlManager) {
        this.playerStatus = playerStatus;
        this.sqlManager = sqlManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        boolean loggedIn = playerStatus.isOnline(playerUUID);
        if (loggedIn) {
            player.sendMessage("[Pure] Вы уже залогинены");
            return true;
        }
        Boolean registered = sqlManager.isRegistered(playerUUID);
        if (registered == null || !registered) {
            player.sendMessage("[Pure] Вы не зарегистрированы");
            return true;
        }
        boolean tooManyStrings = args.length > 1;
        if (tooManyStrings) {
            player.sendMessage("[Pure] Вы ввели слишком много значений");
            return true;
        }
        boolean passwordProvided = args.length == 1;
        if (!passwordProvided) {
            player.sendMessage("[Pure] Вы не ввели пароль");
            return true;
        }
        String password = args[0];
        boolean validPassword = sqlManager.checkPassword(playerUUID, password);
        if (validPassword) {
            GameMode previousGameMode = playerStatus.getPreviousGamemode(playerUUID);
            playerStatus.setOnline(playerUUID);
            player.setGameMode(previousGameMode);
            player.sendMessage("[Pure] Вы залогинились");
            return true;
        } else {
            player.sendMessage("[Pure] Неверный пароль");
        }
        return true;
    }
}
