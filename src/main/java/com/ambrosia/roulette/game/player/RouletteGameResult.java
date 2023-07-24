package com.ambrosia.roulette.game.player;

import com.ambrosia.add.database.game.GameResultEntity;
import com.ambrosia.roulette.Roulette;

public class RouletteGameResult {

    public static GameResultEntity result(int betTotal, int winnings) {
        String conclusion;
        if (betTotal > winnings) conclusion = "LOSE";
        else if (betTotal == winnings) conclusion = "BREAK_EVEN";
        else conclusion = "WIN";
        GameResultEntity result = new GameResultEntity(Roulette.GAME_NAME);
        result.conclusion = conclusion;
        result.deltaWinnings = winnings - betTotal;
        result.originalBet = betTotal;
        return result;
    }
}
