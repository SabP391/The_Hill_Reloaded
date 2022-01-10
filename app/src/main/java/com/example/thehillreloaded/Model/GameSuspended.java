package com.example.thehillreloaded.Model;

import com.example.thehillreloaded.Game.GameItem;
import com.example.thehillreloaded.Game.GameManager;
import com.example.thehillreloaded.Game.RecycleUnit;
import com.example.thehillreloaded.Game.TileMap;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

//classe model che dovrà contenere tutti dati da salvare poi come oggetto su shared preferences
//è' una bozza
public class GameSuspended {
    //dati dinamici - verranno sovrascritti al momento in cui la prtita verrà ripresa -

    //GameManager
    private int sunnyPoints;
    private boolean isPaused;
    private long timeAtGameStart;
    private GameManager instance;

    //Manca il getter
    //private int totalSunnyPoints;

    //RecycleUnitsManager
    //Da problemi sul Gson errore di riferimento circolare (proponeva l'ident transient)
    //private ConcurrentLinkedQueue<RecycleUnit> unlockedUnits;
    //private ConcurrentLinkedQueue<GameItem> itemsOnScreen;
    private boolean isPaperUnitUnlocked;
    private boolean isCompostUnlocked;
    private boolean isAluminiumUnitUnlocked;
    private boolean isSteelUnitUnlocked;
    private boolean isPlasticUnitUnlocked;
    private boolean isEwasteUnitUnlocked;
    private boolean isGlassUnitUnlocked;

    //Non c'è il metodo getter per prendere la tileMap
    //private ArrayList<Integer> tileMap;
    //private TileMap map;



    //QuestManager
    private boolean quest1;
    private boolean quest2;
    private boolean quest3;
    private int counterQuest3;
    private boolean quest4;
    private int counterQuest4;
    private boolean quest5;
    private boolean quest6;
    private int counterQuest6;

    public GameSuspended(boolean isPaused,int sunnyPoints, long timeAtGameStart, boolean isPaperUnitUnlocked, boolean isCompostUnlocked,
                         boolean isAluminiumUnitUnlocked, boolean isSteelUnitUnlocked, boolean isPlasticUnitUnlocked,
                         boolean isEwasteUnitUnlocked, boolean isGlassUnitUnlocked, boolean quest1, boolean quest2,
                         boolean quest3, int counterQuest3, boolean quest4, int counterQuest4, boolean quest5,
                         boolean quest6, int counterQuest6, GameManager instance) {
        this.isPaused =isPaused;
        this.sunnyPoints = sunnyPoints;
        this.timeAtGameStart = timeAtGameStart;
        this.isPaperUnitUnlocked = isPaperUnitUnlocked;
        this.isCompostUnlocked = isCompostUnlocked;
        this.isAluminiumUnitUnlocked = isAluminiumUnitUnlocked;
        this.isSteelUnitUnlocked = isSteelUnitUnlocked;
        this.isPlasticUnitUnlocked = isPlasticUnitUnlocked;
        this.isEwasteUnitUnlocked = isEwasteUnitUnlocked;
        this.isGlassUnitUnlocked = isGlassUnitUnlocked;
        this.quest1 = quest1;
        this.quest2 = quest2;
        this.quest3 = quest3;
        this.counterQuest3 = counterQuest3;
        this.quest4 = quest4;
        this.counterQuest4 = counterQuest4;
        this.quest5 = quest5;
        this.quest6 = quest6;
        this.counterQuest6 = counterQuest6;
        this.instance = instance;
    }

}
