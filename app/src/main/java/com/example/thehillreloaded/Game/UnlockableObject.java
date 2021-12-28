package com.example.thehillreloaded.Game;

import android.graphics.Bitmap;

public class UnlockableObject {
    private static final Bitmap bitmapBoard;
    private static final int upCost;
    private static final int spReward;

    public UnlockableObject(Bitmap bitmap, int unitPoints, int sunnyPoints) {
        bitmapBoard = bitmap;
        upCost = unitPoints;
        spReward = sunnyPoints;
    }

    public static Bitmap getBitmapBoard() {
        return bitmapBoard;
    }

    public static int getUpCost() {
        return upCost;
    }

    public static int getSpReward() {
        return spReward;
    }
}
