package com.ambrosia.add.database.game.roulette;

import com.ambrosia.add.database.game.GameResultEntity;
import com.ambrosia.roulette.game.bet.types.RouletteBet;
import com.ambrosia.roulette.game.player.RoulettePlayerGame;
import com.ambrosia.roulette.table.RouletteSpace;
import io.ebean.Transaction;

public interface RouletteGameApi {

    static DRouletteTableGame createTable(RouletteSpace spinResult) {
        DRouletteTableGame game = new DRouletteTableGame(spinResult);
        game.save();
        return game;
    }

    static DRoulettePlayerGame addPlayer(DRouletteTableGame dbTableGame, RoulettePlayerGame game, GameResultEntity result,
        Transaction transaction) {
        DRoulettePlayerGame player = new DRoulettePlayerGame(dbTableGame, game, result);
        dbTableGame.addPlayer(player);
        for (RouletteBet bet : game.getWinnings().getWinningBets()) {
            player.addBet(new DRoulettePlayerBet(player, bet, true));
        }
        for (RouletteBet bet : game.getWinnings().getLosingBets()) {
            player.addBet(new DRoulettePlayerBet(player, bet, false));
        }
        player.save(transaction);
        player.getBets().forEach(bet -> bet.save(transaction));
        return player;
    }
}
