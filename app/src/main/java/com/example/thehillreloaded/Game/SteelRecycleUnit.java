package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;

public class SteelRecycleUnit extends RecycleUnit{

    public SteelRecycleUnit(TileMap map, Context context){
        super(map, context);
        if(gameMode == GameMode.RELOADED) {
            this.sprite = GameAssets.getInstance(context).getSteelUnit(size);
            position = new Point(((int) (map.getTileSize()) * 2 + offsetFromLeft), (int) (map.getTileSize()) * 2);
        } else {
            this.sprite = GameAssets.getInstance(context).getSteelUnit(size);
            position = new Point((0 + offsetFromLeft), (int) (map.getTileSize()) * 4);
        }
    }

    @Override
    public void drawUnitPoints(Canvas c) {
        if(gameMode == GameMode.RELOADED){
            c.drawBitmap(unitPointsIcon, (int)(position.x - unitPointsIcon.getWidth() * 2), position.y + (int) map.getTileSize(), null);
        } else{
            c.drawBitmap(unitPointsIcon, (int)(position.x + 2 * map.getTileSize()) + 2, position.y + (int) map.getTileSize() - unitPointsIcon.getHeight(), null);
        }
    }

}
