package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;

public class EWasteRecycleUnit extends RecycleUnit{

    public EWasteRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.sprite = GameAssets.getInstance(context).getEwasteUnit(size);
        position = new Point((int)((map.getMapSize().x * map.getTileSize()) -
                (2 * map.getTileSize() + offsetFromRight)), (int)(map.getTileSize() * 2));

    }

    @Override
    public void drawUnitPoints(Canvas c) {
        int xPositionUnitPointIcon = position.x - unitPointsIcon.getWidth() * 2;
        int yPositionUnitPointIcon = position.y + (int) map.getTileSize();;
        c.drawBitmap(unitPointsIcon, (int)(xPositionUnitPointIcon), position.y + (int) map.getTileSize(), null);
        c.drawText(String.valueOf(unitPoints), xPositionUnitPointIcon + unitPointsIcon.getWidth(),yPositionUnitPointIcon + (unitPointsIcon.getHeight() / 2), unitPointPaint);
    }
}
