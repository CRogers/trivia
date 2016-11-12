package com.adaptionsoft.games.uglytrivia;

public class StdoutPrinter implements Printer {
    @Override
    public void printLine(String line) {
        System.out.println(line);
    }
}
