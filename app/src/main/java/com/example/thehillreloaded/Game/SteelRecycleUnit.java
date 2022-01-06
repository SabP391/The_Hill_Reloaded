package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.widget.Toast;

public class SteelRecycleUnit extends RecycleUnit{

    public SteelRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.acceptedItemType = ItemType.STEEL;
        if(gameMode == GameMode.RELOADED) {
            this.sprite = GameAssets.getInstance(context).getSteelUnit(size);
            position = new Point(((int) (map.getTileSize()) * 2 + offsetFromLeft), (int) (map.getTileSize()) * 2);
        } else {
            this.sprite = GameAssets.getInstance(context).getSteelUnit(size);
            position = new Point((0 + offsetFromLeft), (int) (map.getTileSize()) * 4);
        }
        initMyTiles();
        setProcessSlotsPosition();
    }

    @Override
    public void setProcessSlotsPosition() {
        if(gameMode == GameMode.CLASSIC){
            slotsXPosition = position.x + 2 * (int) map.getTileSize() + 5;
            firstSlotLineYPosition = position.y + (int)((grayLine.getStrokeWidth() / 2) + (map.getTileSize() / 2));
        }else{
            slotsXPosition = position.x - (int) map.getTileSize();
            firstSlotLineYPosition = position.y + (int)((grayLine.getStrokeWidth() / 2) + (map.getTileSize()));

        }
        secondSlotLineYPosition = firstSlotLineYPosition + (int)grayLine.getStrokeWidth() + 5;
        thirdSlotLineYPosition = secondSlotLineYPosition + (int)grayLine.getStrokeWidth() + 5;
    }

    @Override
    public void downgradeMessage() {
        Toast.makeText(context, "La centrale dell'acciaio ha perso un upgrade.", Toast.LENGTH_SHORT).show();
    }

}
