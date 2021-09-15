package com.christolis.tbtcore.game.tbt;

import com.christolis.tbtcore.Main;
import com.christolis.tbtcore.game.Game;
import com.christolis.tbtcore.game.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TBGame extends Game {

    private final Mode mode;
    private int timerID;

    private long countdownStart;
    private static final int COUNTDOWN_SECONDS = 10;

    public TBGame(Map map, String mode) {
        super(map);

        this.mode = Mode.valueOf(mode);
        this.maxPlayers = getMode().getMaxPlayers();
    }

    @Override
    public void tick() {
    }

    @Override
    public void ready() {
        countdown();
    }

    @SuppressWarnings("deprecation") // Cause there isn't a better method.
    private void countdown() {
        // Countdown
        this.getPlayers().forEach(player -> {
            player.getPlayer().sendMessage(ChatColor.GOLD + "Match is starting...");
        });

        countdownStart = System.currentTimeMillis();
        timerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.instance, () -> {
            int secsElapsed = (int) Math.floor(System.currentTimeMillis() - countdownStart) / 1000;
            int secsRemaining = COUNTDOWN_SECONDS - secsElapsed;

            switch (secsRemaining) {
                case 4: case 5: {
                    this.getPlayers().forEach(tbtPlayer -> {
                        Player player = tbtPlayer.getPlayer();
                        player.sendTitle(ChatColor.YELLOW + "" + secsRemaining, "");
                        player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1f, 1f);
                    });
                    break;
                }
                case 1: case 2: case 3: {
                    this.getPlayers().forEach(tbtPlayer -> {
                        Player player = tbtPlayer.getPlayer();
                        player.sendTitle(ChatColor.RED + "" + secsRemaining, "");
                        player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1f, 1f);
                    });
                    break;
                }
                case 0: {
                    this.getPlayers().forEach(tbtPlayer -> {
                        Player player = tbtPlayer.getPlayer();

                        player.playSound(player.getLocation(), Sound.BURP, 1f, 1f);
                    });
                    start();
                }
            }
        }, 1L, 20L);
    }

    private void start() {
        Bukkit.getScheduler().cancelTask(timerID);

    }
    public Mode getMode() {
        return mode;
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
