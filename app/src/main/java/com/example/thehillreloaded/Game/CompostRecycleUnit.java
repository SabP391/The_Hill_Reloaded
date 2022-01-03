package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;

public class CompostRecycleUnit extends RecycleUnit{

    public CompostRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.acceptedItemType = ItemType.COMPOST;
        if(gameMode == GameMode.RELOADED) {
            this.sprite = GameAssets.getInstance(context).getCompostUnit(size);
            position = new Point(((int) (map.getTileSize()) * 2 + offsetFromLeft), (int) (map.getTileSize()) * 6);
        }
        initMyTiles();

    }

}
