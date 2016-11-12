package com.adaptionsoft.games.uglytrivia;

public class Player {
    private final Printer printer;
    private final CategorisedQuestions categorisedQuestions;

    private final String name;

    private int place = 0;
    private int goldCoinsInPurse = 0;
    private boolean inPenaltyBox = false;
    private boolean isGettingOutOfPenaltyBox = false;

    public Player(Printer printer, CategorisedQuestions categorisedQuestions, String name) {
        this.printer = printer;
        this.categorisedQuestions = categorisedQuestions;
        this.name = name;
    }

    public void rollPlayer(int roll) {
        String currentPlayerName = name;
        printer.printLine(currentPlayerName + " is the current player");
        printer.printLine("They have rolled a " + roll);

        boolean isEven = roll % 2 == 0;

        if (inPenaltyBox) {
            isGettingOutOfPenaltyBox = !isEven;
            if (isGettingOutOfPenaltyBox) {
                printer.printLine(currentPlayerName + " is getting out of the penalty box");
            } else {
                printer.printLine(currentPlayerName + " is not getting out of the penalty box");
            }
        }

        if (!inPenaltyBox || isGettingOutOfPenaltyBox) {
            place += roll;
            if (place > 11) place = place - 12;

            printer.printLine(currentPlayerName
                    + "'s new location is "
                    + place);
            categorisedQuestions.askQuestion(place);
        }
    }

    public boolean wasCorrectlyAnswered() {
        if (inPenaltyBox){
            if (isGettingOutOfPenaltyBox) {
                printer.printLine("Answer was correct!!!!");
                goldCoinsInPurse++;
                printer.printLine(name
                    + " now has "
                    + goldCoinsInPurse
                    + " Gold Coins.");

                return notWonYet();
            } else {
                return true;
            }
        } else {
            printer.printLine("Answer was corrent!!!!");
            goldCoinsInPurse++;
            printer.printLine(name
                + " now has "
                + goldCoinsInPurse
                + " Gold Coins.");

            return notWonYet();
        }
    }

    private boolean notWonYet() {
        return !(goldCoinsInPurse == 6);
    }

    public void wrongAnswer() {
        printer.printLine("Question was incorrectly answered");
        printer.printLine(name + " was sent to the penalty box");
        inPenaltyBox = true;
    }
}
