package com.christolis.tbtcore.game;

import java.util.ArrayList;

import com.christolis.tbtcore.scoreboard.TBScoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Game.java
 *
 * Represents an instance of a game (any type)
 */
public abstract class Game {

    private Map map;
    private boolean started;

    protected int maxPlayers;

    private final ArrayList<TBTPlayer> players = new ArrayList<>();

    public Game(Map map) {
        this.map = map; // TODO: Randomly construct maps.
        this.started = false;
    }

    public Map getMap() {
        return map;
    }

    public boolean hasStarted() {
        return started;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public ArrayList<TBTPlayer> getPlayers() {
        return players;
    }

    protected abstract void ready();
    protected abstract void tick();

    public boolean addPlayer(TBTPlayer player) {
        if (players.contains(player)) return false;
        if (players.size() + 1 <= this.maxPlayers) {
            players.add(player);

            player.setGame(this);
            player.setLastLocation(player.getPlayer().getLocation().clone());
            player.getPlayer().teleport(getMap().getCenterLocation());

            if (!this.hasStarted()) {
                for (TBTPlayer tbtplayers : this.getPlayers()) {
                    Player bukkitPlayers = tbtplayers.getPlayer();
                    String msg = ChatColor.BLUE + player.getPlayer().getName() +
                        ChatColor.YELLOW + " has joined (" + ChatColor.AQUA +
                        players.size() + ChatColor.YELLOW + "/" + ChatColor.AQUA +
                        this.maxPlayers + ChatColor.YELLOW + ")!";

                    bukkitPlayers.sendMessage(msg);
                }
            }

            // Test stuff
            TBScoreboard scoreboard = new TBScoreboard();
            getPlayers().forEach(playerr -> scoreboard.addViewer(playerr.getPlayer()));

            // Game is full
            if (players.size() == this.maxPlayers) {
                ready();
            }
            return true;
        }
        return false;
    }

    public void removePlayer(TBTPlayer player) {
        player.setGame(null);
        getPlayers().remove(player);

        player.getPlayer().teleport(player.getLastLocation());

        if (!this.hasStarted()) {
            for (TBTPlayer tbtplayers : this.getPlayers()) {
                Player bukkitPlayers = tbtplayers.getPlayer();
                String msg = ChatColor.BLUE + player.getPlayer().getName() +
                    ChatColor.YELLOW + " has left (" + ChatColor.AQUA +
                    players.size() + ChatColor.YELLOW + "/" + ChatColor.AQUA +
                    this.maxPlayers + ChatColor.YELLOW + ")!";

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

}
