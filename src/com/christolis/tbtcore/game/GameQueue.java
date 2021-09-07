package com.christolis.tbtcore.game;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.christolis.tbtcore.Main;
import com.christolis.tbtcore.game.Game.Mode;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GameQueue {

    public final ArrayList<Game> games = new ArrayList<>();
    public final HashMap<Mode, GameInfo> gameInfo = new HashMap<>();

    public GameQueue() {
        for (Mode mode : Mode.values()) {
            gameInfo.put(mode, new GameInfo());
        }
    }

    public void addPlayer(TBTPlayer tbtplayer, Mode mode) {
        // Find game
        Player player = tbtplayer.getPlayer();
        AtomicBoolean foundGame = new AtomicBoolean(false);

        if (tbtplayer.getGame() != null) {
            player.sendMessage(ChatColor.RED + "You are already in a game!");
            return;
        }

        games.forEach((game) -> {
            if (game.getMode() == mode && game.addPlayer(tbtplayer)) {
                player.sendMessage(ChatColor.GREEN + 
                        "Found match! You have been sent to session " +
                        game.getMap().getID() + "...");

                foundGame.set(true);
            }
        });

        if (!foundGame.get()) {
            Game game = createGame(mode);
            game.addPlayer(tbtplayer);
        }
    }

    private Game createGame(Mode mode) {
        try {
            // Temporary; for testing purposes
            String path = Main.instance.getDataFolder().getPath() +
                File.separator + "maps" + File.separator + "Aquatica";
            File testMap = new File(path); 

            Game game = new Game(new Map(testMap), mode);
            games.add(game);
            return game;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public class GameInfo {
        private int playersQueued;
        private int roomRemaining;

        public int getPlayersQueued() {
            return playersQueued;
        }
        
        public int getRoomRemaining() {
            return roomRemaining;
        }
    }
}
