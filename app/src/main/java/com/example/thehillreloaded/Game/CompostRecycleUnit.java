package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.widget.Toast;

public class CompostRecycleUnit extends RecycleUnit{

    public CompostRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.acceptedItemType = ItemType.COMPOST;
        if(gameMode == GameMode.RELOADED) {
            this.sprite = GameAssets.getInstance(context).getCompostUnit(size);
            position = new Point(((int) (map.getTileSize()) * 2 + offsetFromLeft), (int) (map.getTileSize()) * 6);
        }
        initMyTiles();
        setProcessSlotsPosition();
    }

    @Override
    public void setProcessSlotsPosition() {
        slotsXPosition = position.x - (int) map.getTileSize();
        firstSlotLineYPosition = position.y + (int)((grayLine.getStrokeWidth() / 2) + (map.getTileSize() / 2));
        secondSlotLineYPosition = firstSlotLineYPosition + (int)grayLine.getStrokeWidth() + 5;
        thirdSlotLineYPosition = secondSlotLineYPosition + (int)grayLine.getStrokeWidth() + 5;
    }

    @Override
    public void downgradeMessage() {
        Toast.makeText(context, "La centrale dell'organico ha perso un upgrade.", Toast.LENGTH_SHORT).show();
    }

}
