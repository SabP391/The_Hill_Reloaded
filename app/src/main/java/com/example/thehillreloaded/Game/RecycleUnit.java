package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.widget.Toast;

import com.example.thehillreloaded.R;

import java.lang.reflect.Array;

public abstract class RecycleUnit {

    // Costanti relative alla logica di gioco ------------------------------------------------------
    protected static final int COST_OF_FIRST_UPGRADE = 4;
    protected static final int COST_OF_SECOND_UPGRADE_RELOADED = 8;
    protected static final int UNIT_POINT_GAIN = 1;
    protected static final int MAXIMUM_WEAR_LEVEL = 15;
    protected static final int WEAR_LEVEL_INCREASE = 1;
    protected static final long PROCESSING_TIME = 5;
    protected static final long MAX_BUFF_TIME = 15;

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
    protected RecycleUnitStatus unitStatus = RecycleUnitStatus.BASE;
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

    // Variabili per la gestione di buff e debuff --------------------------------------------------
    protected int unitPointMultiplier = 1;
    protected int wearMultiplier = 1;
    protected float processTimeMultiplier = 1;
    protected boolean isBuffed = false;
    protected boolean isDebuffed = false;
    protected long timeAtBuffStart = 0;


    // Attributi necessari per disegnare a schermo
    // gli slot di lavoro disponibili e usati ------------------------------------------------------
    protected int slotsXPosition;
    protected int firstSlotLineYPosition;
    protected int secondSlotLineYPosition;
    protected int thirdSlotLineYPosition;
    protected Paint grayLine;
    protected Paint redLine;
    protected Point warningIconSize;
    protected Point upIconSize;

    // Attributi per disegnare a schermo le icone di avviso ----------------------------------------
    protected Bitmap wearWarning;
    protected Bitmap unitPointsIcon;
    protected Bitmap buffIcon;
    protected Bitmap debuffIcon;

    protected Paint upValue;

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

        warningIconSize = new Point((int) map.getTileSize()/2, (int) map.getTileSize()/2);
        upIconSize = new Point((int) map.getTileSize()/4, (int) map.getTileSize()/4);
        wearWarning = GameAssets.getInstance(context).getWearWarningIcon(warningIconSize);
        unitPointsIcon = GameAssets.getInstance(context).getUnitPointsIcon(upIconSize);
        buffIcon = GameAssets.getInstance(context).getBuffIcon(warningIconSize);
        debuffIcon =  GameAssets.getInstance(context).getDebuffIcon(warningIconSize);
        upValue = new Paint();
        upValue.setTextSize(30);
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

    // Metodi per il rendering delle centrali ------------------------------------------------------

    // Metodo per disegnare a schermo le unità di riciclo
    public void drawUnit(Canvas c, long currentTime){
        c.drawBitmap(sprite, position.x, position.y, null);
        drawProcessSlots(c, currentTime);
        drawWearWarning(c);
        drawUnitPoints(c);
        drawBuff(c, currentTime);
    }

    // Metodo per disegnare l'icona di warning se
    // il wear level raggiunge livelli critici
    public void drawWearWarning(Canvas c){
        if(currentWearLevel >= MAXIMUM_WEAR_LEVEL - 5) {
            c.drawBitmap(wearWarning, position.x, position.y, null);
        }
    }

    public void drawUnitPoints(Canvas c){
        c.drawBitmap(unitPointsIcon, slotsXPosition , secondSlotLineYPosition + map.getTileSize()/3, null);
        c.drawText( String.valueOf(unitPoints), slotsXPosition + map.getTileSize()/4, secondSlotLineYPosition + map.getTileSize()/2, upValue);
    }

