package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;

public class PaperRecycleUnit extends RecycleUnit{

    public PaperRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.acceptedItemType = ItemType.PAPER;
        this.sprite = GameAssets.getInstance(context).getPaperUnit(size);
        position = new Point((int)((map.getMapSize().x * map.getTileSize()) -
                (4 * map.getTileSize() + offsetFromRight)), (int)((map.getTileSize())));
        initMyTiles();
        setProcessSlotsPosition();

    }

    @Override
    public void setProcessSlotsPosition() {
        slotsXPosition = position.x + (int)(2 * map.getTileSize()) + 5;
        firstSlotLineYPosition = position.y + (int)((grayLine.getStrokeWidth() / 2) + (map.getTileSize() / 2));
        secondSlotLineYPosition = firstSlotLineYPosition + (int)grayLine.getStrokeWidth() + 5;
        thirdSlotLineYPosition = secondSlotLineYPosition + (int)grayLine.getStrokeWidth() + 5;
    }

}
