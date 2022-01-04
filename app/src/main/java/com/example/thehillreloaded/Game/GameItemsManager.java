package com.example.thehillreloaded.Game;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameItemsManager {
    public static GameItemsManager instance;
    private ConcurrentLinkedQueue<GameItem> itemsOnScreen;
    private TileMap map;
    private Context context;
    private Random rand;
    private Difficulty difficulty;
    private GameMode gameMode;
    private ArrayList<ItemType> itemTypes;
    private int spawnedItemsCounter = 0;

    private GameItemsManager(){
        itemsOnScreen = new ConcurrentLinkedQueue<GameItem>();
        rand = new Random();
        itemTypes = new ArrayList<ItemType>();
        itemTypes.add(ItemType.GLASS);
    }

    public static GameItemsManager getInstance(){
        if(instance == null){
            instance = new GameItemsManager();
        }
        return instance;
    }

    public void initInstance(TileMap map, Context context, Difficulty difficulty, GameMode gameMode){
        this.map = map;
        this.context = context;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
    }

    public void spawnNewObject(){
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
        int initialTile = rand.nextInt(map.getNumberOfTileSOfTheHill()) + map.getFirstTileOfTheHill();
        Log.d("arraySize", String.valueOf(itemTypes.size()));
        itemsOnScreen.add(new GameItem(initialTile, map, context, itemTypes.get(rand.nextInt(itemTypes.size()))));
        spawnedItemsCounter +=1;
    }

    // Getter e setter -----------------------------------------------------------------------------

    public ConcurrentLinkedQueue<GameItem> getItemsOnScreen() { return itemsOnScreen; }
}
