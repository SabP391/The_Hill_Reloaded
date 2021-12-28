package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Point;

public class AlluminiumRecycleUnit extends RecycleUnit{

    public AlluminiumRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.sprite = GameAssets.getInstance(context).getAlUnit(size);
        position = new Point(0, (int)(map.getTileSize()));

    }
}
