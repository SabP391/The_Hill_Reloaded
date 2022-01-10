package com.example.thehillreloaded.Game;

import android.content.Context;
import android.util.Log;

public class GameManager {
    private boolean isPaused = false;
    private static GameManager instance;
    private long timeAtGameStart;
    private PlayTime playTime;
    private long timeToGetThingsSpicy;
    private long timeFromLastSpawn;
    private float spawnSpeed;
    private int sunnyPoints;
    private int totalSunnyPoints;
    private GameMode gameMode;
    private Difficulty difficulty;
    private SunnyPointsCounter sunnyPointsCounter;
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
        sunnyPoints = 1000;
        sunnyPointsCounter = new SunnyPointsCounter(map, context);
        timeToGetThingsSpicy = 0;

        // Imposta l'intervallo di spawn degli oggetti in base alla difficoltà
        if(difficulty == Difficulty.EASY){
            spawnSpeed = (float) (800.0);
        }
        if(difficulty == Difficulty.NORMAL){
            spawnSpeed = (float) (600.0);
        }
        if(difficulty == Difficulty.HARD){
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

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public SunnyPointsCounter getSunnyPointsCounter() {
        return sunnyPointsCounter;
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
