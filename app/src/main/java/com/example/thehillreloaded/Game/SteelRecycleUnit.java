package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
        int xPositionUnitPointIcon;
        int yPositionUnitPointIcon = position.y + (int) map.getTileSize();;
        if(gameMode == GameMode.RELOADED){
            xPositionUnitPointIcon = position.x - unitPointsIcon.getWidth() * 2;
        } else{
            xPositionUnitPointIcon = (int)(position.x + 2 * map.getTileSize()) + 2;
        }
        c.drawBitmap(unitPointsIcon, (int)(xPositionUnitPointIcon), position.y + (int) map.getTileSize(), null);
        c.drawText(String.valueOf(unitPoints), xPositionUnitPointIcon + unitPointsIcon.getWidth(),yPositionUnitPointIcon + (unitPointsIcon.getHeight() / 2), unitPointPaint);
    }

}
