package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Point;

public class PlasticRecycleUnit extends RecycleUnit{

    private static final int COST_IN_SUNNY_POINTS = 35;

    public PlasticRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.cost = COST_IN_SUNNY_POINTS;
        if(gameMode == GameMode.RELOADED) {
            this.sprite = GameAssets.getInstance(context).getPlasticUnit(size);
            position = new Point((0 + offsetFromLeft), (int) (map.getTileSize()) * 4);
        } else {
            this.sprite = GameAssets.getInstance(context).getPlasticUnit(size);
            position = new Point((((int) (map.getTileSize()) * 2) + offsetFromLeft), (int) (map.getTileSize()) * 6);
        }

    }
}
