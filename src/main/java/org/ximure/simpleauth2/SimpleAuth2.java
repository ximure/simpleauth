package org.ximure.simpleauth2;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.ximure.simpleauth2.auth.AuthManager;
import org.ximure.simpleauth2.auth.PlayerStatus;
import org.ximure.simpleauth2.commands.*;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

public final class SimpleAuth2 extends JavaPlugin {
    public final static File PASSWORDS_DATABASE = new File("./plugins/SimpleAuth2/passwords.db");
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public final static File PLUGIN_FOLDER = new File("./plugins/SimpleAuth2");
    private final AuthManager authManager = new AuthManager();
    private final StringUtils stringUtils = new StringUtils();
    private final Logger logger = Bukkit.getLogger();

    @Override
    public void onEnable() {
        // if plugin folder does not exist - this block will create it
        if (!PLUGIN_FOLDER.exists()) {
            if (!PLUGIN_FOLDER.mkdir()) {
                logger.info(ANSI_RED + "[SimpleAuth2] Plugin folder cannot be created. Maybe something wrong " +
                        "with folder permissions?" + ANSI_RESET);
                // TODO: plugin disabling
            }
        }
        // if database does not exist - this block will create it
        if (!PASSWORDS_DATABASE.exists()) {
            if (!authManager.createDatabase()) {
                logger.info(ANSI_RED + "[SimpleAuth2] Database cannot be created. Maybe something wrong with" +
                        "folder permissions?" + ANSI_RESET);
                // TODO: plugin disabling
            }
            if (!authManager.createPasswordsTable()) {
                logger.info(ANSI_RED + "[SimpleAuth2] Passwords table cannot be created" + ANSI_RESET);
                // TODO: plugin disabling
            }
        }
        // registering event handler and commands
        getServer().getPluginManager().registerEvents(new EventListener(authManager, stringUtils), this);
        Objects.requireNonNull(this.getCommand("login")).setExecutor(new CommandLogin(authManager, stringUtils));
        Objects.requireNonNull(this.getCommand("register")).setExecutor(new CommandRegister(authManager, stringUtils));
        Objects.requireNonNull(this.getCommand("remindpassword"))
                .setExecutor(new CommandSendPasswordReminder(authManager, stringUtils));
        Objects.requireNonNull(this.getCommand("cpw")).setExecutor(
                new CommandChangePassword(authManager, stringUtils));
        Objects.requireNonNull(this.getCommand("cpr")).setExecutor(new CommandChangePasswordReminder(authManager));
        logger.info(ANSI_GREEN + "[SimpleAuth2] Plugin has been launched successfully" + ANSI_RESET);
    }

    @Override
    public void onDisable() {
        // TODO: make disable plugin logic
    }
}
