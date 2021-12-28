package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Point;

public class IncineratorUnit extends RecycleUnit{

    public IncineratorUnit(TileMap map, Context context){
        super(map, context);
        this.sprite = GameAssets.getInstance(context).getIncineratorUnit(size);
        position = new Point(0, (int)(map.getTileSize()));

    }
}
