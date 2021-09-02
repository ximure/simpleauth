package org.ximure.simpleauth;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.ximure.simpleauth.commands.*;
import org.ximure.simpleauth.misc.Utils;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

public final class SimpleAuth extends JavaPlugin {
    public static final File PASSWORDS_DATABASE = new File("./plugins/SimpleAuth/passwords.db");
    public static final File PLUGIN_FOLDER = new File("./plugins/SimpleAuth");
    public static final File MESSAGES_YAML = new File("./plugins/SimpleAuth/messages.yml");
    public final String ANSI_GREEN = "\u001B[32m";
    public final String ANSI_RED = "\u001B[31m";
    public final String ANSI_RESET = "\u001B[0m";
    private final AuthManager authManager = new AuthManager();
    private final Logger logger = Bukkit.getLogger();
    private final Utils utils = new Utils();

    @Override
    public void onEnable() {
        // if plugin folder does not exist - this block will create it
        if (!PLUGIN_FOLDER.exists()) {
            if (!PLUGIN_FOLDER.mkdir()) {
                logger.info(ANSI_RED + "[SimpleAuth] Plugin folder cannot be created. Maybe something wrong " +
                        "with folder permissions?" + ANSI_RESET);
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
        // if database does not exist - this block will create it
        if (!PASSWORDS_DATABASE.exists()) {
            if (!authManager.createDatabase()) {
                logger.info(ANSI_RED + "[SimpleAuth] Database cannot be created. Maybe something wrong with" +
                        "folder permissions?" + ANSI_RESET);
                Bukkit.getPluginManager().disablePlugin(this);
            }
            if (!authManager.createPasswordsTable()) {
                logger.info(ANSI_RED + "[SimpleAuth] Passwords table cannot be created" + ANSI_RESET);
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
        // if messages template config does not exist - this block will create it
        if (!MESSAGES_YAML.exists()) {
            if (!utils.createYmlTemplate()) {
                logger.info(ANSI_RED + "[SimpleAuth] Messages config template cannot be created" + ANSI_RESET);
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }
        // registering event handler and commands
        getServer().getPluginManager().registerEvents(new EventListener(authManager, utils), this);
        Objects.requireNonNull(this.getCommand("login"))
                .setExecutor(new CommandLogin(authManager, utils));
        Objects.requireNonNull(this.getCommand("register"))
                .setExecutor(new CommandRegister(authManager, utils));
        Objects.requireNonNull(this.getCommand("remindpassword"))
                .setExecutor(new CommandSendPasswordReminder(authManager, utils));
        Objects.requireNonNull(this.getCommand("cpw"))
                .setExecutor(new CommandChangePassword(authManager, utils));
        Objects.requireNonNull(this.getCommand("cpr"))
                .setExecutor(new CommandChangePasswordReminder(authManager, utils));
        logger.info(ANSI_GREEN + "[SimpleAuth] Plugin has been launched successfully" + ANSI_RESET);
    }

    @Override
    public void onDisable() {
        authManager.clearGameModes();
        authManager.clearOnlineStatuses();
    }
}
