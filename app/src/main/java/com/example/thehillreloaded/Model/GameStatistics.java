package com.example.thehillreloaded.Model;

public class GameStatistics {

    private String email;
    private int score;
    //private long gameTime;
    private long gameTimeMinutes;
    private long gameTimeSeconds;

    public GameStatistics() {
    }

    public GameStatistics(String email, int score, long gameTime) {
        this.email = email;
        this.score = score;
        this.gameTimeMinutes = gameTimeMinutes;
        this.gameTimeSeconds = gameTimeSeconds;
    }

    public String getEmail() {
        return email;
    }

    public int getScore() {
        return score;
    }

    public long getGameTimeMinutes() {
        return gameTimeMinutes;
    }

    public long getGameTimeSeconds() {
        return gameTimeSeconds;
    }
}
