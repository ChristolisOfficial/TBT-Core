package com.christolis.tbtcore.game;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import com.christolis.tbtcore.Main;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * GameQueue.java
 *
 * Represents an instance of a game queue
 * that can hold different types of games.
 */
public class GameQueue {

    /**
     * Holds a list of all the active games.
     */
    public final ArrayList<Game> games = new ArrayList<>();

    /**
     * Adds a player into the queue.
     *
     * @param tbtplayer The player to put into the queue.
     * @param gameType The type of the game that the player is queuing for.
     * @param mode The game mode that the player is queuing for.
     */
    public void addPlayer(TBTPlayer tbtplayer,
            Class<? extends Game> gameType,
            String mode) {
        // Find game
        Player player = tbtplayer.getPlayer();
        AtomicBoolean foundGame = new AtomicBoolean(false);

        if (tbtplayer.getGame() != null) {
            player.sendMessage(ChatColor.RED + "You are already in a game!");
            return;
        }

        games.forEach((game) -> {
            if (doModesMatch(mode, game) && game.addPlayer(tbtplayer)) {
                player.sendMessage(ChatColor.GREEN + 
                        "Found match! You have been sent to session " +
                        game.getMap().getID() + "...");

                foundGame.set(true);
            }
        });

        if (!foundGame.get()) {
            Game game = createGame(gameType, mode);
            game.addPlayer(tbtplayer);
        }
    }

    /**
     * Creates a new {@link Game} instance.
     *
     * This should mostly get executed whenever there are
     * no available games of the requested mode. The newly
     * created game gets automatically added into the list
     * of active games.
     *
     * @param gameType A class literal of the type of game to be made.
     * @param mode The mode of the game to be made.
     * @return The instance of the newly-created game.
     */
    private Game createGame(Class<? extends Game> gameType, String mode) {
        try {
            // Temporary; for testing purposes
            String path = Main.instance.getDataFolder().getPath() +
                File.separator + "maps" + File.separator + "Aquatica";
            File testMap = new File(path); 

            Map map = new Map(testMap);
            Game game = 
                (Game) gameType.getConstructors()[0].newInstance(map, mode);
            games.add(game);
            return game;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if a given mode matches a running game's mode.
     *
     * @param mode The given mode.
     * @param game The game for the given mode to be checked against.
     * @return If the modes match.
     */
    private static boolean doModesMatch(final String mode, Game game) {
        try {
            Field modeField = game.getClass().getDeclaredField("mode");
            modeField.setAccessible(true);

            Object modeFieldValue = modeField.get(game);
            Class<?> _class = Class.forName(game.getClass().getName() + "$Mode");

            if (modeField.getType().isEnum()) {
                String gameMode = 
                    (String) _class.getMethod("name").invoke(modeFieldValue);

                return mode.equalsIgnoreCase(gameMode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
