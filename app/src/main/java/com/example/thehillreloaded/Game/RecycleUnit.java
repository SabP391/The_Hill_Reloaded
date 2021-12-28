package com.example.thehillreloaded.Game;

import android.graphics.Bitmap;
import android.graphics.Point;

public abstract class RecycleUnit {
    protected Bitmap sprite;
    protected Point position;
    protected int myTiles[];
    protected int cost;
    protected ItemType acceptedItemType;

    public RecycleUnit(){

    }


}
