package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

public abstract class RecycleUnit {
    protected GameMode gameMode;
    protected Context context;
    protected TileMap map;
    protected Bitmap sprite;
    protected Bitmap unitPointsIcon;
    protected int unitPoints = 0;
    protected Point size;
    protected Point position;
    protected int offsetFromLeft = 0;
    protected int offsetFromRight = 0;
    protected int myTiles[];
    protected ItemType acceptedItemType;

    public RecycleUnit(TileMap map, Context context){
        this.gameMode = GameManager.getInstance().getGameMode();
        this.context = context;
        this.map = map;
        Point unitPointIconSize = new Point((int)(map.getTileSize() / 4), (int)(map.getTileSize() / 4));
        this.unitPointsIcon = GameAssets.getInstance(context).getUnitPointsIcon(unitPointIconSize);
        this.offsetFromLeft = (int) (((map.getFirstTileOfTheHill() - 4) * map.getTileSize()) - map.getTileSize());
        if(this.offsetFromLeft < 0){
            this.offsetFromLeft = 0;
        }
        size = new Point((int) (map.getTileSize() * 2), (int) (map.getTileSize() * 2));

        this.offsetFromRight = (int) ((((map.getMapSize().x - (map.getFirstTileOfTheHill() + map.getNumberOfTileSOfTheHill())) - 4) * map.getTileSize()) - map.getTileSize());
        if(this.offsetFromRight < 0){
            this.offsetFromRight = 0;
        }
    }

    protected void drawUnit(Canvas c){
        c.drawBitmap(sprite, position.x, position.y, null);
        c.drawBitmap(unitPointsIcon, (int)(position.x + 2 * map.getTileSize()), position.y, null);
    }

    // Getter e setter------------------------------------------------------------------------------

}
