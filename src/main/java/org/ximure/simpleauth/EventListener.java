package org.ximure.simpleauth;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;
import org.ximure.simpleauth.auth.AuthManager;
import org.ximure.simpleauth.models.PlayerStats;

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
        // writing all needed player status
        Vector playerVelocity = player.getVelocity();
        GameMode playerGamemode = player.getGameMode();
        Boolean isDead = player.isDead();
        Location playerLocation = player.getLocation();
        PlayerStats playerStats = new PlayerStats(playerVelocity, playerGamemode, isDead, playerLocation);
        // adding this player stats to restore it later
        authManager.addPlayerStats(playerUUID, playerStats);
        // getting login/register messages
        String loginMessage = utils.getString("login_message");
        String registerMessage = utils.getString("register_message");
        // enabling spectator gamemode to use less event listeners and sending one of the above messages
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
        // restoring saved player gamemode in case he didn't log in or register
        if (!authManager.isOnline(playerUUID)) {
            player.setGameMode(authManager.getPlayerStats(playerUUID).getLoginGamemode());
        }
        // setting him as offline and removing all his login stats
        authManager.setOffline(playerUUID);
        authManager.removePlayerStats(playerUUID);
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
