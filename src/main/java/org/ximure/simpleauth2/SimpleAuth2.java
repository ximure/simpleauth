package org.ximure.simpleauth2;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.ximure.simpleauth2.commands.*;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

public final class SimpleAuth2 extends JavaPlugin {
    private final Logger logger = Bukkit.getLogger();
    private final SqlManager sqlManager = new SqlManager();
    private final PlayerStatus playerStatus = new PlayerStatus();
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
                Bukkit.getPluginManager().disablePlugin(new SimpleAuth2());
                // TODO: check if this^ works
            }
        }
        // checking if database exist and create it if not
        if (!PASSWORDS_DATABASE.exists()) {
            // TODO: ПРОВЕРИТЬ ЕСЛИ ВСЁ ПОЛОМАЕТСЯ НАХУЙ ЕСЛИ НЕ ЗАКРЫТЬ КОННЕКТ
            if (!sqlManager.createDatabase()) {
                logger.info(ANSI_RED + "[SimpleAuth2] Database cannot be created" + ANSI_RESET);
                Bukkit.getPluginManager().disablePlugin(new SimpleAuth2());
                // TODO: check if this^ works also
            }
            if (!sqlManager.createPasswordsTable()) {
                logger.info(ANSI_RED + "[SimpleAuth2] Passwords table cannot be created" + ANSI_RESET);
                Bukkit.getPluginManager().disablePlugin(new SimpleAuth2());
                // TODO: check if this^ works too
            }
        }
        // registering event handler and commands
        getServer().getPluginManager().registerEvents(new EventListener(playerStatus, sqlManager), this);
        Objects.requireNonNull(this.getCommand("login")).setExecutor(new CommandLogin(playerStatus, sqlManager));
        Objects.requireNonNull(this.getCommand("register")).setExecutor(new CommandRegister(playerStatus, sqlManager));
        Objects.requireNonNull(this.getCommand("remindpassword")).setExecutor(new CommandSendPasswordReminder(sqlManager));
        Objects.requireNonNull(this.getCommand("cpw")).setExecutor(new CommandChangePassword(sqlManager));
        Objects.requireNonNull(this.getCommand("cpr")).setExecutor(new CommandChangePasswordReminder(sqlManager));
        logger.info(ANSI_GREEN + "[SimpleAuth2] Plugin has been launched successfully" + ANSI_RESET);
    }

    @Override
    public void onDisable() {
        // TODO: make disable plugin logic
    }
}
