package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GuestMenuActivity extends AppCompatActivity {

    Intent menuImpostazioni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_menu);

        menuImpostazioni = new Intent(this, SettingsActivity.class);
    }

    public void onClickImpostazioni(View view) { startActivity(menuImpostazioni); }
}