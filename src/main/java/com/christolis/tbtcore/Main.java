package com.christolis.tbtcore;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.christolis.scoreboard.Scoreboard;
import com.christolis.tbtcore.command.*;
import com.christolis.tbtcore.game.Game;
import com.christolis.tbtcore.game.GameQueue;
import com.christolis.tbtcore.game.TBTPlayer;

public class Main extends JavaPlugin implements Listener {

    public static Main instance;

    public static GameQueue gameQueue = new GameQueue();

    private static Set<Command> commands = new HashSet<>();
    {
        commands.add(new CommandTBT());
    }

    @Override
    public void onEnable() {
        instance = this;
        Scoreboard.setPlugin(instance);
        Bukkit.getPluginManager().registerEvents(this, this);

        // Re-create the TBTPlayer wrapper instances
        Bukkit.getOnlinePlayers().forEach(player -> {
            new TBTPlayer(player);
        });
    }

    @Override
    public void onDisable() {
        for (Game game : gameQueue.games) {
            game.shutdown();
        }

        gameQueue.games.clear();
        TBTPlayer.players.clear();
    }

    // @ChristolisOfficial: Desperately looking for a better way
    // to go about this.
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        new TBTPlayer(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TBTPlayer.remove(player);
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {
        for (Command cmd : commands) {
            AtomicBoolean foundAlias = new AtomicBoolean(false);
            cmd.getAliases().forEach(alias -> {
                if (alias.equals(label.toLowerCase())) {
                    cmd.execute(sender, label, args);
                    foundAlias.set(true);
                }
            });

            if (foundAlias.get()) return true;
            if (cmd.getLabel().equals(label.toLowerCase())) {
                cmd.execute(sender, label, args);
                return true;
            }
        }
        return true;
    }
    
}
