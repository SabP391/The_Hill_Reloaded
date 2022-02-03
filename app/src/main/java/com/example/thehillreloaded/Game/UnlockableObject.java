package com.example.thehillreloaded.Game;

public class UnlockableObject {
    private final int upCost;
    private final int spReward;

    public UnlockableObject(int unitPoints, int sunnyPoints) {
        upCost = unitPoints;
        spReward = sunnyPoints;
    }

    public int getUpCost() {
        return upCost;
    }

    public int getSpReward() {
        return spReward;
    }
}
