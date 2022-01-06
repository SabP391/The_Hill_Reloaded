package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameOverActivity extends AppCompatActivity {
    Intent mainMenu;
    Intent replay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        mainMenu = new Intent(this, GuestMenuActivity.class);
        replay = new Intent(this, GameActivity.class);
    }

    public void backToMainMenu(View view){
        startActivity(mainMenu);
    }

    public void replayGame(View view){
        startActivity(replay);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(mainMenu);
    }
}