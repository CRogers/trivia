package com.adaptionsoft.games.uglytrivia;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.ArrayList;
import java.util.List;

public class NewGame implements Game {
    private final Printer printer;

    private final List<Player> players = new ArrayList<>();

    private ListMultimap<Category, String> questions = ArrayListMultimap.create();

    private int currentPlayer = 0;
    private boolean isAllowedToScoreFromPenaltyBoxThisTurn;

    public NewGame(Printer printer) {
        this.printer = printer;
        populateQuestions();
    }

    private void populateQuestions() {
        for (int i = 0; i < 50; i++) {
            for (Category category: Category.values()) {
                questions.put(category, category + " Question " + i);
            }
        }
    }

    public NewGame() {
        this(new StdoutPrinter());
    }

    @Override
    public boolean add(String playerName) {
        players.add(new Player(playerName));

        printer.printLine(playerName + " was added");
        printer.printLine("They are player number " + players.size());
        return true;
    }

    private int howManyPlayers() {
        return players.size();
    }

    @Override
    public void roll(int roll) {
        printer.printLine(getCurrentPlayer().name() + " is the current player");
        printer.printLine("They have rolled a " + roll);

        if (getCurrentPlayer().isinPenaltyBox()) {
            if (roll % 2 != 0) {
                isAllowedToScoreFromPenaltyBoxThisTurn = true;

                printer.printLine(getCurrentPlayer().name() + " is getting out of the penalty box");
                addRollToPlace(roll);
                askQuestion();
            } else {
                printer.printLine(getCurrentPlayer().name() + " is not getting out of the penalty box");
                isAllowedToScoreFromPenaltyBoxThisTurn = false;
            }
        } else {
            addRollToPlace(roll);
            askQuestion();
        }
    }

    private void addRollToPlace(int roll) {
        Player currentPlayer = getCurrentPlayer();
        currentPlayer.place((currentPlayer.place() + roll) % 12);

        printer.printLine(currentPlayer.name()
            + "'s new location is "
            + currentPlayer.place());
        printer.printLine("The category is " + currentCategory());
    }

    private void askQuestion() {
        printer.printLine(questions.get(currentCategory()).remove(0));
    }

    private Category currentCategory() {
        int place = getCurrentPlayer().place();
        return Category.getCategoryForPlace(place);
    }

    @Override
    public boolean wasCorrectlyAnswered() {
        boolean winner = true;
        if (!getCurrentPlayer().isinPenaltyBox() || isAllowedToScoreFromPenaltyBoxThisTurn) {
            printer.printLine("Answer was correct!!!!");
            incrementPurse();
            printer.printLine(getCurrentPlayer().name()
                + " now has "
                + getCurrentPlayer().purse()
                + " Gold Coins.");
            winner = didPlayerWin();
        }
        advancePlayerTurn();
        return winner;
    }

    private void incrementPurse() {
        getCurrentPlayer().purse(getCurrentPlayer().purse() + 1);
    }

    private void advancePlayerTurn() {
        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
    }

    @Override
    public boolean wrongAnswer() {
        printer.printLine("Question was incorrectly answered");
        printer.printLine(getCurrentPlayer().name() + " was sent to the penalty box");
        putInPenaltyBox();

        advancePlayerTurn();
        return true;
    }

    private void putInPenaltyBox() {
        getCurrentPlayer().inPenaltyBox(true);
    }

    private Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    private boolean didPlayerWin() {
        return !(getCurrentPlayer().purse() == 6);
    }

    private enum Category {
        POP("Pop"),
        SCIENCE("Science"),
        SPORTS("Sports"),
        ROCK("Rock"),
        ;

        private final String label;

        /* private */ Category(String label) {
            this.label = label;
        }

        private static Category getCategoryForPlace(int place) {
            int numCategories = values().length;
            int categoryIndex = place % numCategories;
            switch (categoryIndex) {
                case 0:
                    return POP;
                case 1:
                    return SCIENCE;
                case 2:
                    return SPORTS;
                case 3:
                    return ROCK;
                default:
                    throw new IllegalStateException("oops");
            }
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
