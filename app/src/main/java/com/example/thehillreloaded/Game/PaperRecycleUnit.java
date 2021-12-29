package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Point;

public class PaperRecycleUnit extends RecycleUnit{

    private static final int COST_IN_SUNNY_POINTS = 12;

    public PaperRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.cost = COST_IN_SUNNY_POINTS;
        this.sprite = GameAssets.getInstance(context).getPaperUnit(size);
        position = new Point((int)((map.getMapSize().x * map.getTileSize()) -
                (4 * map.getTileSize() + offsetFromRight)), (int)((map.getTileSize())));

    }
}
