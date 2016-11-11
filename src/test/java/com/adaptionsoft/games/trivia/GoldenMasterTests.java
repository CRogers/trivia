package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.uglytrivia.Game;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(JUnitQuickcheck.class)
public class GoldenMasterTests {

    @Property
    public void golden_master_and_new_are_the_same(
        @InRange(minInt = 2, maxInt = 6) int numberOfPlayers,
        List<Boolean> answersAreCorrect,
        List<@InRange(minInt = 0, maxInt = 6) Integer> rolls) {

        System.out.println(String.format("%d\n%s\n%s", numberOfPlayers, answersAreCorrect, rolls));

        Game game = new Game();
        for (int i = 1; i < numberOfPlayers; i++) {
            game.add("Player " + i);
        }

        for(int i = 0; i < Math.min(answersAreCorrect.size(), rolls.size()); i++) {
            game.roll(rolls.get(i));
            boolean notAWinner = false;
            if (answersAreCorrect.get(i)) {
                notAWinner = game.wasCorrectlyAnswered();
            } else {
                notAWinner = game.wrongAnswer();
            }
            boolean gameWon = !notAWinner;
            if (gameWon) {
                break;
            }
        }

        System.out.println("\n\n----------------------------------------\n\n");
    }
}
