package com.adaptionsoft.games.trivia;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

public class GoldenMasterTests {
    private enum Roll {
        CORRECT,
        WRONG
    }

    public static class Play {
        public final int players;
        private final int rolls;

        public Play(int players, int rolls) {
            this.players = players;
            this.rolls = rolls;
        }

        public boolean correctAnswerAt(int i) {
            int place = 1 << i;
            return
        }
    }

    @Test
    public void test_everything() {
        List<Play> plays = Lists.newArrayList();
        for (int numPlayers = 0; numPlayers < 6; numPlayers++) {
            for (int maxRolls = 0; maxRolls < Math.pow(2, 16); maxRolls++) {

            }
        }

//        Game game = new Game();
//
//        game.add("Chet");
//        game.add("Pat");
//        game.add("Sue");
//
//        Random rand = new Random();
//
//        do {
//
//            game.roll(rand.nextInt(5) + 1);
//
//            if (rand.nextInt(9) == 7) {
//                notAWinner = game.wrongAnswer();
//            } else {
//                notAWinner = game.wasCorrectlyAnswered();
//            }
//
//
//
//        } while (notAWinner);
    }
}
