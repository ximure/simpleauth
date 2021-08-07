package org.ximure.simpleauth;

import org.bukkit.GameMode;

import java.util.UUID;

public class PlayerStatus {
    private boolean loggedIn;
    private UUID UUID;
    private GameMode gameMode;

    public PlayerStatus(boolean loggedIn, java.util.UUID UUID, GameMode gameMode) {
        this.loggedIn = loggedIn;
        this.UUID = UUID;
        this.gameMode = gameMode;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public java.util.UUID getUUID() {
        return UUID;
    }

    public void setUUID(java.util.UUID UUID) {
        this.UUID = UUID;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }
}
