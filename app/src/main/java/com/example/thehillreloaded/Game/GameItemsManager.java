package com.example.thehillreloaded.Game;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

// Classe che si occupa di gestire gli oggetti a schermo
// dallo spawn alla loro rimozione
public class GameItemsManager {
    // Attributi della classe ----------------------------------------------------------------------
    public static GameItemsManager instance;
    private ConcurrentLinkedQueue<GameItem> itemsOnScreen;
    private TileMap map;
    private Context context;
    private Random rand;
    private Difficulty difficulty;
    private GameMode gameMode;
    private ArrayList<ItemType> itemTypes;
    private ArrayList<BuffType> buffTypes;
    private int spawnedItemsCounter = 0;

    // Costruttore e metodi di inizializzazione ----------------------------------------------------
    // Costruttore della clase
    private GameItemsManager(){
        itemsOnScreen = new ConcurrentLinkedQueue<GameItem>();
        rand = new Random();
        itemTypes = new ArrayList<ItemType>(20);
        itemTypes.add(ItemType.GLASS);
        itemTypes.add(ItemType.GLASS);
        itemTypes.add(ItemType.GLASS);
        itemTypes.add(ItemType.PAPER);
        if(gameMode == GameMode.CLASSIC){
            itemTypes.add(ItemType.ALUMINIUM);
        } else {
            itemTypes.add(ItemType.COMPOST);
        }

        buffTypes = new ArrayList<>(60);
        // Inizializza l'arraylist degi tipi di buff
        // Questo array viene inizializzato aggiungendo un numero maggiore
        // di "NONE" per far si che la probabilità di generare
        // un buff/debuff sia più bassa di quella di generare un oggetto standard
        for(int i = 0; i < 50; i++){
            buffTypes.add(BuffType.NONE);
        }
        for(BuffType b : BuffType.values()){
            buffTypes.add(b);
        }
        Collections.shuffle(buffTypes);
    }

    public static GameItemsManager getInstance(){
        if(instance == null){
            instance = new GameItemsManager();
        }
        return instance;
    }

    public void destroy(){
        instance = null;
    }

    // Metodo per inizializzare la classe
    public void initInstance(Context context, TileMap map){
        this.map = map;
        this.context = context;
        this.difficulty = GameManager.getInstance().getDifficulty();
        this.gameMode = GameManager.getInstance().getGameMode();
    }

    public void gameItemsManagerReload(Context context, TileMap map){
        this.map = map;
        this.context = context;
        this.difficulty = GameManager.getInstance().getDifficulty();
        this.gameMode = GameManager.getInstance().getGameMode();

        for(int i = 0; i < map.getTileMap().size(); i++){
            if(map.getTileValue(i) == 1){
                spawnObjectInATile(i);
            }
        }
    }

    public void initInstanceMultyplayer(Context context, TileMap map){
        this.map = map;
        this.context = context;
        itemTypes.add(ItemType.PAPER);
        itemTypes.add(ItemType.COMPOST);
        itemTypes.add(ItemType.PLASTIC);
        itemTypes.add(ItemType.STEEL);
        itemTypes.add(ItemType.ALUMINIUM);
        itemTypes.add(ItemType.EWASTE);
    }
    // Metodi utili --------------------------------------------------------------------------------
    // Metodo che aggiunge nuovi oggetti a schermo, generando automaticamente
    // la tile da cui inizia
    public void spawnNewObject(){
        // Viene generata casualmente la tile di partenza
        // dell'oggetto nel range di tile che determinano
        // la prima riga della collina
        int initialTile = rand.nextInt(map.getNumberOfTileSOfTheHill()) + map.getFirstTileOfTheHill();
        spawnObjectInATile(initialTile);
    }

    // Metodo per generare un oggetto in una determinata tile
    public void spawnObjectInATile(int initialTile){
        // Viene effettuato un controllo su quanti ogetti sono stati
        // generati e aggiunge una nuova categoria
        // di oggetti ad intervalli specificati
        addItemType();
        // Viene usato il cotruttore appropriato in base a che la modalità
        // sia classica o reloaded
        if(gameMode == GameMode.CLASSIC){
            itemsOnScreen.add(
                    new GameItem(initialTile,
                            map,
                            context,
                            itemTypes.get(rand.nextInt(itemTypes.size()))));
        }
        // Nel caso la modalità sia reloaded viene generato casualmente
        // anche il tipo di buff
        else{
            itemsOnScreen.add(
                    new GameItem(initialTile,
                            map,
                            context,
                            itemTypes.get(rand.nextInt(itemTypes.size())),
                            buffTypes.get(rand.nextInt(buffTypes.size()))));
        }
        spawnedItemsCounter +=1;
    }

    // Metodo per aggiungere gli item type ogni intervallo prefissato di oggetti
    // generati.
    public void addItemType() {
        switch (spawnedItemsCounter) {
            case 10:
                itemTypes.add(ItemType.GLASS);
                itemTypes.add(ItemType.PAPER);
                break;
            case 15:
                itemTypes.add(ItemType.GLASS);
                if (gameMode == GameMode.RELOADED) {
                    itemTypes.add(ItemType.PAPER);
                    itemTypes.add(ItemType.COMPOST);
                    Collections.shuffle(itemTypes);
                }
                break;
            case 30:
                itemTypes.add(ItemType.GLASS);
                itemTypes.add(ItemType.PAPER);
                if (gameMode == GameMode.RELOADED) {
                    itemTypes.add(ItemType.COMPOST);
                }else{
                    itemTypes.add(ItemType.PAPER);
                }
                itemTypes.add(ItemType.ALUMINIUM);
                Collections.shuffle(itemTypes);
                break;
            case 40:
                itemTypes.add(ItemType.ALUMINIUM);
                itemTypes.add(ItemType.PAPER);
                itemTypes.add(ItemType.STEEL);
                Collections.shuffle(itemTypes);
                break;
            case 45:
                itemTypes.add(ItemType.PLASTIC);
                itemTypes.add(ItemType.STEEL);
                Collections.shuffle(itemTypes);
                break;
            case 50:
                itemTypes.add(ItemType.EWASTE);
                Collections.shuffle(itemTypes);
                break;
            case 95:
                if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) {
                    itemTypes.add(ItemType.RADIOACTIVE);
                    break;
                }
        }
    }

    public void spawnNewObjectMultiplayer(){
        // Viene generata casualmente la tile di partenza
        // dell'oggetto nel range di tile che determinano
        // la prima riga della collina
        int initialTile = rand.nextInt(map.getNumberOfTileSOfTheHill()) + map.getFirstTileOfTheHill();
        // Viene usato il cotruttore appropriato in base a che la modalità
        // sia classica o reloaded
        if(gameMode == GameMode.CLASSIC){
            itemsOnScreen.add(
                    new GameItem(initialTile,
                            map,
                            context,
                            itemTypes.get(rand.nextInt(itemTypes.size()))));
        }
        // Nel caso la modalità sia reloaded viene generato casualmente
        // anche il tipo di buff
        else{
            itemsOnScreen.add(
                    new GameItem(initialTile,
                            map,
                            context,
                            itemTypes.get(rand.nextInt(itemTypes.size())),
                            buffTypes.get(rand.nextInt(buffTypes.size()))));
        }
        spawnedItemsCounter +=1;
    }

    // Getter e setter -----------------------------------------------------------------------------

    public ConcurrentLinkedQueue<GameItem> getItemsOnScreen() { return itemsOnScreen; }
}
