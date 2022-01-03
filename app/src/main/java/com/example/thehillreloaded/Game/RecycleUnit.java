package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.lang.reflect.Array;

public abstract class RecycleUnit {

    // Costanti relative alla logica di gioco ------------------------------------------------------
    protected static final int COST_OF_FIRST_UPGRADE = 4;
    protected static final int COST_OF_SECOND_UPGRADE_RELOADED = 8;
    protected static final int UNIT_POINT_GAIN = 1;
    protected static final int MAXIMUM_WEAR_LEVEL = 30;

    // Attributi di classe -------------------------------------------------------------------------
    protected GameMode gameMode;
    protected Context context;
    protected TileMap map;
    protected Bitmap sprite;
    protected int unitPoints = 0;
    protected Point size;
    protected Point position;
    protected int offsetFromLeft = 0;
    protected int offsetFromRight = 0;
    protected int myTiles[];
    protected RecycleUnitStatus unitStatus = RecycleUnitStatus.UPGRADED_TWICE;
    protected ItemType acceptedItemType;
    protected int currentWearLevel = 0;

    // Variabili per controllare quali degli slot di
    // lavoro di un'unità sono disponibili per accettare
    // l'inizio del riciclo di un oggetto ----------------------------------------------------------
    protected boolean isFirstSlotFree = true;
    protected boolean isSecondSlotFree = true;
    protected boolean isThirdSlotFree = true;
    protected long timeAtFirstSlotProcessStart = 0;
    protected long timeAtSecondSlotProcessStart = 0;
    protected long timeAtThirdSlotProcessStart = 0;


    // Attributi necessari per disegnare a schermo
    // gli slot di lavoro disponibili e usati ------------------------------------------------------
    protected int slotsXPosition;
    protected int firstSlotLineYPosition;
    protected int secondSlotLineYPosition;
    protected int thirdSlotLineYPosition;
    protected Paint grayLine;
    protected Paint redLine;

    public RecycleUnit(TileMap map, Context context){
        this.gameMode = GameManager.getInstance().getGameMode();
        this.context = context;
        this.map = map;
        this.myTiles = new int[4];
        this.offsetFromLeft = (int) (((map.getFirstTileOfTheHill() - 4) * map.getTileSize()) - map.getTileSize());
        if(this.offsetFromLeft < 0){
            this.offsetFromLeft = 0;
        }
        size = new Point((int) (map.getTileSize() * 2), (int) (map.getTileSize() * 2));

        this.offsetFromRight = (int) ((((map.getMapSize().x - (map.getFirstTileOfTheHill() + map.getNumberOfTileSOfTheHill())) - 4) * map.getTileSize()) - map.getTileSize());
        if(this.offsetFromRight < 0){
            this.offsetFromRight = 0;
        }

        grayLine = new Paint();
        grayLine.setColor(Color.GRAY);
        grayLine.setStrokeWidth(15);


        redLine = new Paint();
        redLine.setColor(Color.RED);
        redLine.setStrokeWidth(15);
    }
    // Metodi utili --------------------------------------------------------------------------------

    public abstract void setProcessSlotsPosition();

    // Metodo per inizializzare le tile su cui si trova l'unità
    public void initMyTiles(){
        this.myTiles[0] = map.getTileIndexFromPosition(position);
        this.myTiles[1] = this.myTiles[0] + 1;
        this.myTiles[2] = this.myTiles[0] + map.getMapSize().x;
        this.myTiles[3] = this.myTiles[2] + 1;
    }

    // Metodo per controllare che un valore passato
    // come argomento sia una delle tile occupata dall'unità
    public boolean isOneOfMyTiles(int tileIndex){
        for(int i = 0; i < Array.getLength(myTiles); i++){
            if(myTiles[i] == tileIndex){
                return true;
            }
        }
        return false;
    }

    // Metodo per disegnare a schermo le unità di riciclo
    public void drawUnit(Canvas c){
        c.drawBitmap(sprite, position.x, position.y, null);
        drawProcessSlots(c);
    }

