package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.List;

public class NewGame implements Game {
    private static final int MAX_PLAYERS = 6;

    private final Printer printer;

    private final List<String> players = new ArrayList<>();
    private final int[] places = new int[MAX_PLAYERS];
    private final int[] purses  = new int[MAX_PLAYERS];
    private final boolean[] inPenaltyBox  = new boolean[MAX_PLAYERS];

    private final CategorisedQuestions categorisedQuestions;

    private int currentPlayer = 0;
    private boolean isGettingOutOfPenaltyBox;

    public NewGame(Printer printer) {
        this.printer = printer;
        this.categorisedQuestions = new CategorisedQuestions(printer);
    }

    public NewGame(){
        this(new StdoutPrinter());
    }

    @Override
    public boolean add(String playerName) {
        players.add(playerName);
        places[howManyPlayers()] = 0;
        purses[howManyPlayers()] = 0;
        inPenaltyBox[howManyPlayers()] = false;

        printer.printLine(playerName + " was added");
        printer.printLine("They are player number " + players.size());
        return true;
    }

    private int howManyPlayers() {
        return players.size();
    }

    @Override
    public void roll(int roll) {
        String currentPlayerName = players.get(currentPlayer);
        printer.printLine(currentPlayerName + " is the current player");
        printer.printLine("They have rolled a " + roll);

        boolean isEven = roll % 2 == 0;
        boolean playerIsInPenaltyBox = inPenaltyBox[currentPlayer];

        if (playerIsInPenaltyBox) {
            isGettingOutOfPenaltyBox = !isEven;
            if (isGettingOutOfPenaltyBox) {
                printer.printLine(currentPlayerName + " is getting out of the penalty box");
            } else {
                printer.printLine(currentPlayerName + " is not getting out of the penalty box");
            }
        }

        if (!playerIsInPenaltyBox || !isEven) {
            places[currentPlayer] = places[currentPlayer] + roll;
            if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

            printer.printLine(players.get(currentPlayer)
                    + "'s new location is "
                    + places[currentPlayer]);
            categorisedQuestions.askQuestion(places[currentPlayer]);
        }

    }

    @Override
    public boolean wasCorrectlyAnswered() {
        if (inPenaltyBox[currentPlayer]){
            if (isGettingOutOfPenaltyBox) {
                printer.printLine("Answer was correct!!!!");
                purses[currentPlayer]++;
                printer.printLine(players.get(currentPlayer)
                        + " now has "
                        + purses[currentPlayer]
                        + " Gold Coins.");

                boolean winner = didPlayerWin();
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;

                return winner;
            } else {
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;
                return true;
            }
        } else {
            printer.printLine("Answer was corrent!!!!");
            purses[currentPlayer]++;
            printer.printLine(players.get(currentPlayer)
                    + " now has "
                    + purses[currentPlayer]
                    + " Gold Coins.");

            boolean winner = didPlayerWin();
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;

            return winner;
        }
    }

    @Override
    public boolean wrongAnswer(){
        printer.printLine("Question was incorrectly answered");
        printer.printLine(players.get(currentPlayer)+ " was sent to the penalty box");
        inPenaltyBox[currentPlayer] = true;

        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
        return true;
    }


    private boolean didPlayerWin() {
        return !(purses[currentPlayer] == 6);
    }
}
