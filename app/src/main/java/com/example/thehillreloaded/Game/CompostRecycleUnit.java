package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Point;

public class CompostRecycleUnit extends RecycleUnit{

    private static final int COST_IN_SUNNY_POINTS = 20;

    public CompostRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.cost = COST_IN_SUNNY_POINTS;
        if(gameMode == GameMode.RELOADED) {
            this.sprite = GameAssets.getInstance(context).getCompostUnit(size);
            position = new Point(((int) (map.getTileSize()) * 2 + offsetFromLeft), (int) (map.getTileSize()) * 6);
        }

    }
}
