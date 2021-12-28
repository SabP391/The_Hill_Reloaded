package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Point;

public class PaperRecycleUnit extends RecycleUnit{

    public PaperRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.sprite = GameAssets.getInstance(context).getPaperUnit(size);
        position = new Point(0, (int)(map.getTileSize()));

    }
}
