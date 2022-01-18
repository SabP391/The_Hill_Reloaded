package com.example.thehillreloaded.Game;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.thehillreloaded.Model.GameEnded;
import com.example.thehillreloaded.Model.GameSuspended;
import com.google.gson.Gson;

public class GameManager {
    private boolean isPaused = false;
    private static GameManager instance;
    private long timeAtGameStart;
    private PlayTime playTime;
    private long timeToGetThingsSpicy;
    private long timeFromLastSpawn;
    private float spawnSpeed;
    private int sunnyPoints = 0;
    private int totalSunnyPoints = 0;
    private GameMode gameMode;
    private Difficulty difficulty;
    private SunnyPointsCounter sunnyPointsCounter;

    // Variabili per il gioco multiplayer
    private boolean isMultiplayerGame = false;

    // Intervallo di tempo in cui aumenterà la difficoltà della partita
    private static final int TIME_TO_INCREASE_DIFFICULTY = 60;
    // Incremento della difficoltà di gioco al passare dell'intervallo di tempo
    private static final int SPAWN_SPEED_INCREASE = 20;

    private GameManager(){
        playTime = new PlayTime();
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
        sunnyPointsCounter = new SunnyPointsCounter(map, context);
        timeToGetThingsSpicy = 0;

        // Imposta l'intervallo di spawn degli oggetti in base alla difficoltà
        if(difficulty == Difficulty.EASY){
            spawnSpeed = (float) (1000.0);
        }
        if(difficulty == Difficulty.NORMAL){
            spawnSpeed = (float) (750.0);
        }
        if(difficulty == Difficulty.HARD){
            spawnSpeed = (float) (500.0);
        }
        // Per il multiplayer la velocità è più alta
        if(isMultiplayerGame){
            spawnSpeed = (float) (400.0);
        }
    }


    public void gameManagerReload(boolean isPaused,int sunnyPoints, long timeAtGameStart,
                                  GameManager instance, Difficulty difficulty, GameMode gameMode, Context context, TileMap map){
        this.isPaused = isPaused;
        this.sunnyPoints = sunnyPoints;
        this.timeAtGameStart = timeAtGameStart;
        this.instance = instance;
        this.difficulty = difficulty;
        this.gameMode = gameMode;

        sunnyPointsCounter = new SunnyPointsCounter(map, context);
        timeToGetThingsSpicy = 0;

        // Imposta l'intervallo di spawn degli oggetti in base alla difficoltà
        if(difficulty == Difficulty.EASY){
            spawnSpeed = (float) (1000.0);
        }
        if(difficulty == Difficulty.NORMAL){
            spawnSpeed = (float) (750.0);
        }
        if(difficulty == Difficulty.HARD){
            spawnSpeed = (float) (500.0);
        }
        // Per il multiplayer la velocità è più alta
        if(isMultiplayerGame){
            spawnSpeed = (float) (400.0);
        }
    }


    public void destroy(){
        instance = null;
    }

    public boolean isTimeToSpawn(long currentTime){
        // Calcola il tempo trascorso dall'inizio della partita in secondi
        long timeElapsed = (currentTime - timeAtGameStart) / 1000000000;
        // Se è maggiore del tempo previsto per l'aumento della difficoltà e minore di 600 secondi (10 minuti)
        if((timeElapsed > TIME_TO_INCREASE_DIFFICULTY + timeToGetThingsSpicy) && (timeElapsed <= 600)){
            // Conta quante volte è stato aumentato il tempo
            timeToGetThingsSpicy += TIME_TO_INCREASE_DIFFICULTY;
            // Aumenta la velocità di spawn
            spawnSpeed -= SPAWN_SPEED_INCREASE;
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

    public void addSunnyPoint(int pointsToAdd){
        this.sunnyPoints = this.sunnyPoints + pointsToAdd;
        this.totalSunnyPoints = this.totalSunnyPoints + pointsToAdd;
        sunnyPointsCounter.updateCounter(sunnyPoints);
    }


    // Getter e setter -----------------------------------------------------------------------------


    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public static void setInstance(GameManager instance) {
        GameManager.instance = instance;
    }

    public long getTimeAtGameStart() {
        return timeAtGameStart;
    }

    public void setTimeAtGameStart(long timeAtGameStart) {
        this.timeAtGameStart = timeAtGameStart;
    }

    public PlayTime getPlayTime() {
        return playTime;
    }

    public void setPlayTime(PlayTime playTime) {
        this.playTime = playTime;
    }

    public long getTimeToGetThingsSpicy() {
        return timeToGetThingsSpicy;
    }

    public void setTimeToGetThingsSpicy(long timeToGetThingsSpicy) {
        this.timeToGetThingsSpicy = timeToGetThingsSpicy;
    }

    public long getTimeFromLastSpawn() {
        return timeFromLastSpawn;
    }

    public void setTimeFromLastSpawn(long timeFromLastSpawn) {
        this.timeFromLastSpawn = timeFromLastSpawn;
    }

    public float getSpawnSpeed() {
        return spawnSpeed;
    }

    public void setSpawnSpeed(float spawnSpeed) {
        this.spawnSpeed = spawnSpeed;
    }

    public int getSunnyPoints() {
        return sunnyPoints;
    }

    public void setSunnyPoints(int sunnyPoints) {
        this.sunnyPoints = sunnyPoints;
    }

    public int getTotalSunnyPoints() {
        return totalSunnyPoints;
    }

    public void setTotalSunnyPoints(int totalSunnyPoints) {
        this.totalSunnyPoints = totalSunnyPoints;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public SunnyPointsCounter getSunnyPointsCounter() { return sunnyPointsCounter;}

    //Getter per prendere l'inizio della partita

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setSunnyPointsCounter(SunnyPointsCounter sunnyPointsCounter) {
        this.sunnyPointsCounter = sunnyPointsCounter;
    }

    public static int getTimeToIncreaseDifficulty() {
        return TIME_TO_INCREASE_DIFFICULTY;
    }

    public static int getSpawnSpeedIncrease() {
        return SPAWN_SPEED_INCREASE;
    }

    public boolean isMultiplayerGame() {
        return isMultiplayerGame;
    }

    public void setMultiplayerGame(boolean multiplayerGame) {
        isMultiplayerGame = multiplayerGame;
    }

}
