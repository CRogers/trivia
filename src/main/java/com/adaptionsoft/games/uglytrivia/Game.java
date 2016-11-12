package com.adaptionsoft.games.uglytrivia;

public interface Game {
    boolean add(String playerName);

    void roll(int roll);

    boolean wasCorrectlyAnswered();

    boolean wrongAnswer();
}
