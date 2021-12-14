package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    Intent tornaAdAccesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tornaAdAccesso = new Intent(this, AccessActivity.class);
    }

    public void OnClickTornaIndietro(View view){ finish(); }
    public void onClickEsci(View view) { startActivity(tornaAdAccesso); }
}