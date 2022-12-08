package com.github.AndrewAlbizati.result;

import com.ambrosia.add.database.game.GameResult;
import com.ambrosia.add.database.game.GameResultEntity;
import java.util.ArrayList;
import java.util.List;

public class BlackjackGameResult extends GameResult {

    private String tie;
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
        super("blackjack", originalBet);
    }
}
