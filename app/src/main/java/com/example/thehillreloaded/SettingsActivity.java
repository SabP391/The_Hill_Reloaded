package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    Intent tornaAdAccesso;
    GoogleSignInWrapper autenticazione;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tornaAdAccesso = new Intent(this, AccessActivity.class);
        if(!autenticazione.getInstance(this).isLogged(this)){
            View bottoneImpostazioni = findViewById(R.id.bottone_esci);
            bottoneImpostazioni.setVisibility(View.GONE);
        }
    }

    public void OnClickTornaIndietro(View view){ finish(); }
    public void onClickEsci(View view) {
        autenticazione.getInstance(this).logout();
        startActivity(tornaAdAccesso);
    }
}