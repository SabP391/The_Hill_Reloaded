package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Point;

public class GlassRecycleUnit extends RecycleUnit{

    public GlassRecycleUnit(TileMap map, Context context){
        super(map, context);
        if(gameMode == GameMode.RELOADED) {
            this.sprite = GameAssets.getInstance(context).getGlassUnit(size);
            position = new Point((0 + offsetFromLeft), (int) (map.getTileSize()));
        } else {
            this.sprite = GameAssets.getInstance(context).getGlassUnit(size);
            position = new Point((((int) (map.getTileSize()) * 2) + offsetFromLeft), (int) (map.getTileSize()));
        }

    }
}
