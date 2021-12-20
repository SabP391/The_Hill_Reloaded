package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.thehillreloaded.Game.Game;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inizializza la classe di gioco
        Game game = new Game(this);
        // Imposta la contentView alla SurfaceView creata in game
        setContentView(game);
    }
}