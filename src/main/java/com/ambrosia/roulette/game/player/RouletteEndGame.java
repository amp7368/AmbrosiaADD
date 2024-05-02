package com.ambrosia.roulette.game.player;

import com.ambrosia.add.api.CreditReservation;
import com.ambrosia.add.database.game.GameResultEntity;
import com.ambrosia.add.database.game.roulette.DRouletteTableGame;
import com.ambrosia.add.database.game.roulette.RouletteGameApi;
import com.ambrosia.roulette.Roulette;
import io.ebean.DB;
import io.ebean.Transaction;

public class RouletteEndGame {

    private static GameResultEntity result(RoulettePlayerGame game) {
        String conclusion;
        int change = game.getWinnings().getChangeTotal();
        if (0 > change) conclusion = "LOSE";
        else if (0 == change) conclusion = "BREAK_EVEN";
        else conclusion = "WIN";
        GameResultEntity result = new GameResultEntity(Roulette.GAME_NAME);
        result.conclusion = conclusion;
        result.deltaWinnings = change;
        result.originalBet = game.getBetTotal();
        return result;
    }

    public static void endGame(RoulettePlayerGame game, DRouletteTableGame dbTableGame, CreditReservation creditReservation) {
        GameResultEntity result = result(game);

        try (Transaction transaction = DB.beginTransaction()) {
            creditReservation.release(result, transaction);
            RouletteGameApi.addPlayer(dbTableGame, game, result, transaction);
            transaction.commit();
        }

    }
}
