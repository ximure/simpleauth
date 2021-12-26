package org.ximure.simpleauth.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth.Utils;
import org.ximure.simpleauth.auth.AuthManager;

import java.util.UUID;

public class CommandChangePassword implements CommandExecutor {
    private final AuthManager authManager;
    private final Utils utils;

    public CommandChangePassword(AuthManager authManager, Utils utils) {
        this.authManager = authManager;
        this.utils = utils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        // these 3 blocks are self-explanatory
        boolean nothingProvided = args.length == 0;
        if (nothingProvided) {
            String nothingProvidedMessage = utils.getString("no_passwords");
            player.sendMessage(nothingProvidedMessage);
            return true;
        }
        boolean newPasswordNotProvided = args.length == 1;
        if (newPasswordNotProvided) {
            String newPasswordNotProvidedMessage = utils.getString("no_new_password");
            player.sendMessage(newPasswordNotProvidedMessage);
            return true;
        }
        boolean tooManyArgs = args.length > 2;
        if (tooManyArgs) {
            String tooManyArgsMessage = utils.getString("too_many_args");
            player.sendMessage(tooManyArgsMessage);
            return true;
        } else {
            String oldPassword = args[0];
            Boolean validPassword = authManager.verifyPassword(playerUUID, oldPassword);
            // if old password is valid and there's a data in the database at all
            if (validPassword != null && validPassword) {
                String newPassword = args[1];
                // trying to change player's password in the database. If something goes wrong
                // this block will be executed
                if (!authManager.changePassword(playerUUID, newPassword)) {
                    String notSuccessfullPasswordChange = utils.getString("not_successfull_cpw");
                    player.sendMessage(notSuccessfullPasswordChange);
                    return true;
                }
                // if everything is ok, we'll send player a message that he changed his password
                String successfullPasswordChange = utils.getString("successfull_password_change");
                player.sendMessage(successfullPasswordChange);
                return true;
            }
            // that block we'll be executed when player's entered password not equals to one in the database
            String wrongOldPasswordMessage = utils.getString("wrong_old_password");
            player.sendMessage(wrongOldPasswordMessage);
            return true;
        }
    }
}
