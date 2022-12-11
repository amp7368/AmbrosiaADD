package com.ambrosia.add.database.casino.profits;

import com.ambrosia.add.database.operation.TransactionType;

public record CasinoTotalProfitsByOperation(TransactionType operationType, long sumAmount) {

}
