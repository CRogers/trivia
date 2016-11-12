package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.NewGame;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.Ctor;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(JUnitQuickcheck.class)
public class GoldenMasterTests {
    public static class Play {
        public final int roll;
        private final int answerIsCorrect;

        public Play(
            @InRange(minInt = 1, maxInt = 6) int roll,
            @InRange(minInt = 0, maxInt = 9) int answerIsCorrect) {
            this.roll = roll;
            this.answerIsCorrect = answerIsCorrect;
        }

        public boolean answerIsCorrect() {
            return answerIsCorrect == 7;
        }

        @Override
        public String toString() {
            return String.format("%s:%s", roll, answerIsCorrect());
        }
    }


    @Property
    public void golden_master_and_new_are_the_same(
        @InRange(minInt = 2, maxInt = 6) int numberOfPlayers,
        List<@From(Ctor.class) Play> plays) {

        System.out.println(String.format("%d\n%s", numberOfPlayers, plays));

        Game game = new NewGame();
        for (int i = 1; i < numberOfPlayers; i++) {
            game.add("Player " + i);
        }

        for(Play play : plays) {
            game.roll(play.roll);
            boolean notAWinner = false;
            if (play.answerIsCorrect()) {
                notAWinner = game.wrongAnswer();
            } else {
                notAWinner = game.wasCorrectlyAnswered();
            }
            boolean gameWon = !notAWinner;
            if (gameWon) {
                break;
            }
        }

        System.out.println("\n\n----------------------------------------\n\n");
    }
}
