package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;

public class PlasticRecycleUnit extends RecycleUnit{

    public PlasticRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.acceptedItemType = ItemType.PLASTIC;
        if(gameMode == GameMode.RELOADED) {
            this.sprite = GameAssets.getInstance(context).getPlasticUnit(size);
            position = new Point((0 + offsetFromLeft), (int) (map.getTileSize()) * 4);
        } else {
            this.sprite = GameAssets.getInstance(context).getPlasticUnit(size);
            position = new Point((((int) (map.getTileSize()) * 2) + offsetFromLeft), (int) (map.getTileSize()) * 6);
        }
        initMyTiles();
        setProcessSlotsPosition();
    }

    @Override
    public void setProcessSlotsPosition() {
        if(gameMode == GameMode.CLASSIC){
            slotsXPosition = position.x - (int) map.getTileSize();
        }else{
            slotsXPosition = position.x + 2 * (int) map.getTileSize() + 5;
        }
        firstSlotLineYPosition = position.y + (int)((grayLine.getStrokeWidth() / 2) + (map.getTileSize() / 2));
        secondSlotLineYPosition = firstSlotLineYPosition + (int)grayLine.getStrokeWidth() + 5;
        thirdSlotLineYPosition = secondSlotLineYPosition + (int)grayLine.getStrokeWidth() + 5;
    }

}
