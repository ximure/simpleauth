package org.ximure.simpleauth2;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.ximure.simpleauth2.auth.AuthManager;
import org.ximure.simpleauth2.commands.*;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

public final class SimpleAuth2 extends JavaPlugin {
    public static final File PASSWORDS_DATABASE = new File("./plugins/SimpleAuth2/passwords.db");
    public static final File PLUGIN_FOLDER = new File("./plugins/SimpleAuth2");
    public static final File MESSAGES_YAML = new File("./plugins/SimpleAuth2/messages.yml");
    public final String ANSI_GREEN = "\u001B[32m";
    public final String ANSI_RED = "\u001B[31m";
    public final String ANSI_RESET = "\u001B[0m";
    private final AuthManager authManager = new AuthManager();
    private final MessagesUtils messagesUtils = new MessagesUtils();
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
            if (!authManager.sqlCreateDatabase()) {
                logger.info(ANSI_RED + "[SimpleAuth2] Database cannot be created. Maybe something wrong with" +
                        "folder permissions?" + ANSI_RESET);
                // TODO: plugin disabling
            }
            if (!authManager.sqlCreatePasswordsTable()) {
                logger.info(ANSI_RED + "[SimpleAuth2] Passwords table cannot be created" + ANSI_RESET);
                // TODO: plugin disabling
            }
        }
        // if messages template config does not exist - this block will create it
        if (!MESSAGES_YAML.exists()) {
            if (!messagesUtils.createTemplate()) {
                logger.info(ANSI_RED + "[SimpleAuth2] Messages config template cannot be created" + ANSI_RESET);
                // TODO: plugin disabling
            }
        }
        // registering event handler and commands
        getServer().getPluginManager().registerEvents(new EventListener(authManager, messagesUtils), this);
        Objects.requireNonNull(this.getCommand("login"))
                .setExecutor(new CommandLogin(authManager, messagesUtils));
        Objects.requireNonNull(this.getCommand("register"))
                .setExecutor(new CommandRegister(authManager, messagesUtils));
        Objects.requireNonNull(this.getCommand("remindpassword"))
                .setExecutor(new CommandSendPasswordReminder(authManager, messagesUtils));
        Objects.requireNonNull(this.getCommand("cpw"))
                .setExecutor(new CommandChangePassword(authManager, messagesUtils));
        Objects.requireNonNull(this.getCommand("cpr"))
                .setExecutor(new CommandChangePasswordReminder(authManager, messagesUtils));
        logger.info(ANSI_GREEN + "[SimpleAuth2] Plugin has been launched successfully" + ANSI_RESET);
    }

    @Override
    public void onDisable() {
        // TODO: make disable plugin logic
    }
}
