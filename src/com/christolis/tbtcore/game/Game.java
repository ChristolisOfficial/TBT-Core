package com.christolis.tbtcore.game;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Game.java
 *
 * Represents an instance of a game (any type)
 */
public class Game {

    private Map map;
    private final Mode mode;

    private int remainingPlayers;
    private final ArrayList<TBTPlayer> players = new ArrayList<>();

    public Game(Map map, Mode mode) {
        this.map = map; // TODO: Randomly construct map
        this.mode = mode;
        this.remainingPlayers = mode.getMaxPlayers();
    }

    public Map getMap() {
        return map;
    }

    public Mode getMode() {
        return mode;
    }

    public int getRemainingPlayers() {
        return remainingPlayers;
    }

    public ArrayList<TBTPlayer> getPlayers() {
        return players;
    }

    public boolean addPlayer(TBTPlayer player) {
        if (getPlayers().contains(player)) return false;
        if (remainingPlayers > 0 && remainingPlayers <= this.getMode().getMaxPlayers()) {
            getPlayers().add(player);
            player.setGame(this);
            player.setLastLocation(player.getPlayer().getLocation().clone());
            player.getPlayer().teleport(getMap().getCenterLocation());
            this.remainingPlayers--;
            return true;
        }
        return false;
    }

    public void removePlayer(TBTPlayer player, boolean freePosition) {
        player.setGame(null);
        getPlayers().remove(player);

        player.getPlayer().teleport(player.getLastLocation());

        if (freePosition)
            this.remainingPlayers++;
    }

    /**
     * Will completely shutdown the game and take
     * care of kicking all players and removing
     * the appropriate files.
     */
    public void shutdown() {
        map.shutdown();
    }

    public enum Mode {
        ONE(2), TWO(4), FOUR(8);

        private final int maxPlayers;

        private Mode(int maxPlayers) {
            this.maxPlayers = maxPlayers;
        }

        public int getMaxPlayers() {
            return maxPlayers;
        }
    }
}
