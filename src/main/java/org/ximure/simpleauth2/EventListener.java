package org.ximure.simpleauth2;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class EventListener implements Listener {
    // TODO: a lot of listeners to check if what player can do while logged in, registered etc
    private final PlayerStatus playerStatus;
    private final SqlManager sqlManager;

    public EventListener(PlayerStatus playerStatus, SqlManager sqlManager) {
        this.playerStatus = playerStatus;
        this.sqlManager = sqlManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        GameMode gameMode = player.getGameMode();
        // storing previous player gamemode to restore if after registration/login
        playerStatus.setGameMode(playerUUID, gameMode);
        // enabling spectator gamemode to use less event listeners
        player.setGameMode(GameMode.SPECTATOR);
        if (sqlManager.isRegistered(playerUUID)) {
            player.sendMessage("[Pure] Введите ваш пароль");
        } else {
            player.sendMessage("[Pure] Зарегистрируйтесь с помощью /register пароль подсказка_к_паролю");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        playerStatus.setOffline(playerUUID);
    }
}
