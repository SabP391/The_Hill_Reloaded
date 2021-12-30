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
        int xPositionUnitPointIcon;
        int yPositionUnitPointIcon = yPositionUnitPointIcon = position.y + (int) map.getTileSize();;
        if(gameMode == GameMode.RELOADED){
            xPositionUnitPointIcon = (int)(position.x + 2 * map.getTileSize()) + 2;
        } else{
            xPositionUnitPointIcon = (int)(position.x - unitPointsIcon.getWidth() * 3);
        }
        c.drawBitmap(unitPointsIcon, (int)(xPositionUnitPointIcon), position.y + (int) map.getTileSize(), null);
        c.drawText(String.valueOf(unitPoints), xPositionUnitPointIcon + unitPointsIcon.getWidth(),yPositionUnitPointIcon + (unitPointsIcon.getHeight() / 2), unitPointPaint);
    }
}
