package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameWonActivity extends AppCompatActivity {
        Intent mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_won);

        mainMenu = new Intent(this, GuestMenuActivity.class);
    }
    
    public void backToMainMenu(View view){
        startActivity(mainMenu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(mainMenu);
    }
}