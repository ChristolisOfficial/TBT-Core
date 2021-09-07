package com.christolis.tbtcore.game;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Game.java
 *
 * Represents an instance of a game (any type)
 */
public class Game {

    private Map map;
    private final Mode mode;
    private boolean started;

    private int remainingPlayers;
    private final ArrayList<TBTPlayer> players = new ArrayList<>();

    public Game(Map map, Mode mode) {
        this.map = map; // TODO: Randomly construct map
        this.mode = mode;
        this.started = false;
        this.remainingPlayers = mode.getMaxPlayers();
    }

    public Map getMap() {
        return map;
    }

    public Mode getMode() {
        return mode;
    }

    public boolean hasStarted() {
        return started;
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

            if (!this.hasStarted()) {
                for (TBTPlayer tbtplayers : this.getPlayers()) {
                    Player bukkitPlayers = tbtplayers.getPlayer();
                    String msg = ChatColor.BLUE + player.getPlayer().getName() +
                        ChatColor.YELLOW + " has joined (" + ChatColor.AQUA +
                        players.size() + ChatColor.YELLOW + "/" + ChatColor.AQUA +
                        getMode().getMaxPlayers() + ChatColor.YELLOW + ")!";

                    bukkitPlayers.sendMessage(msg);
                }
            }
            return true;
        }
        return false;
    }

    public void removePlayer(TBTPlayer player) {
        player.setGame(null);
        getPlayers().remove(player);

        player.getPlayer().teleport(player.getLastLocation());

        this.remainingPlayers++;

        if (!this.hasStarted()) {
            for (TBTPlayer tbtplayers : this.getPlayers()) {
                Player bukkitPlayers = tbtplayers.getPlayer();
                String msg = ChatColor.BLUE + player.getPlayer().getName() +
                    ChatColor.YELLOW + " has left (" + ChatColor.AQUA +
                    players.size() + ChatColor.YELLOW + "/" + ChatColor.AQUA +
                    getMode().getMaxPlayers() + ChatColor.YELLOW + ")!";

                bukkitPlayers.sendMessage(msg);
            }
        }
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
