package com.ambrosia.add.database.casino.profits;

import com.ambrosia.add.database.operation.TransactionEntity;
import com.ambrosia.add.database.operation.TransactionType;
import java.util.HashMap;
import java.util.Map;

public final class CasinoTotalProfits {

    public long profits = 0;
    public long circulated = 0;
    public long playerHoldings = 0;
    public final Map<TransactionType, CasinoTotalProfitsByOperation> totalByType = new HashMap<>();


    public void put(TransactionEntity entity) {
        this.totalByType.put(entity.operationType, new CasinoTotalProfitsByOperation(entity.operationType, entity.sumAmount));
        this.playerHoldings += entity.sumAmount;
        if (entity.operationType == TransactionType.WIN || entity.operationType == TransactionType.LOSS) {
            circulated += Math.abs(entity.sumAmount);
            profits -= entity.sumAmount;
        }
    }
}
