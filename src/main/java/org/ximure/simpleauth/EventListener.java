package org.ximure.simpleauth;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.ximure.simpleauth.misc.Utils;

import java.util.UUID;

public class EventListener implements Listener {
    private final AuthManager authManager;
    private final Utils utils;

    public EventListener(AuthManager authManager, Utils utils) {
        this.authManager = authManager;
        this.utils = utils;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        GameMode currentGameMode = player.getGameMode();
        String message = utils.getStringFromYml(authManager.isRegistered(playerUUID) ? "login_message" : "register_message");
        // saving current player's gamemode before switching it to spectator to restore it later
        authManager.saveGameMode(playerUUID, currentGameMode);
        // enabling spectator gamemode to use less event listeners
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(message);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        // restoring saved player gamemode in case he didn't log in or register
        if (!authManager.isOnline(playerUUID)) {
            GameMode playerGameMode = authManager.restoreGameMode(playerUUID);
            player.setGameMode(playerGameMode);
        }
        authManager.setOffline(playerUUID);
    }

    // While player didn't register or login, he'll be unable to move
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        event.setCancelled(!authManager.isOnline(playerUUID));
    }

    // While player didn't register or login, he'll be unable to send message to chat
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        event.setCancelled(!authManager.isOnline(playerUUID));
    }

    // While player didn't register or login, he'll be unable to send any commands, except /register and /login
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        UUID playerUUID = player.getUniqueId();
        String notRegisteredOrLoggedIn = utils.getStringFromYml("no_command");
        if (!authManager.isOnline(playerUUID)) {
            boolean allowedCommands = message.contains("/login") || message.contains("/register") ||
                    message.contains("/remindpassword");
            if (allowedCommands) {
                event.setCancelled(false);
            } else {
                player.sendMessage(notRegisteredOrLoggedIn);
                event.setCancelled(true);
            }
        }
    }

    // Blocking player from...
    // Using RMB on something (opening chests, shear sheep etc)
    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        event.setCancelled(!authManager.isOnline(playerUUID));
    }

    // Dropping items
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        event.setCancelled(!authManager.isOnline(playerUUID));
    }

    // Doing something with items in inventory at all
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        UUID playerUUID = event.getWhoClicked().getUniqueId();
        event.setCancelled(!authManager.isOnline(playerUUID));
    }

    // Teleporting to players in spectator mode while he's not registered or logged in
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.SPECTATE) && !authManager.isOnline(playerUUID)) {
            event.setCancelled(true);
        }
    }
}
