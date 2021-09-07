package com.christolis.tbtcore.command;

import com.christolis.tbtcore.Main;
import com.christolis.tbtcore.game.TBTPlayer;
import com.christolis.tbtcore.game.Game.Mode;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTBT extends Command {

    public CommandTBT() {
        super("tbt");
    }

    @Override
    public boolean execute(CommandSender sender, String cmd, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        TBTPlayer tbtplayer = TBTPlayer.find(player);

        if (args.length == 1) {
            if (args[0].toLowerCase().equals("leave")) {
                if (tbtplayer.getGame() != null) {
                    tbtplayer.getGame().removePlayer(tbtplayer, true);
                } else {
                    player.sendMessage(ChatColor.RED + "You are not in a game!");
                }
            }
        }
        if (args.length == 2) {
            if (args[0].toLowerCase().equals("queue")) {
                if (args[1].toLowerCase().equals("1v1")) {
                    if (tbtplayer != null) {
                        Main.gameQueue.addPlayer(tbtplayer, Mode.ONE);
                    }
                }
            }
        }
        return true;
    }
}
