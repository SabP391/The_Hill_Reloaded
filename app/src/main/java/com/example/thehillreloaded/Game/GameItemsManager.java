package com.example.thehillreloaded.Game;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
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
        itemTypes = new ArrayList<ItemType>(8);
        itemTypes.add(ItemType.GLASS);
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

    // Metodi utili --------------------------------------------------------------------------------
    // Metodo che aggiunge nuovi oggetti a schermo
    public void spawnNewObject(){
        // Viene effettuato un controllo su quanti ogetti sono stati
        // generati e aggiunge una nuova categoria
        // di oggetti ad intervalli specificati
        switch(spawnedItemsCounter){
            case 5:
                itemTypes.add(ItemType.PAPER);
                break;
            case 15:
                if(gameMode == GameMode.RELOADED){
                    itemTypes.add(ItemType.COMPOST);
                }
                break;
            case 20:
                itemTypes.add(ItemType.ALUMINIUM);
                break;
            case 25:
                itemTypes.add(ItemType.STEEL);
                break;
            case 30:
                itemTypes.add(ItemType.PLASTIC);
                break;
            case 35:
                itemTypes.add(ItemType.EWASTE);
                break;
            case 40:
                if(difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD){
                    itemTypes.add(ItemType.RADIOACTIVE);
                    break;
                }
        }
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
