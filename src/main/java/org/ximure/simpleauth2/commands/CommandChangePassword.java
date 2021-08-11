package org.ximure.simpleauth2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth2.MessagesUtils;
import org.ximure.simpleauth2.auth.AuthManager;

import java.util.UUID;

public class CommandChangePassword implements CommandExecutor {
    private final AuthManager authManager;
    private final MessagesUtils messagesUtils;

    public CommandChangePassword(AuthManager authManager, MessagesUtils messagesUtils) {
        this.authManager = authManager;
        this.messagesUtils = messagesUtils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        boolean nothingProvided = args.length == 0;
        if (nothingProvided) {
            String nothingProvidedMessage = messagesUtils.getString("no_password_reminder");
            player.sendMessage(nothingProvidedMessage);
            return false;
        }
        boolean newPasswordNotProvided = args.length == 1;
        if (newPasswordNotProvided) {
            String newPasswordNotProvidedMessage = messagesUtils.getString("no_new_password");
            player.sendMessage(newPasswordNotProvidedMessage);
            return false;
        }
        boolean tooManyArgs = args.length > 2;
        if (tooManyArgs) {
            String tooManyArgsMessage = messagesUtils.getString("too_many_args");
            player.sendMessage(tooManyArgsMessage);
            return false;
        }
        else {
            String oldPassword = args[0];
            Boolean validPassword = authManager.sqlVerifyPassword(playerUUID, oldPassword);
            if (validPassword != null && validPassword) {
                String newPassword = args[1];
                if (!authManager.sqlChangePassword(playerUUID, newPassword)) {
                    String notSuccessfullPasswordChange = messagesUtils.getString("not_successfull_cpw");
                    player.sendMessage(notSuccessfullPasswordChange);
                    return false;
                }
                String successfullPasswordChange = messagesUtils.getString("successfull_password_change");
                // changing password in the database
                player.sendMessage(successfullPasswordChange);
                return true;
            }
            String wrongOldPasswordMessage = messagesUtils.getString("wrong_old_password");
            player.sendMessage(wrongOldPasswordMessage);
            return false;
        }
    }
}
