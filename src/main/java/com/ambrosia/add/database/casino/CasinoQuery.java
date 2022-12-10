package com.ambrosia.add.database.casino;

import com.ambrosia.add.database.casino.game.CasinoGamesCount;
import com.ambrosia.add.database.casino.profits.CasinoTotalProfits;
import com.ambrosia.add.database.casino.query.QGameResultAggregate;
import com.ambrosia.add.database.operation.TransactionEntity;
import com.ambrosia.add.database.operation.query.QTransactionEntity;
import java.util.List;

public class CasinoQuery {

    private CasinoTotalProfits total;
    private CasinoGamesCount totalGames;

    private CasinoTotalProfits totalProfitsQuery() {
        QTransactionEntity alias = QTransactionEntity.alias();

        List<TransactionEntity> entitySumAmount = new QTransactionEntity().select(alias.sumAmount, alias.operationType).findList();
        CasinoTotalProfits profits = new CasinoTotalProfits();
        for (TransactionEntity entity : entitySumAmount) profits.put(entity);
        return profits;
    }

    public CasinoTotalProfits totalProfits() {
        if (this.total == null) this.total = totalProfitsQuery();
        return this.total;
    }

    private CasinoGamesCount totalGamesQuery() {
        QGameResultAggregate alias = QGameResultAggregate.alias();
        List<GameResultAggregate> query = new QGameResultAggregate().select(alias.count, alias.conclusion, alias.name,
            alias.deltaWinnings).findList();
        CasinoGamesCount games = new CasinoGamesCount();
        for (GameResultAggregate queryEntry : query) {
            games.put(queryEntry);
        }
        return games;
    }

    public CasinoGamesCount totalGames() {
        if (this.totalGames == null) this.totalGames = totalGamesQuery();
        return this.totalGames;
    }
}
