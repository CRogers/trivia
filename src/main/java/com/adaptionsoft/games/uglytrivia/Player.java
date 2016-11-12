package com.adaptionsoft.games.uglytrivia;

public class Player {
    public final String name;
    public int place = 0;
    public int goldCoinsInPurse = 0;
    public boolean inPenaltyBox = false;

    public Player(String name) {
        this.name = name;
    }
}
