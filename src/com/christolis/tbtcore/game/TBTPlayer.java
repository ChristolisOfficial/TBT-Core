package com.christolis.tbtcore.game;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TBTPlayer {

    public static final Map<Player, TBTPlayer> players = new HashMap<>();

    /**
     * This helps in teleporting the player back to the
     * main world (or lobby) after a game has finished 
     */
    private Location lastLocation;

    private final Player player;
    private Game game;

    public TBTPlayer(final Player player) {
        this.player = player;
        players.put(player, this);
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public static TBTPlayer find(Player player) {
        return players.get(player);
    }

    public static void remove(Player player) {
        players.remove(player);
    }
}
