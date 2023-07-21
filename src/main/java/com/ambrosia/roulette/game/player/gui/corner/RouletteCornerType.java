package com.ambrosia.roulette.game.player.gui.corner;

public enum RouletteCornerType {
    HIGH,
    LOW;

    public int cornerRow() {
        return this == LOW ? 0 : 1;
    }
}
