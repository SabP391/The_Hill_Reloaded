package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Point;

public class SteelRecycleUnit extends RecycleUnit{

    public SteelRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.sprite = GameAssets.getInstance(context).getSteelUnit(size);
        position = new Point((int)(map.getTileSize())*2, (int)(map.getTileSize())*2);

    }
}
