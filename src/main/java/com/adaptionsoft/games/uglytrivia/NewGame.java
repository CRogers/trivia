package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NewGame implements Game {
    private final Printer printer;

    ArrayList<String> players = new ArrayList<>();
    int[] places = new int[6];
    int[] purses  = new int[6];
    boolean[] inPenaltyBox  = new boolean[6];

    List<String> popQuestions = new LinkedList<>();
    List<String> scienceQuestions = new LinkedList<>();
    List<String> sportsQuestions = new LinkedList<>();
    List<String> rockQuestions = new LinkedList<>();

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public NewGame(Printer printer) {
        this.printer = printer;
        for (int i = 0; i < 50; i++) {
            popQuestions.add(createQuestion(i, "Pop"));
            scienceQuestions.add(createQuestion(i, "Science"));
            sportsQuestions.add(createQuestion(i, "Sports"));
            rockQuestions.add(createQuestion(i, "Rock"));
        }
    }

    public NewGame(){
        this(new StdoutPrinter());
    }

    private String createQuestion(int index, String questionType) {
        return questionType + " Question " + index;
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

    public int howManyPlayers() {
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
        if (currentCategory() == "Pop")
            printer.printLine(popQuestions.remove(0));
        if (currentCategory() == "Science")
            printer.printLine(scienceQuestions.remove(0));
        if (currentCategory() == "Sports")
            printer.printLine(sportsQuestions.remove(0));
        if (currentCategory() == "Rock")
            printer.printLine(rockQuestions.remove(0));
    }


    private String currentCategory() {
        if (places[currentPlayer] == 0) return "Pop";
        if (places[currentPlayer] == 4) return "Pop";
        if (places[currentPlayer] == 8) return "Pop";
        if (places[currentPlayer] == 1) return "Science";
        if (places[currentPlayer] == 5) return "Science";
        if (places[currentPlayer] == 9) return "Science";
        if (places[currentPlayer] == 2) return "Sports";
        if (places[currentPlayer] == 6) return "Sports";
        if (places[currentPlayer] == 10) return "Sports";
        return "Rock";
    }

    @Override
    public boolean wasCorrectlyAnswered() {
        if (inPenaltyBox[currentPlayer]){
            if (isGettingOutOfPenaltyBox) {
                return blah();
            } else {
                advanceToNextPlayer();
                return true;
            }
        } else {
            return blah();
        }
    }

    private void advanceToNextPlayer() {
        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
    }

    private boolean blah() {
        currentPlayerScored();

        boolean winner = didPlayerWin();
        advanceToNextPlayer();

        return winner;
    }

    private void currentPlayerScored() {
        printer.printLine("Answer was correct!!!!");
        purses[currentPlayer]++;
        printer.printLine(players.get(currentPlayer)
                + " now has "
                + purses[currentPlayer]
                + " Gold Coins.");
    }

    @Override
    public boolean wrongAnswer(){
        printer.printLine("Question was incorrectly answered");
        printer.printLine(players.get(currentPlayer)+ " was sent to the penalty box");
        inPenaltyBox[currentPlayer] = true;

        advanceToNextPlayer();
        return true;
    }


    private boolean didPlayerWin() {
        return !(purses[currentPlayer] == 6);
    }
}
