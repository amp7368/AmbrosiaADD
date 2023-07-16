package com.ambrosia.roulette.game.bet;

import com.ambrosia.add.discord.util.Emeralds;
import com.ambrosia.roulette.game.bet.types.RouletteBetType;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public class RouletteBet {

    public RouletteBetType type;
    public int betAmount;

    public RouletteBetType getType() {
        return this.type;
    }

    public int getAmount() {
        return this.betAmount;
    }

    public Field toDiscordField() {
        String description = Emeralds.message(getAmount(), true);
        return new Field(getType().displayName(), description, false);
    }
}
