package com.ambrosia.roulette.game.player;

import com.ambrosia.add.database.game.GameResultEntity;
import com.ambrosia.roulette.Roulette;

public class RouletteGameResult {
    
    public static GameResultEntity result(RoulettePlayerGame game, int betTotal, int change) {
        game.getBets();

        String conclusion;
        if (0 > change) conclusion = "LOSE";
        else if (0 == change) conclusion = "BREAK_EVEN";
        else conclusion = "WIN";
        GameResultEntity result = new GameResultEntity(Roulette.GAME_NAME);
        result.conclusion = conclusion;
        result.deltaWinnings = change;
        result.originalBet = betTotal;
        return result;
    }
}
