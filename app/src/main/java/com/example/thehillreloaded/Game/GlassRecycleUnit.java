package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;

public class GlassRecycleUnit extends RecycleUnit{

    public GlassRecycleUnit(TileMap map, Context context){
        super(map, context);
        if(gameMode == GameMode.RELOADED) {
            this.sprite = GameAssets.getInstance(context).getGlassUnit(size);
            position = new Point((0 + offsetFromLeft), (int) (map.getTileSize()));
        } else {
            this.sprite = GameAssets.getInstance(context).getGlassUnit(size);
            position = new Point((((int) (map.getTileSize()) * 2) + offsetFromLeft), (int) (map.getTileSize()));
        }

    }

    @Override
    public void drawUnitPoints(Canvas c) {
        if(gameMode == GameMode.RELOADED){
            c.drawBitmap(unitPointsIcon, (int)(position.x + 2 * map.getTileSize()) + 2, position.y + (int) map.getTileSize() - unitPointsIcon.getHeight(), null);
        } else{
            c.drawBitmap(unitPointsIcon, (int)(position.x - unitPointsIcon.getWidth() * 3), position.y + (int) map.getTileSize(), null);
        }
    }
}
