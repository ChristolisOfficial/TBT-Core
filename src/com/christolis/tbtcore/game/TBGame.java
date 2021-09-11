package com.christolis.tbtcore.game;

public class TBGame extends Game {

    private final Mode mode;

    public TBGame(Map map, String mode) {
        super(map);

        this.mode = Mode.valueOf(mode);
        this.maxPlayers = getMode().getMaxPlayers();
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
