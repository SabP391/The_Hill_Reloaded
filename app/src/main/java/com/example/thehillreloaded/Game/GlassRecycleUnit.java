package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Point;

public class GlassRecycleUnit extends RecycleUnit{

    public GlassRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.sprite = GameAssets.getInstance(context).getGlassUnit(size);
        position = new Point(0, (int)(map.getTileSize()));

    }
}
