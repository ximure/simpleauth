package org.ximure.simpleauth;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.ximure.simpleauth.commands.CommandLogin;
import org.ximure.simpleauth.commands.CommandPasswordReminder;
import org.ximure.simpleauth.commands.CommandRegister;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

public final class SimpleAuth extends JavaPlugin {
    private final Logger logger = Bukkit.getLogger();
    private final SqlManager sqlManager = new SqlManager();
    public final static File PLUGIN_FOLDER = new File("./plugins/SimpleAuth2");
    public final static File PASSWORDS_DATABASE = new File("./plugins/SimpleAuth2/passwords.db");
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    @Override
    public void onEnable() {
        // checking if plugin dir exists and if not - create it
        if (!PLUGIN_FOLDER.exists()) {
            if (!PLUGIN_FOLDER.mkdir()) {
                logger.info(ANSI_RED + "[SimpleAuth2] Plugin folder cannot be created" + ANSI_RESET);
                Bukkit.getPluginManager().disablePlugin(new SimpleAuth());
                // TODO: check if this^ works
            }
        }
        // checking if database exist and create it if not
        if (!PASSWORDS_DATABASE.exists()) {
            if (!sqlManager.createDatabase() && !sqlManager.createPasswordsTable()) {
                Bukkit.getPluginManager().disablePlugin(new SimpleAuth());
                // TODO: check if this^ works also
            }
        }
        // registering event handler and commands
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        Objects.requireNonNull(this.getCommand("login")).setExecutor(new CommandLogin());
        Objects.requireNonNull(this.getCommand("register")).setExecutor(new CommandRegister());
        Objects.requireNonNull(this.getCommand("remindpassword")).setExecutor(new CommandPasswordReminder());
        logger.info(ANSI_GREEN + "[SimpleAuth2] Plugin has been launched successfully" + ANSI_RESET);
    }

    @Override
    public void onDisable() {
        // TODO: make disable plugin logic
    }
}
