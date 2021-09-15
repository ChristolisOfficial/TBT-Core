package com.christolis.tbtcore.scoreboard;

import com.christolis.scoreboard.Scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TBScoreboard extends Scoreboard {

    public TBScoreboard() {
        super(ChatColor.YELLOW + "" + ChatColor.BOLD + "THE BRIDGES", 5);
    }

    @Override
    public void playerInit(Player player) {
        this.setPlayerLine(2, ChatColor.BLUE + "" + player.getName(), player);
    }

    @Override
    public void init() {
        this.setLine(1, ChatColor.GOLD + "test");
    }

    @Override
    public void tick() {
    }
}
