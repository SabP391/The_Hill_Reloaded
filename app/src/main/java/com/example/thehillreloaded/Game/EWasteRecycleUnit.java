package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Point;

public class EWasteRecycleUnit extends RecycleUnit{

    public EWasteRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.sprite = GameAssets.getInstance(context).getEwasteUnit(size);
        position = new Point(0, (int)(map.getTileSize()));

    }
}
