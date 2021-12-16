package com.example.thehillreloaded;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserMenuActivity extends AppCompatActivity {

    Intent menuImpostazioni;
    Button iniziaPartita;
    Fragment modalitaGiocoF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        menuImpostazioni = new Intent(this, SettingsActivity.class);

        iniziaPartita = findViewById(R.id.bottone_inizia_utente);
        iniziaPartita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modalitaGiocoF = new GameModeFragment();
                selezionaModalitàFragment(modalitaGiocoF);

            }
        });


    }

    private void selezionaModalitàFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.fragment_modalita, fragment)
                .addToBackStack("fragment1")
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