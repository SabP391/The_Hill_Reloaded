package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;

public class AluminiumRecycleUnit extends RecycleUnit{

    public AluminiumRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.sprite = GameAssets.getInstance(context).getAlUnit(size);
        position = new Point((int)((map.getMapSize().x * map.getTileSize()) -
                (4 * map.getTileSize() + offsetFromRight)), (int)(map.getTileSize() * 4));

    }

}
