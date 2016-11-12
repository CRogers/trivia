package com.adaptionsoft.games.uglytrivia;

public class Player {
    public final Printer printer;
    private final CategorisedQuestions categorisedQuestions;
    public final String name;
    private int place = 0;
    public int goldCoinsInPurse = 0;
    public boolean inPenaltyBox = false;
    public boolean isGettingOutOfPenaltyBox = false;

    public Player(Printer printer, CategorisedQuestions categorisedQuestions, String name) {
        this.printer = printer;
        this.categorisedQuestions = categorisedQuestions;
        this.name = name;
    }

    void rollPlayer(int roll) {
        String currentPlayerName = name;
        printer.printLine(currentPlayerName + " is the current player");
        printer.printLine("They have rolled a " + roll);

        boolean isEven = roll % 2 == 0;
        boolean playerIsInPenaltyBox = inPenaltyBox;

        if (playerIsInPenaltyBox) {
            isGettingOutOfPenaltyBox = !isEven;
            if (isGettingOutOfPenaltyBox) {
                printer.printLine(currentPlayerName + " is getting out of the penalty box");
            } else {
                printer.printLine(currentPlayerName + " is not getting out of the penalty box");
            }
        }

        if (!playerIsInPenaltyBox || !isEven) {
            place += roll;
            if (place > 11) place = place - 12;

            printer.printLine(currentPlayerName
                    + "'s new location is "
                    + place);
            categorisedQuestions.askQuestion(place);
        }
    }

    public boolean wasCorrectlyAnsweredPlayer() {
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

    public boolean notWonYet() {
        return !(goldCoinsInPurse == 6);
    }
}
