
package com.adaptionsoft.games.trivia.runner;
import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.NewGame;

import java.util.Random;


public class GameRunner {

	private static boolean gameStillRunning;

	public static void main(String[] args) {
		Game aGame = new NewGame();
		
		aGame.add("Chet");
		aGame.add("Pat");
		aGame.add("Sue");
		
		Random rand = new Random();
	
		do {
			
			aGame.roll(rand.nextInt(5) + 1);
			
			if (rand.nextInt(9) == 7) {
				gameStillRunning = aGame.wrongAnswer();
			} else {
				gameStillRunning = aGame.wasCorrectlyAnswered();
			}
			
			
			
		} while (gameStillRunning);
		
	}
}
