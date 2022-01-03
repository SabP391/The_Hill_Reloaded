package com.example.thehillreloaded.Model;

//classe model che dovrà contenere tutti dati statici da salvare su db firebase a partita conclusa
//è' una bozza
public class GameEnded {
    private String mailUtente;
    private int totalScore;
    private long gameTime;

    public GameEnded(String mailUtente, int totalScore, long gameTime) {
        this.mailUtente = mailUtente;
        this.totalScore = totalScore;
        this.gameTime = gameTime;
    }

    public String getMailUtente() {
        return mailUtente;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public long getGameTime() {
        return gameTime;
    }
}
