package com.example.thehillreloaded.Game;

import android.content.Context;
import android.service.quicksettings.Tile;

public class GameManager {
    private boolean isPaused = false;
    private static GameManager instance;
    private long timeAtGameStart;
    private long timeFromLastSpawn;
    private float spawnSpeed;
    private int sunnyPoints;
    private int totalSunnyPoints;
    private GameMode gameMode;
    private Difficulty difficulty;
    private SunnyPointsCounter sunnyPointsCounter;

    private GameManager(){
        spawnSpeed = (float) (1000.0);
    }

    public static GameManager getInstance(){
        if(instance == null){
            instance = new GameManager();
        }
        return instance;
    }

    public void initInstance(GameMode gameMode, Difficulty difficulty, Context context, TileMap map){

        this.gameMode = gameMode;
        this.difficulty = difficulty;
        timeAtGameStart = System.nanoTime();
        timeFromLastSpawn = (long)spawnSpeed;
        sunnyPoints = 0;
        sunnyPointsCounter = new SunnyPointsCounter(map, context);
    }

    public boolean isTimeToSpawn(long currentTime){
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
