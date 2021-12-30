package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;

public class CompostRecycleUnit extends RecycleUnit{

    public CompostRecycleUnit(TileMap map, Context context){
        super(map, context);
        if(gameMode == GameMode.RELOADED) {
            this.sprite = GameAssets.getInstance(context).getCompostUnit(size);
            position = new Point(((int) (map.getTileSize()) * 2 + offsetFromLeft), (int) (map.getTileSize()) * 6);
        }

    }

    @Override
    public void drawUnitPoints(Canvas c) {
        c.drawBitmap(unitPointsIcon, (int)(position.x - unitPointsIcon.getWidth() * 2), position.y + (int) map.getTileSize(), null);
    }
}
