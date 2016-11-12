package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class NewGame implements Game {
    private static final String POP = "Pop";
    private static final String SCIENCE = "Science";
    private static final String SPORTS = "Sports";
    private static final String ROCK = "Rock";
    public static final int MAX_PLAYERS = 6;

    private final Printer printer;

    private final ArrayList<String> players = new ArrayList<>();
    private final int[] places = new int[MAX_PLAYERS];
    private final int[] purses  = new int[MAX_PLAYERS];
    private final boolean[] inPenaltyBox  = new boolean[MAX_PLAYERS];

    private final LinkedList<String> popQuestions = new LinkedList<>();
    private final LinkedList<String> scienceQuestions = new LinkedList<>();
    private final LinkedList<String> sportsQuestions = new LinkedList<>();
    private final LinkedList<String> rockQuestions = new LinkedList<>();

    private int currentPlayer = 0;
    private boolean isGettingOutOfPenaltyBox;

    public NewGame(Printer printer) {
        this.printer = printer;
        for (int i = 0; i < 50; i++) {
            popQuestions.addLast("Pop Question " + i);
            scienceQuestions.addLast(("Science Question " + i));
            sportsQuestions.addLast(("Sports Question " + i));
            rockQuestions.addLast(createRockQuestion(i));
        }
    }

    public NewGame(){
        this(new StdoutPrinter());
    }

    private String createRockQuestion(int index){
        return "Rock Question " + index;
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
        printer.printLine(players.get(currentPlayer) + " is the current player");
        printer.printLine("They have rolled a " + roll);

        if (inPenaltyBox[currentPlayer]) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;

                printer.printLine(players.get(currentPlayer) + " is getting out of the penalty box");
                places[currentPlayer] = places[currentPlayer] + roll;
                if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

                printer.printLine(players.get(currentPlayer)
                        + "'s new location is "
                        + places[currentPlayer]);
                printer.printLine("The category is " + currentCategory());
                askQuestion();
            } else {
                printer.printLine(players.get(currentPlayer) + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
                }

        } else {

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
        if (Objects.equals(currentCategory(), POP))
            printer.printLine(popQuestions.removeFirst());
        if (Objects.equals(currentCategory(), SCIENCE))
            printer.printLine(scienceQuestions.removeFirst());
        if (Objects.equals(currentCategory(), SPORTS))
            printer.printLine(sportsQuestions.removeFirst());
        if (Objects.equals(currentCategory(), ROCK))
            printer.printLine(rockQuestions.removeFirst());
    }


    private String currentCategory() {
        if (places[currentPlayer] == 0) return POP;
        if (places[currentPlayer] == 4) return POP;
        if (places[currentPlayer] == 8) return POP;
        if (places[currentPlayer] == 1) return SCIENCE;
        if (places[currentPlayer] == 5) return SCIENCE;
        if (places[currentPlayer] == 9) return SCIENCE;
        if (places[currentPlayer] == 2) return SPORTS;
        if (places[currentPlayer] == 6) return SPORTS;
        if (places[currentPlayer] == 10) return SPORTS;
        return "Rock";
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
