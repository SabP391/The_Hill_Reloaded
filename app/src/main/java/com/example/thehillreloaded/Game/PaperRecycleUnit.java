package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Point;

public class PaperRecycleUnit extends RecycleUnit{

    public PaperRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.sprite = GameAssets.getInstance(context).getPaperUnit(size);
        position = new Point((int)((map.getMapSize().x * map.getTileSize()) -
                (4 * map.getTileSize() + offsetFromRight)), (int)((map.getTileSize())));

    }
}
