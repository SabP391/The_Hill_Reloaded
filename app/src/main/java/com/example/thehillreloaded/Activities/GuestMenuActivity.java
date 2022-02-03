package com.example.thehillreloaded.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

import com.example.thehillreloaded.Fragments.DifficultyFragment;
import com.example.thehillreloaded.Fragments.GameModeFragment;
import com.example.thehillreloaded.R;
import com.example.thehillreloaded.Services.SoundEffectService;

public class GuestMenuActivity extends AppCompatActivity implements GameModeFragment.SoundFX, DifficultyFragment.SoundFX {
    //variabili per service
    SoundEffectService soundService;
    boolean soundServiceBound = false;
    Intent effettiSonori;

    //variabili per le SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    //boolean per controllare lo stato degli effetti sonori nelle shared preferences
    boolean SFXattivi;

    Intent menuImpostazioni;
    Intent menuAccesso;
    Button nuovaPartita;
    Fragment modalitaGioco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_menu);

        effettiSonori = new Intent(this, SoundEffectService.class);
        menuImpostazioni = new Intent(this, SettingsActivity.class);
        menuAccesso = new Intent(this, AccessActivity.class);
        nuovaPartita = findViewById(R.id.bottone_inizia_guest);
    }

    @Override
    protected void onStart() {
        super.onStart();

        pref = getApplicationContext().getSharedPreferences("HillR_pref", MODE_PRIVATE);
        editor = pref.edit();
        SFXattivi = pref.getBoolean("SFX_attivi", true);
        //binding del service per gli effetti sonori
        bindService(effettiSonori, soundServiceConnection, Context.BIND_AUTO_CREATE);

        nuovaPartita.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(SFXattivi){ soundService.suonoBottoni(); }
                modalitaGioco = new GameModeFragment();
                selezionaModalitaFragment(modalitaGioco);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //distrugge bind
        if(soundServiceBound){
            unbindService(soundServiceConnection);
            soundServiceBound = false;
        }
    }

    private void selezionaModalitaFragment(Fragment fragment){
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
        if(SFXattivi){ soundService.suonoBottoni(); }
        startActivity(menuAccesso);
        finish();
    }

    public void onClickImpostazioni(View view) {
        if(SFXattivi){ soundService.suonoBottoni(); }
        startActivity(menuImpostazioni);
    }

    //Necessari per il service binding
    private ServiceConnection soundServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SoundEffectService.LocalBinder binder = (SoundEffectService.LocalBinder) service;
            soundService = binder.getService();
            soundServiceBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            soundServiceBound = false;
        }
    };

    @Override
    public void suonoBottoni() {
        soundService.suonoBottoni();
    }
}