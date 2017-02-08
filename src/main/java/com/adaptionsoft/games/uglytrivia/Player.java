package com.adaptionsoft.games.uglytrivia;

class Player {
    private String name;
    public int place;
    public int purse;
    public boolean isInPenaltyBox;

    public Player(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public void name(String name) {
        this.name = name;
    }

    public int place() {
        return place;
    }

    public void place(int place) {
        this.place = place;
    }

    public int purse() {
        return purse;
    }

    public void purse(int purse) {
        this.purse = purse;
    }

    public boolean isinPenaltyBox() {
        return isInPenaltyBox;
    }

    public void inPenaltyBox(boolean inPenaltyBox) {
        isInPenaltyBox = inPenaltyBox;
    }
}
