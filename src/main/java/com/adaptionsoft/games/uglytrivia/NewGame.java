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
        playerList.add(new Player(printer, categorisedQuestions, playerName));

        printer.printLine(playerName + " was added");
        printer.printLine("They are player number " + howManyPlayers());
        return true;
    }

    private int howManyPlayers() {
        return playerList.size();
    }

    @Override
    public void roll(int roll) {
        getCurrentPlayer().rollPlayer(roll);
    }

    private Player getCurrentPlayer() {
        return playerList.get(currentPlayer);
    }

    @Override
    public boolean wasCorrectlyAnswered() {
        Player currentPlayerObj = getCurrentPlayer();
        boolean won = currentPlayerObj.wasCorrectlyAnsweredPlayer();

        currentPlayer++;
        if (currentPlayer == howManyPlayers()) currentPlayer = 0;
        return won;
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


}
