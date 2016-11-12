package com.adaptionsoft.games.uglytrivia;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class CategorisedQuestions {
    private final Printer printer;

    private final Queue<String> popQuestions = new LinkedList<>();
    private final Queue<String> scienceQuestions = new LinkedList<>();
    private final Queue<String> sportsQuestions = new LinkedList<>();
    private final Queue<String> rockQuestions = new LinkedList<>();

    public CategorisedQuestions(Printer printer) {
        this.printer = printer;
        for (int i = 0; i < 50; i++) {
            popQuestions.offer("Pop Question " + i);
            scienceQuestions.offer(("Science Question " + i));
            sportsQuestions.offer(("Sports Question " + i));
            rockQuestions.offer("Rock Question " + i);
        }
    }

    public void askQuestion(int place) {
        Category currentCategory = currentCategory(place);
        printer.printLine("The category is " + currentCategory);
        if (Objects.equals(currentCategory, Category.POP))
            printer.printLine(popQuestions.poll());
        if (Objects.equals(currentCategory, Category.SCIENCE))
            printer.printLine(scienceQuestions.poll());
        if (Objects.equals(currentCategory, Category.SPORTS))
            printer.printLine(sportsQuestions.poll());
        if (Objects.equals(currentCategory, Category.ROCK))
            printer.printLine(rockQuestions.poll());
    }


    private Category currentCategory(int place) {
        switch (place) {
            case 0:
            case 4:
            case 8:
                return Category.POP;
            case 1:
            case 5:
            case 9:
                return Category.SCIENCE;
            case 2:
            case 6:
            case 10:
                return Category.SPORTS;
            default:
                return Category.ROCK;
        }
    }
}
