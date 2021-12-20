package com.example.thehillreloaded.Game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

public class GameObject {
    private Bitmap objectSprite;
    private Point position;
    private TileMap map;
    private int initialTile;
    private int currentTile;
    private boolean onScreen = false;

    public GameObject(Bitmap image, int initialTile, TileMap map){
        this.objectSprite = image;
        this.initialTile = initialTile;
        this.currentTile = initialTile;
        this.position = new Point((int) (initialTile * map.getTileSize()), 0);
        this.map = map;
    }

    public void move(){
        if(onScreen){
            if(currentTile < map.mapSize.y * map.mapSize.x - (map.mapSize.x + initialTile)){
                if(map.tileMap.get(currentTile + map.mapSize.x) != 1) {
                    map.tileMap.set(currentTile, 0);
                    position.y += (int) (map.tileSize);
                    currentTile = currentTile + map.mapSize.x;
                    map.tileMap.set(currentTile, 1);
                }
            }
        }
    }

    public void drawObject(Canvas c){
        if(onScreen){
            c.drawBitmap(objectSprite, position.x, position.y, null);
        }
    }

    public void setOnScreen(boolean bool){
        onScreen = bool;
    }
}
