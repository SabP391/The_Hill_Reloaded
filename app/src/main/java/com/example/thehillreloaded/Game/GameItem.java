package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

// Classe degli oggetti di gioco
// Questa classe contiene il bitmap della sprite dell'oggetto
// un enum che indica la tipologia dell'oggetto
// e le variabili necessarie per gestire la posizione dell'oggetto
// a schermo e per gestire il suo movimento
// Inoltre, è presente una variabile che indica se l'oggetto è un oggetto
// normale o è un buff/debuff
public class GameItem {
    // Membri della classe -------------------------------------------------------------------------
    private final ItemType itemType;
    private Bitmap objectSprite;
    private Point position;
    private TileMap map;
    private final int initialTile;
    private int currentTile;
    private final BuffType buffType;
    private long startTime = 0;
    private static final int FALLING_SPEED = (int) (1000.0 / 15.0);

    // Costruttori di classe -----------------------------------------------------------------------

    // Costruttore per la modalità di gioco classica
    // gli oggetti vengono inizializzati assegnando come dimensione
    // al Bitmap la dimensione di una tile della tilemap
    public GameItem(int initialTile, TileMap map, Context context, ItemType itemType){
        this.initialTile = initialTile;
        this.currentTile = initialTile;
        this.position = map.getPositionFromTileIndex(initialTile);
        this.map = map;
        this.itemType = itemType;
        this.buffType = BuffType.NONE;
        Point spriteSize = new Point((int) map.getTileSize(), (int) map.getTileSize());
        assignSprite(context, spriteSize);

    }

    // Costruttore per la modalità di gioco reloaded
    // In questo caso il BuffType non è inizializzato di default a none
    // viene quindi scelta la sprite adatta per l'ogetto
    // in base al fatto che sia un buff o un debuff
    public GameItem(int initialTile, TileMap map, Context context, ItemType itemType, BuffType buffType){
        this.initialTile = initialTile;
        this.currentTile = initialTile;
        this.position = map.getPositionFromTileIndex(initialTile);
        this.map = map;
        this.itemType = itemType;
        // Viene effettuato un controllo sull'itemType
        // poichè i rifiuti radiattivi non possono essere buff/debuff
        if(this.itemType != ItemType.RADIOACTIVE){
            this.buffType = buffType;
        }
        else{
            this.buffType = BuffType.NONE;
        }
        Point spriteSize = new Point((int) map.getTileSize(), (int) map.getTileSize());
        // Controllo sul buffType
        switch(this.buffType){
            // Se l'oggetto non è un buff assegna le sprite standard
            case NONE:
                assignSprite(context, spriteSize);
                break;
            // Se l'oggetto è un buff assegna le sprite dei buff
            case DOUBLE_UNIT_POINTS:
            case REDUCE_PROCESSING_TIME:
            case REDUCE_UNIT_WEAR:
                assignSpriteBuff(context, spriteSize);
                break;
            // Se l'oggetto è un debuff assegna le spride dei debuff
            case NO_UNIT_POINTS:
            case INCREASE_UNIT_WEAR:
            case INCREASE_PROCESSING_TIME:
                assignSpriteDebuff(context, spriteSize);
                break;
        }
    }

    // Metodo che assegna la giusta sprite in base al tipo dell'oggetto
    // Questo metodo assegna le sprite degli oggetti standard, scegliendo
    // casualmente tra le possibili sprite associate al tipo di oggetto
    // esistenti
    public void assignSprite(Context context, Point spriteSize){
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
                objectSprite = GameAssets.getInstance(context).getRadioactive(spriteSize);
                break;
            default:
                throw new IllegalArgumentException("Non esiste questo tipo di item");
        }
    }

    // Metodo che assegna le sprite in base al tipo di oggetto
    // Questo metodo assegna le sprite agli oggetti buff
    public void assignSpriteBuff(Context context, Point spriteSize){
        switch (itemType){
            case ALUMINIUM:
                objectSprite = GameAssets.getInstance(context).getAlBuff(spriteSize);
                break;
            case COMPOST:
                objectSprite = GameAssets.getInstance(context).getCompostBuff(spriteSize);
                break;
            case EWASTE:
                objectSprite = GameAssets.getInstance(context).getEWasteBuff(spriteSize);
                break;
            case GLASS:
                objectSprite = GameAssets.getInstance(context).getGlassBuff(spriteSize);
                break;
            case PAPER:
                objectSprite = GameAssets.getInstance(context).getPaperBuff(spriteSize);
                break;
            case PLASTIC:
                objectSprite = GameAssets.getInstance(context).getPlasticBuff(spriteSize);
                break;
            case STEEL:
                objectSprite = GameAssets.getInstance(context).getSteelBuff(spriteSize);
                break;
            default:
                throw new IllegalArgumentException("Non esiste questo tipo di item");
        }
    }

    // Metodo che assegna le sprite in base al tipo di oggetto
    // Questo metodo assegna le sprite agli oggetti debuff
    public void assignSpriteDebuff(Context context, Point spriteSize){
        switch (itemType){
            case ALUMINIUM:
                objectSprite = GameAssets.getInstance(context).getAlDebuff(spriteSize);
                break;
            case COMPOST:
                objectSprite = GameAssets.getInstance(context).getCompostDebuff(spriteSize);
                break;
            case EWASTE:
                objectSprite = GameAssets.getInstance(context).getEwasteDebuff(spriteSize);
                break;
            case GLASS:
                objectSprite = GameAssets.getInstance(context).getGlassDebuff(spriteSize);
                break;
            case PAPER:
                objectSprite = GameAssets.getInstance(context).getPaperDebuff(spriteSize);
                break;
            case PLASTIC:
                objectSprite = GameAssets.getInstance(context).getPlasticDebuff(spriteSize);
                break;
            case STEEL:
                objectSprite = GameAssets.getInstance(context).getSteelDebuff(spriteSize);
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

    // Metodo che controlla se un oggetto di buff non può più
    // spostarsi (in tal caso verrà eliminato dallo schermo)
    public boolean checkForBuffDestruction(){
        if(buffType == BuffType.DOUBLE_UNIT_POINTS ||
                buffType == BuffType.REDUCE_PROCESSING_TIME ||
                buffType == BuffType.REDUCE_UNIT_WEAR){
            if(isTouchingTheBlueLine() || !map.isNextTileFree(currentTile)){
                return true;
            }
        }
        return false;
    }

    // Metodo che controlla che un oggetto sia in posizione di gameover
    // controllando che non sia oltre la linea rossa e che non
    // sia in fase di caduta
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

    // Metodo che controlla se l'oggetto si trova
    // nell'ultima riga della tilemap
    public boolean isTouchingTheBlueLine(){
        return (position.y >= map.getBlueLineYPosition() - map.getTileSize());
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

    public int getInitialTile() { return  initialTile; }

    public ItemType getItemType() { return itemType; }

    public BuffType getBuffType() { return buffType; }

}
