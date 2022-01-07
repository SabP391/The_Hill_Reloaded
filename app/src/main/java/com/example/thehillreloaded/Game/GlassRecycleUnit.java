package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.widget.Toast;

import com.example.thehillreloaded.R;

public class GlassRecycleUnit extends RecycleUnit{

    public GlassRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.acceptedItemType = ItemType.GLASS;
        if(gameMode == GameMode.RELOADED) {
            this.sprite = GameAssets.getInstance(context).getGlassUnit(size);
            position = new Point((0 + offsetFromLeft), (int) (map.getTileSize()));
        } else {
            this.sprite = GameAssets.getInstance(context).getGlassUnit(size);
            position = new Point((((int) (map.getTileSize()) * 2) + offsetFromLeft), (int) (map.getTileSize()));
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

    @Override
    public void downgradeMessage() {
        Toast.makeText(context, R.string.ve_perso_upgrade, Toast.LENGTH_SHORT).show();
    }

}
