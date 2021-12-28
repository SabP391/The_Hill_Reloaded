package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

public abstract class RecycleUnit {
    protected Context context;
    protected TileMap map;
    protected Bitmap sprite;
    protected Point size;
    protected Point position;
    protected int myTiles[];
    protected int cost;
    protected ItemType acceptedItemType;

    public RecycleUnit(TileMap map, Context context){
        this.context = context;
        this.map = map;
        size = new Point((int) (map.getTileSize() * 2), (int) (map.getTileSize() * 2));
    }

    protected void drawUnit(Canvas c){c.drawBitmap(sprite, position.x, position.y, null);}

}
