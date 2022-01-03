package com.example.thehillreloaded.Model;

import java.util.ArrayList;

//classe model che dovrà contenere tutti dati da salvare poi come oggetto su shared preferences
//è' una bozza
public class GameSuspended {
    //dati dinamici - verranno sovrascritti al momento in cui la prtita verrà ripresa -


    private String mailUtente;
    private int temporaryScore;
    private long temporaryGameTime;
    private ArrayList<Integer> tm;

    public GameSuspended(String mailUtente, int temporaryScore, long temporaryGameTime, ArrayList<Integer> tm) {
        this.mailUtente = mailUtente;
        this.temporaryScore = temporaryScore;
        this.temporaryGameTime = temporaryGameTime;
        this.tm = tm;
    }

    public String getMailUtente() {
        return mailUtente;
    }

    public int getTemporaryScore() {
        return temporaryScore;
    }

    public long getTemporaryGameTime() {
        return temporaryGameTime;
    }

    public ArrayList<Integer> getTm() {
        return tm;
    }
}