    public void drawProcessSlots(Canvas c){
        switch (unitStatus){
            case BASE:
                c.drawLine(slotsXPosition,
                        firstSlotLineYPosition,
                        slotsXPosition + map.getTileSize(),
                        firstSlotLineYPosition,
                        grayLine);
                break;
            case UPGRADED_ONCE:
                c.drawLine(slotsXPosition,
                        firstSlotLineYPosition,
                        slotsXPosition + map.getTileSize(),
                        firstSlotLineYPosition, grayLine);
                c.drawLine(slotsXPosition,
                        secondSlotLineYPosition,
                        slotsXPosition + map.getTileSize(),
                        secondSlotLineYPosition,
                        grayLine);
                break;
            case UPGRADED_TWICE:
                c.drawLine(slotsXPosition,
                        firstSlotLineYPosition,
                        slotsXPosition + map.getTileSize(),
                        firstSlotLineYPosition, grayLine);
                c.drawLine(slotsXPosition,
                        secondSlotLineYPosition,
                        slotsXPosition + map.getTileSize(),
                        secondSlotLineYPosition,
                        grayLine);
                c.drawLine(slotsXPosition,
                        thirdSlotLineYPosition,
                        slotsXPosition + map.getTileSize(),
                        thirdSlotLineYPosition,
                        grayLine);
                break;
        }
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

    // Metodo che ritorna un intero tra 0 e 3 per
    // indicare qual è il primo slot di lavoro disponibile
    // in una recicle unit. Questo metodo effettua anche il controllo
    // sullo stato dell'unità, in modo da non ritornare
    // slot non disponibili sebbene non occupati
    public int firstFreeSlot(){
        int freeSlot = 0;
        switch (unitStatus){
            case BASE:
                if(isFirstSlotFree){
                    freeSlot = 1;
                }
                break;
            case UPGRADED_ONCE:
                if(isFirstSlotFree){
                    freeSlot = 1;
                }else if(isSecondSlotFree){
                    freeSlot = 2;
                }
                break;
            case UPGRADED_TWICE:
                if(isFirstSlotFree){
                    freeSlot = 1;
                }else if(isSecondSlotFree){
                    freeSlot = 2;
                }else if(isThirdSlotFree){
                    freeSlot = 3;
                }
                break;
        }
        return freeSlot;
    }

    // Metodi per il processamento degli item di gioco ---------------------------------------------

    // Metodo per processare gli oggetti nella modalità classica
    // Controlla che ci sia uno slot libero e nel caso cio` sia vero
    // inizia il processamento, imposta tale slot come occupato
    // e ritorna vero, altrimenti ritorna falso
    public boolean processItemClassic(GameItem item){
        if(item.getItemType() == this.acceptedItemType) {
            switch (firstFreeSlot()){
                case 0:
                    return false;
                case 1:
                    isFirstSlotFree = false;
                    timeAtFirstSlotProcessStart = System.nanoTime();
                    Log.d("free slot", "1");
                    unitPoints += UNIT_POINT_GAIN;
                    break;
                case 2:
                    isSecondSlotFree = false;
                    Log.d("free slot", "2");
                    timeAtSecondSlotProcessStart = System.nanoTime();
                    unitPoints += UNIT_POINT_GAIN;
                    break;
                case 3:
                    isThirdSlotFree = false;
                    Log.d("free slot", "3");
                    timeAtThirdSlotProcessStart = System.nanoTime();
                    unitPoints += UNIT_POINT_GAIN;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + firstFreeSlot());
            }
            return true;
        }
        else return false;
    }

    public boolean processItemReloaded(GameItem item){
        if(item.getItemType() == this.acceptedItemType) {
            switch (firstFreeSlot()){
                case 0:
                    return false;
                case 1:
                    isFirstSlotFree = false;
                    timeAtFirstSlotProcessStart = System.nanoTime();
                    Log.d("free slot", "1");
                    unitPoints += UNIT_POINT_GAIN;
                    break;
                case 2:
                    isSecondSlotFree = false;
                    timeAtSecondSlotProcessStart = System.nanoTime();
                    Log.d("free slot", "2");
                    unitPoints += UNIT_POINT_GAIN;
                    break;
                case 3:
                    isThirdSlotFree = false;
                    timeAtThirdSlotProcessStart = System.nanoTime();
                    Log.d("free slot", "3");
                    unitPoints += UNIT_POINT_GAIN;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + firstFreeSlot());
            }
        }else{
            switch (firstFreeSlot()){
                case 0:
                    return false;
                case 1:
                    isFirstSlotFree = false;
                    timeAtFirstSlotProcessStart = System.nanoTime();
                    Log.d("free slot", "1");
                    unitPoints += UNIT_POINT_GAIN;
                    break;
                case 2:
                    isSecondSlotFree = false;
                    timeAtSecondSlotProcessStart = System.nanoTime();
                    Log.d("free slot", "2");
                    unitPoints += UNIT_POINT_GAIN;
                    break;
                case 3:
                    isThirdSlotFree = false;
                    timeAtThirdSlotProcessStart = System.nanoTime();
                    Log.d("free slot", "3");
                    unitPoints += UNIT_POINT_GAIN;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + firstFreeSlot());
            }
        }
        return true;
    }

    public boolean processItem(GameItem item){
        boolean result;
        if(gameMode == GameMode.CLASSIC){
            result = processItemClassic(item);
        }
        else{
            result = processItemReloaded(item);
            if (item.getItemType() == ItemType.COMPOST) {
                QuestManager.getInstance().increaseCounterQuest4();
            }
        }
        return result;
    }

    // Getter e setter------------------------------------------------------------------------------

    public int getUnitPoints() {
        return unitPoints;
    }

    public void setUnitPoints(int unitPoints) {
        this.unitPoints = unitPoints;
    }

    public static int getMaximumWearLevel() {
        return MAXIMUM_WEAR_LEVEL;
    }

    public int getCurrentWearLevel() {
        return currentWearLevel;
    }

    public RecycleUnitStatus getUnitStatus() {
        return unitStatus;
    }
}
