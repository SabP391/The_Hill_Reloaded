package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IncineratorUnit extends RecycleUnit{

    private static final int SINGLE_ITEM_COST = 2;
    private static final int LINE_COST = 3;

    public IncineratorUnit(TileMap map, Context context){
        super(map, context);
        this.sprite = GameAssets.getInstance(context).getIncineratorUnit(size);
        position = new Point((int)((map.getMapSize().x * map.getTileSize()) -
                (2 * map.getTileSize() + offsetFromRight)), (int)(map.getTileSize() * 6));
        initMyTiles();
        setProcessSlotsPosition();
    }

    @Override
    public void setProcessSlotsPosition() {
        slotsXPosition = position.x - (int) map.getTileSize() - 5;
        firstSlotLineYPosition = position.y + (int)((grayLine.getStrokeWidth() / 2) + (map.getTileSize() / 2));
        secondSlotLineYPosition = firstSlotLineYPosition + (int)grayLine.getStrokeWidth() + 5;
        thirdSlotLineYPosition = secondSlotLineYPosition + (int)grayLine.getStrokeWidth() + 5;
    }

    // Distrugge gli oggetti sulla linea blu
    public int destroyFirstLine(ConcurrentLinkedQueue<GameItem> itemOnScreenList){
        int itemOnFloor = 0;
        if(GameManager.getInstance().getSunnyPoints() < LINE_COST) {
            return 0;
        }else{
            for (GameItem item : itemOnScreenList) {
                if (item.isTouchingTheBlueLine()) {
                    itemOnFloor++;
                    map.setTileValue(item.getCurrentTile(), 0);
                    itemOnScreenList.remove(item);
                }
            }
            if(itemOnFloor == 0){
                return 1;
            }else {
                GameManager.getInstance().subtractSunnyPoints(LINE_COST);
                Log.d("Current sunnypoints", String.valueOf(GameManager.getInstance().getSunnyPoints()));
                return 2;
            }
        }
    }
}
