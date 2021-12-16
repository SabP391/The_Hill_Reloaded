package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GuestMenuActivity extends AppCompatActivity {

    Intent menuImpostazioni;
    Intent menuAccesso;
    Button nuovaPartita;
    Fragment modalitaGioco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_menu);

        menuImpostazioni = new Intent(this, SettingsActivity.class);
        menuAccesso = new Intent(this, AccessActivity.class);

        nuovaPartita = findViewById(R.id.bottone_inizia_ospite);
        nuovaPartita.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                modalitaGioco = new GameModeFragment();
                selezionaModalitàFragment(modalitaGioco);
            }
        });
    }


    private void selezionaModalitàFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.fragment_modalita_ospite, fragment)
                .addToBackStack("fragment1")
                .commit();
    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStackImmediate();
        else super.onBackPressed();
    }

    public void onClickAccesso(View view) {
        startActivity(menuAccesso);
        finish();
    }

    public void onClickImpostazioni(View view) { startActivity(menuImpostazioni); }
}