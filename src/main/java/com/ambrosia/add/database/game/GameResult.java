package com.ambrosia.add.database.game;

public abstract class GameResult {


    private final String name;
    private String conclusion;
    private int deltaWinnings = 0;
    private Long id;
    private final int originalBet;
    private int currentBet;

    public GameResult(GameResultEntity data) {
        this.id = data.id;
        this.name = data.name;
        this.conclusion = data.conclusion;
        this.currentBet = this.originalBet = data.originalBet;
        this.deltaWinnings = data.deltaWinnings;
    }

    public GameResult(String name, int originalBet) {
        this.name = name;
        this.currentBet = this.originalBet = originalBet;
    }

    public GameResultEntity toEntity() {
        GameResultEntity entity = new GameResultEntity(name);
        entity.conclusion = this.conclusion;
        entity.deltaWinnings = this.deltaWinnings;
        entity.id = this.id;
        entity.originalBet = this.originalBet;
        return entity;
    }

    public void result(HandResult handResult) {
        this.result(handResult.resultName(), handResult.betMultiplier());
    }

    public void result(String handResult, double betMultiplier) {
        this.addWinnings(betMultiplier);
        if (this.conclusion == null) {
            this.conclusion = handResult;
        } else {
            this.conclusion = this.overflowHandResult(this.conclusion, handResult);
        }
    }

    protected abstract String overflowHandResult(String original, String next);

    private void addWinnings(double betMultiplier) {
        this.deltaWinnings += Math.ceil(currentBet * betMultiplier);
    }

    public void multiplyBet(int multiplier) {
        this.currentBet = this.currentBet * multiplier;
    }

    public long getCurrentBet() {
        return this.currentBet;
    }

    public int getWinnings() {
        return this.deltaWinnings;
    }

    public int getOriginalBet() {
        return this.originalBet;
    }
}
