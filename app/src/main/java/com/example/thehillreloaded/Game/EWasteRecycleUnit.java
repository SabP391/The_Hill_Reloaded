package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Point;

public class EWasteRecycleUnit extends RecycleUnit{

    private static final int COST_IN_SUNNY_POINTS = 40;

    public EWasteRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.cost = COST_IN_SUNNY_POINTS;
        this.sprite = GameAssets.getInstance(context).getEwasteUnit(size);
        position = new Point((int)((map.getMapSize().x * map.getTileSize()) -
                (2 * map.getTileSize() + offsetFromRight)), (int)(map.getTileSize() * 2));

    }
}
