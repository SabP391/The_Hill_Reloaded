package com.example.thehillreloaded.Game;

public class GameManager {
    private boolean isPaused = false;
    private static GameManager instance;
    private long timeAtGameStart;
    private long timeFromLastSpawn;
    private float spawnSpeed;
    private int sunnyPoints;

    private GameManager(){
        spawnSpeed = (float) (1000.0);
    }

    public static GameManager getInstance(){
        if(instance == null){
            instance = new GameManager();
        }
        return instance;
    }

    public void initInstance(){

        timeAtGameStart = System.nanoTime();
        timeFromLastSpawn = (long)spawnSpeed;
        sunnyPoints = 0;
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

}
