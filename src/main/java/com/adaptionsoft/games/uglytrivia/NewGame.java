package com.adaptionsoft.games.uglytrivia;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

public class NewGame implements Game {
    private enum Category {
        POP("Pop", 0),
        SCIENCE("Science", 1),
        SPORTS("Sports", 2),
        ROCK("Rock", 3);

        public final String name;
        public final int index;

        Category(String name, int index) {
            this.name = name;
            this.index = index;
        }

        private static Category fromIndex(int categoryIndex) {
            return Arrays.stream(values())
                .filter(category -> category.index == categoryIndex)
                .findFirst()
                .get();
        }

        private String createQuestion(int index) {
            return name + " Question " + index;
        }
    }

    public static final int BOARD_SIZE = 12;
    private final Printer printer;

    private ArrayList<String> players = new ArrayList<>();
    private int[] positions = new int[6];
    private int[] goldCoins = new int[6];
    private boolean[] inPenaltyBox = new boolean[6];

    private Map<Category, LinkedList<String>> questions = Maps.newHashMap();

    private int currentPlayer = 0;
    private boolean isGettingOutOfPenaltyBox;

    public NewGame(Printer printer) {
        this.printer = printer;
        for (Category category : Category.values()) {
            LinkedList<String> categoryQuestions = new LinkedList<>();
            for (int i = 0; i < 50; i++) {
                categoryQuestions.add(category.createQuestion(i));
            }
            questions.put(category, categoryQuestions);
        }
    }

    public NewGame() {
        this(new StdoutPrinter());
    }

    @Override
    public boolean add(String playerName) {
        players.add(playerName);
        printer.printLine(playerName + " was added");
        printer.printLine("They are player number " + players.size());
        return true;
    }

    @Override
    public void roll(int roll) {
        printer.printLine(players.get(currentPlayer) + " is the current player");
        printer.printLine("They have rolled a " + roll);

        if (inPenaltyBox[currentPlayer]) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;
                printer.printLine(players.get(currentPlayer) + " is getting out of the penalty box");
                moveCurrentPlayer(roll);
                askQuestion();
            } else {
                isGettingOutOfPenaltyBox = false;
                printer.printLine(players.get(currentPlayer) + " is not getting out of the penalty box");
            }
        } else {
            moveCurrentPlayer(roll);
            askQuestion();
        }
    }

    private void moveCurrentPlayer(int roll) {
        positions[currentPlayer] = (positions[currentPlayer] + roll) % BOARD_SIZE;
        printer.printLine(players.get(currentPlayer)
            + "'s new location is "
            + positions[currentPlayer]);
    }

    private void askQuestion() {
        Category category = currentCategory();
        printer.printLine("The category is " + category.name);
        printer.printLine(questions.get(category).removeFirst());
    }

    private Category currentCategory() {
        int categoryIndex = positions[currentPlayer] % 4;
        return Category.fromIndex(categoryIndex);
    }

    @Override
    public boolean wasCorrectlyAnswered() {
        if (inPenaltyBox[currentPlayer] && !isGettingOutOfPenaltyBox) {
            advancePlayer();
            return true;
        }
        printer.printLine("Answer was correct!!!!");
        goldCoins[currentPlayer]++;
        printer.printLine(players.get(currentPlayer)
            + " now has "
            + goldCoins[currentPlayer]
            + " Gold Coins.");

        boolean notWinner = !didPlayerWin();
        advancePlayer();
        return notWinner;
    }

    private void advancePlayer() {
        currentPlayer++;
        currentPlayer %= players.size();
    }

    @Override
    public boolean wrongAnswer() {
        printer.printLine("Question was incorrectly answered");
        printer.printLine(players.get(currentPlayer) + " was sent to the penalty box");
        inPenaltyBox[currentPlayer] = true;

        advancePlayer();
        return true;
    }


    private boolean didPlayerWin() {
        return goldCoins[currentPlayer] == 6;
    }
}
