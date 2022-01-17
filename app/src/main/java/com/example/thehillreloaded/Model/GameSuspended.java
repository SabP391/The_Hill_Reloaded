package com.example.thehillreloaded.Model;

import com.example.thehillreloaded.Game.Difficulty;
import com.example.thehillreloaded.Game.GameItem;
import com.example.thehillreloaded.Game.GameManager;
import com.example.thehillreloaded.Game.RecycleUnit;
import com.example.thehillreloaded.Game.TileMap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

//classe model che dovrà contenere tutti dati da salvare poi come oggetto su shared preferences
//è' una bozza
public class GameSuspended {
    //dati dinamici - verranno sovrascritti al momento in cui la prtita verrà ripresa -

    //GameManager
    private int sunnyPoints;
    private boolean isPaused;
    private long timeAtGameStart;
    private long timeAtGamePause;
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
    private ArrayList<Integer> tileMap;

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
    private Difficulty difficulty;
    private List<RecycleUnitSave> recycleUnitSave;

    public GameSuspended(boolean isPaused,int sunnyPoints, long timeAtGameStart, long timeAtGamePause, boolean isPaperUnitUnlocked, boolean isCompostUnlocked,
                         boolean isAluminiumUnitUnlocked, boolean isSteelUnitUnlocked, boolean isPlasticUnitUnlocked,
                         boolean isEwasteUnitUnlocked, boolean isGlassUnitUnlocked, boolean quest1, boolean quest2,
                         boolean quest3, int counterQuest3, boolean quest4, int counterQuest4, boolean quest5,
                         boolean quest6, int counterQuest6, GameManager instance, List<RecycleUnitSave> recycleUnitSave,
                         ArrayList<Integer> tileMap, Difficulty difficulty) {
        this.isPaused =isPaused;
        this.sunnyPoints = sunnyPoints;
        this.timeAtGameStart = timeAtGameStart;
        this.timeAtGamePause = timeAtGamePause;
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
        this.recycleUnitSave = recycleUnitSave;
        this.tileMap = tileMap;
        this.difficulty = difficulty;
    }

    public int getSunnyPoints() {
        return sunnyPoints;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public long getTimeAtGameStart() {
        return timeAtGameStart;
    }

    public long getTimeAtGamePause() {
        return timeAtGamePause;
    }

    public GameManager getInstance() {
        return instance;
    }

    public boolean isPaperUnitUnlocked() {
        return isPaperUnitUnlocked;
    }

    public boolean isCompostUnlocked() {
        return isCompostUnlocked;
    }

    public boolean isAluminiumUnitUnlocked() {
        return isAluminiumUnitUnlocked;
    }

    public boolean isSteelUnitUnlocked() {
        return isSteelUnitUnlocked;
    }

    public boolean isPlasticUnitUnlocked() {
        return isPlasticUnitUnlocked;
    }

    public boolean isEwasteUnitUnlocked() {
        return isEwasteUnitUnlocked;
    }

    public boolean isGlassUnitUnlocked() {
        return isGlassUnitUnlocked;
    }

    public ArrayList<Integer> getTileMap() {
        return tileMap;
    }

    public boolean isQuest1() {
        return quest1;
    }

    public boolean isQuest2() {
        return quest2;
    }

    public boolean isQuest3() {
        return quest3;
    }

    public int getCounterQuest3() {
        return counterQuest3;
    }

    public boolean isQuest4() {
        return quest4;
    }

    public int getCounterQuest4() {
        return counterQuest4;
    }

    public boolean isQuest5() {
        return quest5;
    }

    public boolean isQuest6() {
        return quest6;
    }

    public int getCounterQuest6() {
        return counterQuest6;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public List<RecycleUnitSave> getRecycleUnitSave() {
        return recycleUnitSave;
    }
}
