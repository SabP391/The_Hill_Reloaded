package com.example.thehillreloaded.Model;

public class GameStatistics {
    private String user;
    private int score;
    private long gameTime;

    public GameStatistics(String user, int score, long gameTime) {
        this.user = user;
        this.score = score;
        this.gameTime = gameTime;
    }

    public String getUser() {
        return user;
    }

    public int getScore() {
        return score;
    }

    public long getGameTime() {
        return gameTime;
    }
}
