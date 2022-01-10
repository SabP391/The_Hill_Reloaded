package com.example.thehillreloaded.Model;

//classe model che dovrà contenere tutti dati statici da salvare su db firebase a partita conclusa
//è' una bozza
public class GameEnded {
    private int totalScore;
    private long gameTime;
    private long actualDate;
    private String email;

    public GameEnded(int totalScore, long gameTime, long actualDate, String email) {
        this.totalScore = totalScore;
        this.gameTime = gameTime;
        this.actualDate = actualDate;
        this.email = email;
    }

    public int getTotalScore() {
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
}
