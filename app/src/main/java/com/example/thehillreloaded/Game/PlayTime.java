package com.example.thehillreloaded.Game;

// Classe in cui verrà accumulato il tempo di gioco
public class PlayTime {

    private int minutes;
    private int seconds;

    void PlayTime(){
        this.minutes = 0;
        this.seconds = 0;
    }

    // Metodo che incrementa i secondi
    // se il contatore dei secondi arriva a 60, aggiunge  un
    // minuto e azzera nuovamente i secondi
    public void increasePlayTime(){
        if(seconds < 59){
            seconds++;
        }
        else{
            seconds = 0;
            minutes++;
        }
    }

    @Override
    public String toString() {
        return  String.format("%02d:%02d", minutes, seconds);
    }

    // Getter e setter -----------------------------------------------------------------------------
    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
