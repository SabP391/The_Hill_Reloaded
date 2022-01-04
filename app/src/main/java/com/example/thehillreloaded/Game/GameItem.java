package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;


public class GameItem {
    // Membri della classe -------------------------------------------------------------------------
    private final ItemType itemType;
    private final Bitmap objectSprite;
    private Point position;
    private TileMap map;
    private final int initialTile;
    private int currentTile;
    private boolean onScreen = false;
    private boolean isABuff = false;
    private boolean isADebuff = false;
    private long startTime = 0;
    private static final int FALLING_SPEED = (int) (1000.0 / 15.0);

    // Costruttori di classe -----------------------------------------------------------------------
    public GameItem(int initialTile, TileMap map, Context context, ItemType itemType){
        this.initialTile = initialTile;
        this.currentTile = initialTile;
        this.position = new Point((int) (initialTile * map.getTileSize()), 0);
        this.map = map;
        this.itemType = itemType;
        Point spriteSize = new Point((int) map.getTileSize(), (int) map.getTileSize());
        switch (itemType){
            case ALUMINIUM:
                objectSprite = GameAssets.getInstance(context).getRandAl(spriteSize);
                break;
            case COMPOST:
                objectSprite = GameAssets.getInstance(context).getRandCompost(spriteSize);
                break;
            case EWASTE:
                objectSprite = GameAssets.getInstance(context).getRandEWaste(spriteSize);
                break;
            case GLASS:
                objectSprite = GameAssets.getInstance(context).getRandGlass(spriteSize);
                break;
            case PAPER:
                objectSprite = GameAssets.getInstance(context).getRandPaper(spriteSize);
                break;
            case PLASTIC:
                objectSprite = GameAssets.getInstance(context).getRandPlastic(spriteSize);
                break;
            case STEEL:
                objectSprite = GameAssets.getInstance(context).getRandSteel(spriteSize);
                break;
            case RADIOACTIVE:
                objectSprite = GameAssets.getInstance(context).getRandSteel(spriteSize);
                break;
            default:
                throw new IllegalArgumentException("Non esiste questo tipo di item");
        }
    }

    // Metodi per la gestione dell'oggetto in gioco ------------------------------------------------
    // Metodo per gestire la caduta dell'oggetto sullo schermo
    // Il metodo permette all'oggetto di cadere solo finchè
    // non arriva in fondo allo schermo e se la tile
    // successiva a quella attualmente occupata è libera
    public void fall(long currentTime){
        long tempTime = (currentTime - startTime) / 10000000;
        if(tempTime > FALLING_SPEED){
            if(!isTouchingTheBlueLine()){
                if(map.isNextTileFree(currentTile)) {
                    map.setTileValue(currentTile, 0);
                    position.y += (int) (map.getTileSize());
                    currentTile = map.getNextTileIndex(currentTile);
                    map.setTileValue(currentTile, 1);
                }
            }
            startTime = currentTime;
        }
    }

    public boolean checkForGameOverPosition(){
        if(isOverTheRedLine() && !map.isNextTileFree(currentTile)){
            return true;
        }
        return false;
    }

    // Metodo per disegnare a schermo gli oggetti di gioco
    public void drawObject(Canvas c){
        c.drawBitmap(objectSprite, position.x, position.y, null);
    }

    // Metodi utili --------------------------------------------------------------------------------
    public void setOnScreen(boolean bool){
        onScreen = bool;
    }

    // Metodo che controlla se l'oggetto si trova
    // nell'ultima riga della tilemap
    public boolean isTouchingTheBlueLine(){
        return !(currentTile < (map.getMapSize().y * map.getMapSize().x) - (map.getMapSize().x - initialTile));
    }

    public boolean isOverTheRedLine(){
        if((currentTile >= map.getFirstTileOfTheHill()) && (currentTile <= map.getFirstTileOfTheHill() + map.getNumberOfTileSOfTheHill())){
            return true;
        }else return false;
    }

    // Getter e setter -----------------------------------------------------------------------------
    public Point getPosition(){
        return position;
    }

    public void setPosition(Point pos){
        this.position = pos;
    }

    public int getCurrentTile(){
        return currentTile;
    }

    public ItemType getItemType() { return itemType; }
}
