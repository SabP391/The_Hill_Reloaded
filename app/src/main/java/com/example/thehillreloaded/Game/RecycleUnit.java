package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

public abstract class RecycleUnit {
    protected GameMode gameMode;
    protected Context context;
    protected TileMap map;
    protected Bitmap sprite;
    protected Point size;
    protected Point position;
    protected int offsetFromLeft = 0;
    protected int offsetFromRight = 0;
    protected int myTiles[];
    protected int cost;
    protected ItemType acceptedItemType;

    public RecycleUnit(TileMap map, Context context){
        this.gameMode = GameManager.getInstance().getGameMode();
        this.context = context;
        this.map = map;
        size = new Point((int) (map.getTileSize() * 2), (int) (map.getTileSize() * 2));
    }

    protected void drawUnit(Canvas c){c.drawBitmap(sprite, position.x, position.y, null);}

}
