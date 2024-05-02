package com.ambrosia.add.database.casino.game;

import com.ambrosia.add.database.casino.GameResultAggregate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

public final class CasinoGamesCountByName {

    public final String name;
    public final Map<String, CasinoGameCountByConclusion> conclusionToGame = new HashMap<>();

    public int count = 0;
    public int deltaWinnings = 0;
    public long totalExchanged = 0;

    public CasinoGamesCountByName(String name) {
        this.name = name;
    }

    public void put(GameResultAggregate queryEntry) {
        count += queryEntry.count;
        deltaWinnings += queryEntry.deltaWinnings;
        totalExchanged += Math.abs(queryEntry.deltaWinnings);
        conclusionToGame.put(queryEntry.conclusion, new CasinoGameCountByConclusion(queryEntry.count, queryEntry.deltaWinnings));
    }

    public Double houseEdge() {
        if (totalExchanged == 0) return null;
        BigDecimal deltaWinnings = BigDecimal.valueOf(-this.deltaWinnings); // we're House, so flip the sign
        BigDecimal totalExchanged = BigDecimal.valueOf(this.totalExchanged);

        BigDecimal houseWins = deltaWinnings.divide(totalExchanged, Double.SIZE, RoundingMode.HALF_UP);
        return houseWins.multiply(BigDecimal.TEN).multiply(BigDecimal.TEN).doubleValue();
    }

    public Field houseEdgeDisplay() {
        Double houseEdge = this.houseEdge();
        if (houseEdge == null) return new Field("House Edge", "No data", true);
        String edgeMessage = String.format("%.3f%%", houseEdge);
        return new Field("House Edge", edgeMessage, true);
    }
}