    // Metod per disegnare l'icona di buff o debuff
    // se sull'unità è presente uno tra i due
    public void drawBuff(Canvas c, long currentTime){
        long elapsedTime = (currentTime - timeAtBuffStart) / 1000000000;
        // controllo sulla variabile che controlla che ci sia un buff
        if(isBuffed){
            // Se il tempo trascorso è inferiore alla durata del buff
            // disegna l'icona
            if(elapsedTime <= MAX_BUFF_TIME){
                c.drawBitmap(buffIcon,
                        position.x + (2 * map.getTileSize()) - buffIcon.getWidth(),
                        position.y,
                        null);
            }
            // Altrimenti reimposta a false la variabile
            // e riporta ad 1 tutti i moltplicatori
            else{
                isBuffed = false;
                processTimeMultiplier = 1;
                wearMultiplier = 1;
                unitPointMultiplier = 1;
            }
        }
        // stessa cosa avviene nel caso sia un debuff
        if(isDebuffed){
            if(elapsedTime <= MAX_BUFF_TIME){
                c.drawBitmap(debuffIcon,
                    position.x + (2 * map.getTileSize()) - debuffIcon.getWidth(),
                    position.y,
                    null);
            }
            else{
                isDebuffed = false;
                processTimeMultiplier = 1;
                wearMultiplier = 1;
                unitPointMultiplier = 1;
            }
        }
    }

    // Metodo per disegnare a schermo gli slot di lavoro
    // delle unità di riciclo
    public void drawProcessSlots(Canvas c, long currentTime){
        // Controllo sullo stato dell'unità:
        // disegna a schermo un numero diverso di linee
        // in base a quanti upgrade sono stati eseguiti
        switch (unitStatus){
            case BASE:
                c.drawLine(slotsXPosition,
                        firstSlotLineYPosition,
                        slotsXPosition + map.getTileSize(),
                        firstSlotLineYPosition,
                        grayLine);
                // Metodo per disegnare la linea rossa quando
                // il primo slot di lavoro è occupato
                drawFirstSlotProgress(c, currentTime);
                break;
            case UPGRADED_ONCE:
                c.drawLine(slotsXPosition,
                        firstSlotLineYPosition,
                        slotsXPosition + map.getTileSize(),
                        firstSlotLineYPosition, grayLine);
                drawFirstSlotProgress(c, currentTime);
                c.drawLine(slotsXPosition,
                        secondSlotLineYPosition,
                        slotsXPosition + map.getTileSize(),
                        secondSlotLineYPosition,
                        grayLine);
                // Metodo per disegnare la linea rossa quando
                // il primo secondo di lavoro è occupato
                drawSecondSlotProgress(c, currentTime);
                break;
            case UPGRADED_TWICE:
                c.drawLine(slotsXPosition,
                        firstSlotLineYPosition,
                        slotsXPosition + map.getTileSize(),
                        firstSlotLineYPosition, grayLine);
                drawFirstSlotProgress(c, currentTime);
                c.drawLine(slotsXPosition,
                        secondSlotLineYPosition,
                        slotsXPosition + map.getTileSize(),
                        secondSlotLineYPosition,
                        grayLine);
                drawSecondSlotProgress(c, currentTime);
                c.drawLine(slotsXPosition,
                        thirdSlotLineYPosition,
                        slotsXPosition + map.getTileSize(),
                        thirdSlotLineYPosition,
                        grayLine);
                // Metodo per disegnare la linea rossa quando
                // il terzo slot di lavoro è occupato
                drawThirdSlotProgress(c, currentTime);
                break;
        }
    }

    // Metodo per disegnare la linea rossa quando
    // il primo slot di lavoro è occupato
    public void drawFirstSlotProgress(Canvas c, long currentTime) {
        if(!isFirstSlotFree){
            long elapsedTime = (currentTime - timeAtFirstSlotProcessStart) / 1000000000;
            // il moltiplicatore della linea rossa viene normalizzato
            // tramite il metodo redLineMultiplier
            float redLineM = redLineMultiplier(elapsedTime);
            // Se lo slot di lavoro è occupato disegna la linea rossa
            if(elapsedTime < PROCESSING_TIME * processTimeMultiplier){
                c.drawLine(slotsXPosition,
                        firstSlotLineYPosition,
                        slotsXPosition + (map.getTileSize() * redLineM),
                        firstSlotLineYPosition,
                        redLine);
            }
            // Altrimenti reimposta a true la variabile di controllo
            // per liberare lo slot di lavoro
            else{
                unitPoints += UNIT_POINT_GAIN * unitPointMultiplier;
                isFirstSlotFree = true;
            }
        }
    }

