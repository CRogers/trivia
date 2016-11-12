package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.List;

public class NewGame implements Game {
    private final Printer printer;

    private final List<Player> playerList = new ArrayList<>();

    private final CategorisedQuestions categorisedQuestions;

    private int currentPlayer = 0;

    public NewGame(Printer printer) {
        this.printer = printer;
        this.categorisedQuestions = new CategorisedQuestions(printer);
    }

    public NewGame(){
        this(new StdoutPrinter());
    }

    @Override
    public boolean add(String playerName) {
        playerList.add(new Player(playerName));

        printer.printLine(playerName + " was added");
        printer.printLine("They are player number " + howManyPlayers());
        return true;
    }

    private int howManyPlayers() {
        return playerList.size();
    }

    @Override
    public void roll(int roll) {
        Player currentPlayerObj = getCurrentPlayer();
        String currentPlayerName = currentPlayerObj.name;
        printer.printLine(currentPlayerName + " is the current player");
        printer.printLine("They have rolled a " + roll);

        boolean isEven = roll % 2 == 0;
        boolean playerIsInPenaltyBox = currentPlayerObj.inPenaltyBox;

        if (playerIsInPenaltyBox) {
            currentPlayerObj.isGettingOutOfPenaltyBox = !isEven;
            if (currentPlayerObj.isGettingOutOfPenaltyBox) {
                printer.printLine(currentPlayerName + " is getting out of the penalty box");
            } else {
                printer.printLine(currentPlayerName + " is not getting out of the penalty box");
            }
        }

        if (!playerIsInPenaltyBox || !isEven) {
            currentPlayerObj.place += roll;
            if (currentPlayerObj.place > 11) currentPlayerObj.place = currentPlayerObj.place - 12;

            printer.printLine(currentPlayerName
                    + "'s new location is "
                    + currentPlayerObj.place);
            categorisedQuestions.askQuestion(currentPlayerObj.place);
        }

    }

    private Player getCurrentPlayer() {
        return playerList.get(currentPlayer);
    }

    @Override
    public boolean wasCorrectlyAnswered() {
        Player currentPlayerObj = getCurrentPlayer();
        if (currentPlayerObj.inPenaltyBox){
            if (currentPlayerObj.isGettingOutOfPenaltyBox) {
                printer.printLine("Answer was correct!!!!");
                currentPlayerObj.goldCoinsInPurse++;
                printer.printLine(currentPlayerObj.name
                        + " now has "
                        + currentPlayerObj.goldCoinsInPurse
                        + " Gold Coins.");

                boolean winner = didPlayerWin();
                currentPlayer++;
                if (currentPlayer == howManyPlayers()) currentPlayer = 0;

                return winner;
            } else {
                currentPlayer++;
                if (currentPlayer == howManyPlayers()) currentPlayer = 0;
                return true;
            }
        } else {
            printer.printLine("Answer was corrent!!!!");
            currentPlayerObj.goldCoinsInPurse++;
            printer.printLine(currentPlayerObj.name
                    + " now has "
                    + currentPlayerObj.goldCoinsInPurse
                    + " Gold Coins.");

            boolean winner = didPlayerWin();
            currentPlayer++;
            if (currentPlayer == howManyPlayers()) currentPlayer = 0;

            return winner;
        }
    }

    @Override
    public boolean wrongAnswer(){
        Player currentPlayerObj = getCurrentPlayer();
        printer.printLine("Question was incorrectly answered");
        printer.printLine(currentPlayerObj.name + " was sent to the penalty box");
        currentPlayerObj.inPenaltyBox = true;

        currentPlayer++;
        if (currentPlayer == howManyPlayers()) currentPlayer = 0;
        return true;
    }


    private boolean didPlayerWin() {
        Player currentPlayerObj = getCurrentPlayer();
        return !(currentPlayerObj.goldCoinsInPurse == 6);
    }
}
