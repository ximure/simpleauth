package org.ximure.simpleauth2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth2.StringUtils;
import org.ximure.simpleauth2.auth.AuthManager;

import java.util.UUID;

public class CommandChangePassword implements CommandExecutor {
    private final AuthManager authManager;
    private final StringUtils stringUtils;

    public CommandChangePassword(AuthManager authManager, StringUtils stringUtils) {
        this.authManager = authManager;
        this.stringUtils = stringUtils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        boolean nothingProvided = args.length == 0;
        if (nothingProvided) {
            String nothingProvidedMessage = stringUtils.getString("no_password_reminder");
            player.sendMessage(nothingProvidedMessage);
            return true;
        }
        boolean newPasswordNotProvided = args.length == 1;
        if (newPasswordNotProvided) {
            String newPasswordNotProvidedMessage = stringUtils.getString("no_new_password");
            player.sendMessage(newPasswordNotProvidedMessage);
            return true;
        }
        boolean tooManyArgs = args.length > 2;
        if (tooManyArgs) {
            String tooManyArgsMessage = stringUtils.getString("too_many_args");
            player.sendMessage(tooManyArgsMessage);
            return true;
        }
        else {
            String oldPassword = args[0];
            Boolean validPassword = authManager.checkPassword(playerUUID, oldPassword);
            if (validPassword != null && validPassword) {
                String newPassword = args[1];
                String successfullPasswordChange = stringUtils.getString("successfull_password_change");
                // changing password in the database
                authManager.changePassword(playerUUID, newPassword);
                player.sendMessage(successfullPasswordChange);
                return true;
            }
            String wrongOldPasswordMessage = stringUtils.getString("wrong_old_password");
            player.sendMessage(wrongOldPasswordMessage);
        }
        return true;
    }
}
