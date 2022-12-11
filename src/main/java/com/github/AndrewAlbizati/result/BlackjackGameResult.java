package com.github.AndrewAlbizati.result;

import com.ambrosia.add.database.game.GameResult;
import com.ambrosia.add.database.game.GameResultEntity;
import com.github.AndrewAlbizati.Blackjack;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class BlackjackGameResult extends GameResult {

    private List<String> hands = null;

    public BlackjackGameResult(GameResultEntity data) {
        super(data);
    }

    @Override
    protected String overflowHandResult(String original, String next) {
        if (hands != null) {
            hands.add(next);
            return "SPLIT";
        }
        hands = new ArrayList<>();
        hands.add(original);
        hands.add(next);
        return "SPLIT";

    }

    public BlackjackGameResult(int originalBet) {
        super(Blackjack.GAME_NAME, originalBet);
    }

    @Override
    public GameResultEntity toEntity() {
        String extraResults = new Gson().toJson(hands);
        GameResultEntity entity = super.toEntity();
        entity.extraResults = extraResults;
        return entity;
    }
}
