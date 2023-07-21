package com.ambrosia.roulette.game.player;

import com.ambrosia.add.discord.util.Emeralds;

public record RoulettePartialBet(int amount, RoulettePlayerGame player) {

    public String display() {
        return Emeralds.message(amount(), true);
    }

}
