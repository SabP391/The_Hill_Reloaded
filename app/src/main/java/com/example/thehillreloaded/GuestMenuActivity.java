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
    Button nuovaPartita;
    Fragment modalitaGioco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_menu);

        menuImpostazioni = new Intent(this, SettingsActivity.class);

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
                .addToBackStack("fragment1")
                .add(R.id.fragment_modalita_ospite, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStackImmediate();
        else super.onBackPressed();
    }

    public void onClickImpostazioni(View view) { startActivity(menuImpostazioni); }
}