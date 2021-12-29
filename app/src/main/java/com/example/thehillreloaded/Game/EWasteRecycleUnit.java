package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Point;

public class EWasteRecycleUnit extends RecycleUnit{

    public EWasteRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.sprite = GameAssets.getInstance(context).getEwasteUnit(size);
        position = new Point((int)((map.getMapSize().x * map.getTileSize()) -
                (2 * map.getTileSize() + offsetFromRight)), (int)(map.getTileSize() * 2));

    }
}
