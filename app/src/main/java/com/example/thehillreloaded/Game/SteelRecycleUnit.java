package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Point;

public class SteelRecycleUnit extends RecycleUnit{

    private static final int COST_IN_SUNNY_POINTS = 30;

    public SteelRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.cost = COST_IN_SUNNY_POINTS;
        if(gameMode == GameMode.RELOADED) {
            this.sprite = GameAssets.getInstance(context).getSteelUnit(size);
            position = new Point(((int) (map.getTileSize()) * 2 + offsetFromLeft), (int) (map.getTileSize()) * 2);
        } else {
            this.sprite = GameAssets.getInstance(context).getSteelUnit(size);
            position = new Point((0 + offsetFromLeft), (int) (map.getTileSize()) * 4);
        }

    }
}
