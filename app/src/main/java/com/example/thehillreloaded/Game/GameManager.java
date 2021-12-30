package com.example.thehillreloaded.Game;

import android.content.Context;
import android.util.Log;

public class GameManager {
    private boolean isPaused = false;
    private static GameManager instance;
    private long timeAtGameStart;
    private long timeToGetThingsSpicy;
    private long timeFromLastSpawn;
    private float spawnSpeed;
    private int sunnyPoints;
    private int totalSunnyPoints;
    private GameMode gameMode;
    private Difficulty difficulty;
    private SunnyPointsCounter sunnyPointsCounter;

    // Intervallo di tempo in cui aumenterà la difficoltà della partita
    private static final int TIME_TO_INCREASE_DIFFICULTY = 60;
    // Incremento della difficoltà di gioco al passare dell'intervallo di tempo
    private static final int SPAWN_SPEED_INCREASE = 20;

    private GameManager(){
    }

    public static GameManager getInstance(){
        if(instance == null){
            instance = new GameManager();
        }
        return instance;
    }

    public void initInstance(GameMode gameMode, Difficulty difficulty, Context context, TileMap map){
        timeAtGameStart = System.nanoTime();
        this.gameMode = gameMode;
        this.difficulty = difficulty;
        timeAtGameStart = System.nanoTime();
        timeFromLastSpawn = (long)spawnSpeed;
        sunnyPoints = 1000;
        sunnyPointsCounter = new SunnyPointsCounter(map, context);
        timeToGetThingsSpicy = 0;

        if(difficulty == Difficulty.EASY){
            spawnSpeed = (float) (1000.0);
        }
        if(difficulty == Difficulty.NORMAL){
            spawnSpeed = (float) (800.0);
        }
        if(difficulty == Difficulty.HARD){
            spawnSpeed = (float) (500.0);
        }
    }

    public boolean isTimeToSpawn(long currentTime){
        long timeElapsed = (currentTime - timeAtGameStart) / 1000000000;
        if((timeElapsed > TIME_TO_INCREASE_DIFFICULTY + timeToGetThingsSpicy) && (timeElapsed <= 600)){
            timeToGetThingsSpicy += TIME_TO_INCREASE_DIFFICULTY;
            spawnSpeed -= SPAWN_SPEED_INCREASE;
            Log.d("Time elapsed", String.valueOf(timeToGetThingsSpicy));
            Log.d( "Current Spanw speed", String.valueOf(spawnSpeed));
        }
        if((currentTime - timeFromLastSpawn) / 10000000 >= spawnSpeed){
            timeFromLastSpawn = currentTime;
            return true;
        }
        return false;
    }

    public boolean isPaused(){
        return isPaused;
    }

    public void pause(){
        isPaused = true;
    }

    public void unPause(){
        isPaused = false;
    }

    public void subtractSunnyPoints(int pointsToSubtract){
        this.sunnyPoints = this.sunnyPoints - pointsToSubtract;
        sunnyPointsCounter.updateCounter(sunnyPoints);
    }

    // Getter e setter -----------------------------------------------------------------------------
    public int getSunnyPoints(){
        return sunnyPoints;
    }

    public void setSunnyPoints(int points){
        this.sunnyPoints = points;
        sunnyPointsCounter.updateCounter(sunnyPoints);
    }

    public GameMode getGameMode(){ return  gameMode; }

    public SunnyPointsCounter getSunnyPointsCounter() { return sunnyPointsCounter;}
}
