package com.example.thehillreloaded.Model;

public class GameStatistics {

    private String email;
    private int score;
    private long gameTime;

    public GameStatistics() {
    }

    public GameStatistics(String email, int score, long gameTime) {
        this.email = email;
        this.score = score;
        this.gameTime = gameTime;
    }

    public String getEmail() {
        return email;
    }

    public int getScore() {
        return score;
    }

    public long getGameTime() {
        return gameTime;
    }
}
