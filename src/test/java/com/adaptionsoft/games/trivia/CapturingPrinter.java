package com.adaptionsoft.games.trivia;

import com.adaptionsoft.games.uglytrivia.Printer;

public class CapturingPrinter implements Printer {
    private final StringBuilder stringBuilder = new StringBuilder();

    @Override
    public void printLine(String line) {
        stringBuilder
            .append(line)
            .append("\n");
    }

    public String capturedOutput() {
        return stringBuilder.toString();
    }
}