    // Metodo per disegnare la linea rossa quando
    // il seondo slot di lavoro è occupato
    public void drawSecondSlotProgress(Canvas c, long currentTime) {
        if(!isSecondSlotFree){
            long elapsedTime = (currentTime - timeAtSecondSlotProcessStart) / 1000000000;
            // il moltiplicatore della linea rossa viene normalizzato
            // tramite il metodo redLineMultiplier
            float redLineM = redLineMultiplier(elapsedTime);
            // Se lo slot di lavoro è occupato disegna la linea rossa
            if(elapsedTime < PROCESSING_TIME){
                c.drawLine(slotsXPosition,
                        secondSlotLineYPosition,
                        slotsXPosition + (map.getTileSize() * redLineM),
                        secondSlotLineYPosition,
                        redLine);
            }
            // Altrimenti reimposta a true la variabile di controllo
            // per liberare lo slot di lavoro
            else{
                unitPoints += UNIT_POINT_GAIN * unitPointMultiplier;
                isSecondSlotFree = true;
            }
        }
    }

    // Metodo per disegnare la linea rossa quando
    // il seondo slot di lavoro è occupato
    public void drawThirdSlotProgress(Canvas c, long currentTime) {
        if(!isThirdSlotFree){
            long elapsedTime = (currentTime - timeAtThirdSlotProcessStart) / 1000000000;
            // il moltiplicatore della linea rossa viene normalizzato
            // tramite il metodo redLineMultiplier
            float redLineM = redLineMultiplier(elapsedTime);
            // Se lo slot di lavoro è occupato disegna la linea rossa
            if(elapsedTime < PROCESSING_TIME * processTimeMultiplier){
                c.drawLine(slotsXPosition,
                        thirdSlotLineYPosition,
                        slotsXPosition + (map.getTileSize() * redLineM),
                        thirdSlotLineYPosition,
                        redLine);
            }
            // Altrimenti reimposta a true la variabile di controllo
            // per liberare lo slot di lavoro
            else{
                unitPoints += UNIT_POINT_GAIN * unitPointMultiplier;
                isThirdSlotFree = true;
            }
        }
    }


    // Metodo per calcolare il moltiplicatore della linea rossa
    // disegnata a schermo per mostrare l'avanzamento del lavoro.
    // Prende in input il tempo di lavoro trascorso e ritorna
    // un valore tra 0 e 1 che verrà moltiplicato alla posizione x finale
    // della linea rossa
    public float redLineMultiplier(double elapsedTime){
        return (float)((elapsedTime / (PROCESSING_TIME * processTimeMultiplier)) +
                (1.0 / (PROCESSING_TIME * processTimeMultiplier)));
    }

