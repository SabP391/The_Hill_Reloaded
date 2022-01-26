package com.example.thehillreloaded.Model;

import com.example.thehillreloaded.Game.PlayTime;

//classe model che dovrà contenere tutti dati statici da salvare su db firebase a partita conclusa
//è' una bozza
public class GameEnded {
    private long totalScore;
    private long gameTime;
    private long actualDate;
    private String email;
    //private PlayTime playTime;
    long minutes;
    long seconds;

    public GameEnded() {
    }

    public GameEnded(long totalScore, long gameTime, long actualDate, String email, long minutes, long seconds ) {
        this.totalScore = totalScore;
        this.gameTime = gameTime;
        this.actualDate = actualDate;
        this.email = email;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public long getGameTime() {
        return gameTime;
    }

    public long getActualDate() {
        return actualDate;
    }

    public String getEmail() {
        return email;
    }

    public long getMinutes() { return minutes; }

    public long getSeconds() { return seconds; }
}
