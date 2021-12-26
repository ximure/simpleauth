package org.ximure.simpleauth;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.ximure.simpleauth.auth.AuthManager;

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
        // if player is dead on login we'll put him inside of map with dead on login player
        // to check if he was dead in login/register commands to not teleport him to the location
        // of dead :>
        if (player.isDead()) {
            authManager.putDeadOnLogin(playerUUID);
        }
        GameMode currentGameMode = player.getGameMode();
        Location loginPlayerLocation = player.getLocation();
        authManager.setPlayerLoginLocation(playerUUID, loginPlayerLocation);
        String loginMessage = utils.getString("login_message");
        String registerMessage = utils.getString("register_message");
        // saving current player's gamemode before switching it to spectator to restore it later
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
        // removing player from dead on login map
        authManager.removeDeadOnLogin(playerUUID);
        // restoring saved player gamemode in case he didn't log in or register
        if (!authManager.isOnline(playerUUID)) {
            GameMode playerGameMode = authManager.restoreGameMode(playerUUID);
            player.setGameMode(playerGameMode);
        }
        authManager.setOffline(playerUUID);
        authManager.removePlayerLocation(playerUUID);
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
        String notRegisteredOrLoggedIn = utils.getString("no_command");
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
    // Using RMB on something (opening chests, shear sheep, etc)
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
