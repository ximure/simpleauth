package org.ximure.simpleauth.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ximure.simpleauth.Utils;
import org.ximure.simpleauth.auth.AuthManager;

import java.util.UUID;

public class CommandLogin implements CommandExecutor {
    private final AuthManager authManager;
    private final Utils utils;

    public CommandLogin(AuthManager authManager, Utils utils) {
        this.authManager = authManager;
        this.utils = utils;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();

        // these 4 are self-explanatory
        boolean loggedIn = authManager.isOnline(playerUUID);
        if (loggedIn) {
            String alreadyLoggedIn = utils.getString("already_logged_in");
            player.sendMessage(alreadyLoggedIn);
            return true;
        }
        Boolean registered = authManager.isRegistered(playerUUID);
        if (!registered) {
            String notRegistered = utils.getString("not_registered");
            player.sendMessage(notRegistered);
            return true;
        }
        boolean tooManyStrings = args.length > 1;
        if (tooManyStrings) {
            String tooManyArgs = utils.getString("too_many_args");
            player.sendMessage(tooManyArgs);
            return true;
        }
        boolean passwordProvided = args.length == 1;
        if (!passwordProvided) {
            String passwordNotProvided = utils.getString("password_not_provided");
            player.sendMessage(passwordNotProvided);
            return true;
        }
        String hashedPassword = args[0];
        boolean validPassword = authManager.verifyPassword(playerUUID, hashedPassword);
        // if password is valid - we'll let player on a server
        if (validPassword) {
            GameMode previousGameMode = authManager.restoreGameMode(playerUUID);
            String successfullLogin = utils.getString("successfull_login");
            // setting playerStatus to online. Player won't be able to use /register or /login command after that
            authManager.setOnline(playerUUID);
            // restoring previous gamemode which has been written in onplayerjoin event
            player.setGameMode(previousGameMode);
            player.sendMessage(successfullLogin);
        } else {
            // if password is not valid - we won't let player on a server
            String wrongPassword = utils.getString("wrong_password");
            player.sendMessage(wrongPassword);
        }
        return true;
    }
}
