package com.example.thehillreloaded.Game;

import android.graphics.Bitmap;

public class UnlockableObject {
    private final Bitmap bitmapBoard;
    private final int upCost;
    private final int spReward;

    public UnlockableObject(Bitmap bitmap, int unitPoints, int sunnyPoints) {
        bitmapBoard = bitmap;
        upCost = unitPoints;
        spReward = sunnyPoints;
    }

    public Bitmap getBitmapBoard() {
        return bitmapBoard;
    }

    public int getUpCost() {
        return upCost;
    }

    public int getSpReward() {
        return spReward;
    }
}
