package org.ximure.simpleauth2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth2.SqlManager;

import java.util.UUID;

public class CommandChangePassword implements CommandExecutor {
    // TODO: check why it doesn't change password in the database
    private final SqlManager sqlManager;

    public CommandChangePassword(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        boolean nothingProvided = args.length == 0;
        if (nothingProvided) {
            player.sendMessage("[Pure] Вы не ввели старый и новый пароли");
            return true;
        }
        boolean newPasswordNotProvided = args.length == 1;
        if (newPasswordNotProvided) {
            player.sendMessage("[Pure] Вы не ввели новый пароль");
            return true;
        }
        boolean tooManyArgs = args.length > 2;
        if (tooManyArgs) {
            player.sendMessage("[Pure] Вы ввели слишком много значений." +
                    " Синтаксис команды: /cpw старый_пароль новый_пароль");
            return true;
        }
        else {
            String oldPassword = args[0];
            Boolean validPassword = sqlManager.checkPassword(playerUUID, oldPassword);
            if (validPassword != null && validPassword) {
                String newPassword = args[1];
                // changing password in the database
                sqlManager.changePassword(playerUUID, newPassword);
                player.sendMessage("[Pure] Вы сменили пароль. Ваш новый пароль: " + newPassword);
                return true;
            }
            player.sendMessage("[Pure] Вы ввели неверный старый пароль");
        }
        return true;
    }
}
