package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class NewGame implements Game {
    private enum Category {
        POP("Pop"),
        SCIENCE("Science"),
        SPORTS("Sports"),
        ROCK("Rock");

        private final String name;

        Category(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static final int MAX_PLAYERS = 6;

    private final Printer printer;

    private final ArrayList<String> players = new ArrayList<>();
    private final int[] places = new int[MAX_PLAYERS];
    private final int[] purses  = new int[MAX_PLAYERS];
    private final boolean[] inPenaltyBox  = new boolean[MAX_PLAYERS];

    private final Queue<String> popQuestions = new LinkedList<>();
    private final Queue<String> scienceQuestions = new LinkedList<>();
    private final Queue<String> sportsQuestions = new LinkedList<>();
    private final Queue<String> rockQuestions = new LinkedList<>();

    private int currentPlayer = 0;
    private boolean isGettingOutOfPenaltyBox;

    public NewGame(Printer printer) {
        this.printer = printer;
        for (int i = 0; i < 50; i++) {
            popQuestions.offer("Pop Question " + i);
            scienceQuestions.offer(("Science Question " + i));
            sportsQuestions.offer(("Sports Question " + i));
            rockQuestions.offer("Rock Question " + i);
        }
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
            printer.printLine("The category is " + currentCategory());
            askQuestion();
        }

    }

    private void askQuestion() {
        if (Objects.equals(currentCategory(), Category.POP))
            printer.printLine(popQuestions.poll());
        if (Objects.equals(currentCategory(), Category.SCIENCE))
            printer.printLine(scienceQuestions.poll());
        if (Objects.equals(currentCategory(), Category.SPORTS))
            printer.printLine(sportsQuestions.poll());
        if (Objects.equals(currentCategory(), Category.ROCK))
            printer.printLine(rockQuestions.poll());
    }


    private Category currentCategory() {
        if (places[currentPlayer] == 0) return Category.POP;
        if (places[currentPlayer] == 4) return Category.POP;
        if (places[currentPlayer] == 8) return Category.POP;
        if (places[currentPlayer] == 1) return Category.SCIENCE;
        if (places[currentPlayer] == 5) return Category.SCIENCE;
        if (places[currentPlayer] == 9) return Category.SCIENCE;
        if (places[currentPlayer] == 2) return Category.SPORTS;
        if (places[currentPlayer] == 6) return Category.SPORTS;
        if (places[currentPlayer] == 10) return Category.SPORTS;
        return Category.ROCK;
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
        return !(purses[currentPlayer] == MAX_PLAYERS);
    }
}
