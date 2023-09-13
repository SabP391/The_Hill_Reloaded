package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IncineratorUnit extends RecycleUnit{

    private static final int SINGLE_ITEM_COST = 2;
    private static final int LINE_COST = 3;

    private Paint pollutionPaint;
    private RectF pollutionToDraw;

    public IncineratorUnit(TileMap map, Context context){
        super(map, context);
        this.sprite = GameAssets.getInstance(context).getIncineratorUnit(size);
        this.acceptedItemType = ItemType.ALL;
        position = new Point((int)((map.getMapSize().x * map.getTileSize()) -
                (2 * map.getTileSize() + offsetFromRight)), (int)(map.getTileSize() * 6));
        initMyTiles();
        setProcessSlotsPosition();

        pollutionToDraw = new RectF(0, 0, map.getTileSize() * (map.getMapSize().x + 1), map.getTileSize() * map.getMapSize().y);
        pollutionPaint = new Paint();
        pollutionPaint.setStyle(Paint.Style.FILL);
        pollutionPaint.setColor(Color.BLACK);
        pollutionPaint.setAlpha(100);
    }

    @Override
    public void drawFirstSlotProgress(Canvas c, long currentTime) {
        if (!isFirstSlotFree) {
            long elapsedTime = (currentTime - timeAtFirstSlotProcessStart) / 1000000000;
            // il moltiplicatore della linea rossa viene normalizzato
            // tramite il metodo redLineMultiplier
            float redLineM = redLineMultiplier(elapsedTime);
            // Se lo slot di lavoro è occupato disegna la linea rossa
            if (elapsedTime < PROCESSING_TIME) {
                c.drawRect(pollutionToDraw, pollutionPaint);
                c.drawLine(slotsXPosition,
                        firstSlotLineYPosition,
                        slotsXPosition + (map.getTileSize() * redLineM),
                        firstSlotLineYPosition,
                        redLine);
            }
            // Altrimenti reimposta a true la variabile di controllo
            // per liberare lo slot di lavoro
            else {
                isFirstSlotFree = true;
            }
        }
    }

    @Override
    public void downgradeMessage() { }

    @Override
    public void setProcessSlotsPosition() {
        slotsXPosition = position.x - (int) map.getTileSize() - 5;
        firstSlotLineYPosition = position.y + (int)((grayLine.getStrokeWidth() / 2) + (map.getTileSize() / 2));
        secondSlotLineYPosition = firstSlotLineYPosition + (int)grayLine.getStrokeWidth() + 5;
        thirdSlotLineYPosition = secondSlotLineYPosition + (int)grayLine.getStrokeWidth() + 5;
    }

    @Override
    public boolean processItemClassic(GameItem item){
        if(isFirstSlotFree && GameManager.getInstance().getSunnyPoints() >= SINGLE_ITEM_COST){
            isFirstSlotFree = false;
            timeAtFirstSlotProcessStart = System.nanoTime();
            GameManager.getInstance().subtractSunnyPoints(SINGLE_ITEM_COST);
            return true;
        }
        return false;
    }

    @Override
    public boolean processItemReloaded(GameItem item){
        return processItemClassic(item);
    }

    // Distrugge gli oggetti sulla linea blu
    public int destroyFirstLine(ConcurrentLinkedQueue<GameItem> itemOnScreenList){
        int itemOnFloor = 0;
        if(GameManager.getInstance().getSunnyPoints() < LINE_COST && !isFirstSlotFree) {
            return 0;
        }else if(isFirstSlotFree){
            isFirstSlotFree = false;
            timeAtFirstSlotProcessStart = System.nanoTime();
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
        }else return 3;
    }

    @Override
    public void drawUnitPoints(Canvas c){

    }
}