    // Metodi per l'upgrade delle centrali ---------------------------------------------------------

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
                        if(currentWearLevel>0){
                            currentWearLevel = 0;
                        }
                        unitPoints -= COST_OF_FIRST_UPGRADE;
                        unitStatus = RecycleUnitStatus.UPGRADED_TWICE;
                        return true;
                    }
                    else return false;
                }else{
                    if(unitPoints >= COST_OF_SECOND_UPGRADE_RELOADED){
                        if(currentWearLevel>0){
                            currentWearLevel = 0;
                        }
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

    public void downgradeUnit() {
        if (unitStatus == RecycleUnitStatus.UPGRADED_TWICE) {
            this.unitStatus = RecycleUnitStatus.UPGRADED_ONCE;
            this.currentWearLevel = 0;
        } else if (unitStatus == RecycleUnitStatus.UPGRADED_ONCE) {
            this.unitStatus = RecycleUnitStatus.BASE;
            this.currentWearLevel = 0;
        }
        downgradeMessage();
    }

    public abstract void downgradeMessage();

    public void wearLevelCalculator(){
        if(unitStatus == RecycleUnitStatus.UPGRADED_ONCE || unitStatus == RecycleUnitStatus.UPGRADED_TWICE) {
            currentWearLevel += (WEAR_LEVEL_INCREASE * wearMultiplier);
            if (currentWearLevel == MAXIMUM_WEAR_LEVEL) {
                downgradeUnit();
            }
        }
    }

    // Metodi per il processamento degli item di gioco ---------------------------------------------

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

    // Metodo per processare gli oggetti nella modalità classica
    // Controlla che ci sia uno slot libero e nel caso cio` sia vero
    // inizia il processamento, imposta tale slot come occupato
    // e ritorna vero, altrimenti ritorna falso
    public boolean processItemClassic(GameItem item){
        // Controlla che l'oggetto sia dello stesso tipo dell'unità
        if(item.getItemType() == this.acceptedItemType) {
            // Controlla che ci siano slot di lavoro liberi
            switch (firstFreeSlot()){
                // Se non ce ne sono ritorna subito false
                case 0:
                    return false;
                // Se ce ne sono, imposta il primo slot disponibile
                // come occupato e registra il tempo di inizio del processamento
                case 1:
                    isFirstSlotFree = false;
                    timeAtFirstSlotProcessStart = System.nanoTime();
                    if(item.getItemType() == ItemType.PAPER){
                        QuestManager.getInstance().increaseCounterQuest6();
                    }
                    break;
                case 2:
                    isSecondSlotFree = false;
                    timeAtSecondSlotProcessStart = System.nanoTime();
                    if(item.getItemType() == ItemType.PAPER){
                        QuestManager.getInstance().increaseCounterQuest6();
                    }
                    break;
                case 3:
                    isThirdSlotFree = false;
                    timeAtThirdSlotProcessStart = System.nanoTime();
                    if(item.getItemType() == ItemType.PAPER){
                        QuestManager.getInstance().increaseCounterQuest6();
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + firstFreeSlot());
            }
            // Nel caso in cui venga trovato uno slot libero
            // ritorna true
            return true;
        }
        // Viene ritornato false anche nel caso in cui
        // l'oggetto sia di un tipo differente rispetto a quello dell'unità
        else return false;
    }

    // Metodo per processare gli oggetti nella modalità reloaded
    // Controlla che ci sia uno slot libero e nel caso cio` sia vero
    // inizia il processamento, imposta tale slot come occupato
    // e ritorna vero, altrimenti ritorna falso
    // In questo caso vengono processati anche gli item di tipo diverso
    // da quello dell'unità, applicando però un malus
    // Viene infine effettuato un controllo sui buff e debuff
    public boolean processItemReloaded(GameItem item){
        // Controlla che l'oggetto sia dello stesso tipo dell'unità
        if(item.getItemType() == this.acceptedItemType) {
            // Controlla che ci siano slot di lavoro liberi
            switch (firstFreeSlot()){
                // Se non ce ne sono ritorna subito false
                case 0:
                    return false;
                // Se ce ne sono, imposta il primo slot disponibile
                // come occupato e registra il tempo di inizio del processamento
                case 1:
                    isFirstSlotFree = false;
                    timeAtFirstSlotProcessStart = System.nanoTime();
                    if(item.getItemType() == ItemType.PAPER){
                        QuestManager.getInstance().increaseCounterQuest6();
                    }
                    break;
                case 2:
                    isSecondSlotFree = false;
                    timeAtSecondSlotProcessStart = System.nanoTime();
                    if(item.getItemType() == ItemType.PAPER){
                        QuestManager.getInstance().increaseCounterQuest6();
                    }
                    break;
                case 3:
                    isThirdSlotFree = false;
                    timeAtThirdSlotProcessStart = System.nanoTime();
                    if(item.getItemType() == ItemType.PAPER){
                        QuestManager.getInstance().increaseCounterQuest6();
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + firstFreeSlot());
            }
            // Se sull'unità non sono già presenti buff o debuff
            // applica l'effetto del buff o debuff dell'oggetto processato, se presente
            if(!isBuffed && !isDebuffed) {
                startBuff(item);
                startDebuff(item);
                // Se l'oggetto presenta un buff ma l'unità
                // ha già un buff atttivo, l'oggetto viene processato come
                // un oggetto normale e viene mostrato un messaggio
            }else if(item.getBuffType() != BuffType.NONE){
                Toast.makeText(context, R.string.power_up_gia_attivo, Toast.LENGTH_SHORT).show();
            }
        // Se l'oggetto processato non è dello stesso tipo dell'unità
        }else{
            // Controlla che il giocatore abbia abbastanza unit point
            // da applicare il malus
            if(unitPoints >= UNIT_POINT_GAIN * 2 && GameManager.getInstance().getSunnyPoints() >= 1){
                // Controlla che l'oggetto non presenti buff o debuff
                if(item.getBuffType() == BuffType.NONE) {
                    // Controlla che ci sia almento uno slot di lavoro libero
                    switch (firstFreeSlot()) {
                        // Nel caso non ci siano ritorna subito false
                        case 0:
                            return false;
                        // Altrimenti inizia il processamento sul primo
                        // slot disponibile
                        case 1:
                            isFirstSlotFree = false;
                            timeAtFirstSlotProcessStart = System.nanoTime();
                            break;
                        case 2:
                            isSecondSlotFree = false;
                            timeAtSecondSlotProcessStart = System.nanoTime();
                            break;
                        case 3:
                            isThirdSlotFree = false;
                            timeAtThirdSlotProcessStart = System.nanoTime();
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + firstFreeSlot());
                    }
                    // Se l'oggetto viene procesato, rimuove unit points e sunny points
                    unitPoints -= UNIT_POINT_GAIN * 2;
                    GameManager.getInstance().subtractSunnyPoints(1);
                }else {
                    // Se l'oggetto presenta un buff o debuff e viene
                    // portato sulla centrale di tipo errato viene mostrato un messaggio
                    // di errore e viene ritornato false
                    Toast.makeText(context, R.string.power_up_centrale_sbagliata, Toast.LENGTH_SHORT).show();
                    return false;
                }
                // Ritorna false in ogni altro caso
            }else{
                return false;
            }
        }
        return true;
    }

    // Metodo che applica uno o l'altro dei metodi di processamenti
    // in base a che la modalità di gioco sia classic o reloaded
    public boolean processItem(GameItem item){
        boolean result;
        if(gameMode == GameMode.CLASSIC){
            result = processItemClassic(item);
            wearLevelCalculator();
        }
        else{
            result = processItemReloaded(item);
            wearLevelCalculator();
            if (item.getItemType() == ItemType.COMPOST) {
                QuestManager.getInstance().increaseCounterQuest4();
            }
        }
        return result;
    }

    // Metodo per gestire i buff della centrale
    public void startBuff(GameItem item){
        // Viene effettuato un controllo sul tipo di buff e
        // viene modificato il moltiplicatore associato a tale buff
        // infine viene mostrato un messaggio su quale buff sia stato attivato
        switch (item.getBuffType()){
            case DOUBLE_UNIT_POINTS:
                unitPointMultiplier = 2;
                Toast.makeText(context, R.string.unit_points_raddoppiati, Toast.LENGTH_SHORT).show();
                break;
            case REDUCE_UNIT_WEAR:
                wearMultiplier = 0;
                Toast.makeText(context, R.string.usura_ridotta, Toast.LENGTH_SHORT).show();
                break;
            case REDUCE_PROCESSING_TIME:
                processTimeMultiplier = (float) 0.5;
                Toast.makeText(context, R.string.processamento_ridotto, Toast.LENGTH_SHORT).show();
                break;
            default:
                return;
        }
        // Se viene effettivamente applicato un buff viene
        // impostata la variabile di controllo a true e viene registrato il tempo
        isBuffed = true;
        timeAtBuffStart = System.nanoTime();
    }

    // Metodo per gestire i debuff della centrale
    public void startDebuff(GameItem item){
        switch (item.getBuffType()){
            // Viene effettuato un controllo sul tipo di debuff e
            // viene modificato il moltiplicatore associato a tale debuff
            // infine viene mostrato un messaggio su quale debuff sia stato attivato
            case NO_UNIT_POINTS:
                unitPointMultiplier = 0;
                Toast.makeText(context, R.string.unit_points_ridotti, Toast.LENGTH_SHORT).show();
                break;
            case INCREASE_UNIT_WEAR:
                wearMultiplier = 2;
                Toast.makeText(context, R.string.usura_raddoppiata, Toast.LENGTH_SHORT).show();
                break;
            case INCREASE_PROCESSING_TIME:
                processTimeMultiplier = (float) 1.5;
                Toast.makeText(context, R.string.processamento_aumentato, Toast.LENGTH_SHORT).show();
                break;
            default:
                return;
        }
        // Se viene effettivamente applicato un debuff viene
        // impostata la variabile di controllo a true e viene registrato il tempo
        isDebuffed = true;
        timeAtBuffStart = System.nanoTime();
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

    public ItemType getAcceptedItemType() { return acceptedItemType;}
}
