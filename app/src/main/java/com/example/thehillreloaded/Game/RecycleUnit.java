package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public abstract class RecycleUnit {

    protected static final int COST_OF_FIRST_UPGRADE = 4;
    protected static final int COST_OF_SECOND_UPGRADE_RELOADED = 8;

    protected GameMode gameMode;
    protected Context context;
    protected TileMap map;
    protected Bitmap sprite;
    protected Bitmap unitPointsIcon;
    protected int unitPoints = 50;
    protected Point size;
    protected Point position;
    protected int offsetFromLeft = 0;
    protected int offsetFromRight = 0;
    protected int myTiles[];
    protected RecycleUnitStatus unitStatus = RecycleUnitStatus.BASE;
    protected ItemType acceptedItemType;

    public RecycleUnit(TileMap map, Context context){
        this.gameMode = GameManager.getInstance().getGameMode();
        this.context = context;
        this.map = map;
        this.myTiles = new int[4];
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
    // Metodi utili --------------------------------------------------------------------------------

    // Metodo per inizializzare le tile su cui si trova l'unità
    public void initMyTiles(){
        this.myTiles[0] = map.getTileIndexFromPosition(position);
        this.myTiles[1] = this.myTiles[0] + 1;
        this.myTiles[2] = this.myTiles[0] + map.getMapSize().x;
        this.myTiles[3] = this.myTiles[2] + 1;
    }

    // Metodo per disegnare a schermo le unità di riciclo
    public void drawUnit(Canvas c){
        c.drawBitmap(sprite, position.x, position.y, null);
    }

    // Metodo per effettuare l'upgrade dell'unità
    public boolean upgradeUnit(){
        // Viene effettuato un controllo sullo stato in cui
        // si trova l'unità
        switch (unitStatus){
            // Se l'unità è allo stato base e ha abbastanza
            // unit points per effettuare l'upgrade
            // cambia lo stato dell'unità e sottrae gli unit points necessari
            case BASE:
                if(unitPoints >= COST_OF_FIRST_UPGRADE){
                    unitPoints -= COST_OF_FIRST_UPGRADE;
                    unitStatus = RecycleUnitStatus.UPGRADED_ONCE;
                    return true;
                }
                else return false;
                // Se l'unità è già stata migliorata una volta
                // controlla in che modalità ci si trova
                // e effettua l'upgrade di conseguenza
            case UPGRADED_ONCE:
                if(gameMode == GameMode.CLASSIC){
                    if(unitPoints >= COST_OF_FIRST_UPGRADE){
                        unitPoints -= COST_OF_FIRST_UPGRADE;
                        unitStatus = RecycleUnitStatus.UPGRADED_TWICE;
                        return true;
                    }
                    else return false;
                }else{
                    if(unitPoints >= COST_OF_SECOND_UPGRADE_RELOADED){
                        unitPoints -= COST_OF_SECOND_UPGRADE_RELOADED;
                        unitStatus = RecycleUnitStatus.UPGRADED_TWICE;
                        return true;
                    }
                    else return false;
                }
                // Se l'unità è stata già aggiornata
                // due volte ritorna false poiché non
                // è possibile aggiornarla oltre
            case UPGRADED_TWICE:
                return false;
            default:
                throw new IllegalStateException("Unexpected value: " + unitStatus);
        }
    }

    // Getter e setter------------------------------------------------------------------------------

    public int getUnitPoints() {
        return unitPoints;
    }

    public void setUnitPoints(int unitPoints) {
        this.unitPoints = unitPoints;
    }
}
