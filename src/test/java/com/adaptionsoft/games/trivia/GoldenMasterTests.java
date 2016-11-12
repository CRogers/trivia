package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.NewGame;
import com.adaptionsoft.games.uglytrivia.Printer;
import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.Ctor;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

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


    @Property(trials = 1000)
    public void golden_master_and_new_are_the_same(
        @InRange(minInt = 2, maxInt = 6) int numberOfPlayers,
        List<@From(Ctor.class) Play> plays) {

        String goldenMasterOutput = playGame(numberOfPlayers, plays, GoldenMasterGame::new);
        String newGameOutput = playGame(numberOfPlayers, plays, NewGame::new);

        assertThat(newGameOutput).isEqualTo(goldenMasterOutput);
    }

    private String playGame(int numberOfPlayers, List<Play> plays, Function<Printer, Game> ctor) {
        CapturingPrinter capturingPrinter = new CapturingPrinter();
        Game game = ctor.apply(capturingPrinter);
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
        return capturingPrinter.capturedOutput();
    }
}
