package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Point;

public class PlasticRecycleUnit extends RecycleUnit{

    public PlasticRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.sprite = GameAssets.getInstance(context).getPlasticUnit(size);
        position = new Point((int)(map.getTileSize())*3, (int)(map.getTileSize())*5);

    }
}
