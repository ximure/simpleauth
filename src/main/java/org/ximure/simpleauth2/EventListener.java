package org.ximure.simpleauth2;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.ximure.simpleauth2.auth.AuthManager;

import java.util.UUID;

public class EventListener implements Listener {
    // TODO: a lot of listeners to check if what player can do while logged in, registered etc
    private final AuthManager authManager;
    private final StringUtils stringUtils;

    public EventListener(AuthManager authManager, StringUtils stringUtils) {
        this.authManager = authManager;
        this.stringUtils = stringUtils;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        GameMode currentGameMode = player.getGameMode();
        String loginMessage = stringUtils.getString("login_message");
        String registerMessage = stringUtils.getString("register_message");
        // storing previous player gamemode to restore if after registration/login
        authManager.saveGameMode(playerUUID, currentGameMode);
        // enabling spectator gamemode to use less event listeners
        player.setGameMode(GameMode.SPECTATOR);
        if (authManager.isRegistered(playerUUID)) {
            player.sendMessage(loginMessage);
        } else {
            player.sendMessage(registerMessage);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        authManager.setOffline(playerUUID);
    }
}
