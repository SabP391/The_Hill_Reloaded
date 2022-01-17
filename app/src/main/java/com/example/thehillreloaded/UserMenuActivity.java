package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

import com.example.thehillreloaded.Services.SoundEffectService;

public class UserMenuActivity extends AppCompatActivity implements GameModeFragment.SoundFX, DifficultyFragment.SoundFX {
    //variabili per service
    SoundEffectService soundService;
    boolean soundServiceBound = false;

    //variabili per le SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    //boolean per controllare lo stato degli effetti sonori nelle shared preferences
    boolean SFXattivi;

    Intent effettiSonori;
    Intent menuImpostazioni;
    Button iniziaPartita;
    Fragment modalitaGiocoF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        menuImpostazioni = new Intent(this, SettingsActivity.class);
        effettiSonori = new Intent(this, SoundEffectService.class);
        iniziaPartita = findViewById(R.id.bottone_inizia_utente);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        //fragment credenziali utente logato
        AuthFragment authFragment = new AuthFragment();
        tx.add(R.id.fragment_auth_container, authFragment);
        tx.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //getSharedPreferences può essere chiamato solo DOPO l'onCreate di un'attività
        pref = getApplicationContext().getSharedPreferences("HillR_pref", MODE_PRIVATE);
        editor = pref.edit();
        SFXattivi = pref.getBoolean("SFX_attivi", true);
        //binding del service per gli effetti sonori
        bindService(effettiSonori, soundServiceConnection, Context.BIND_AUTO_CREATE);

        iniziaPartita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SFXattivi){ soundService.suonoBottoni(); }
                modalitaGiocoF = new GameModeFragment();
                selezionaModalitàFragment(modalitaGiocoF);
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

    private void selezionaModalitàFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.fragment_contanier, fragment)
                .addToBackStack("fragment1")
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStackImmediate();
        else super.onBackPressed();
    }

    // Metodo che avvia la nuova activity per il multiplayer
    public void onClickMultiplayer(View view){
        Intent intent = new Intent(this, MultiplayerActivity.class);
        startActivity(intent);
    }

    public void onClickImpostazioni(View view) {
        if(SFXattivi){ soundService.suonoBottoni(); }
        startActivity(menuImpostazioni);
    }

    public void onClickGioco(View view) {
        if(SFXattivi){ soundService.suonoBottoni(); }
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("IS_NEW_GAME", false);
        startActivity(intent);
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
    public void suonoBottoni() { if(SFXattivi){ soundService.suonoBottoni(); }}

    public void btViewPunteggi(View view) {
        Intent viewPunteggi = new Intent(this, GameStatisticsActivity.class);
        startActivity(viewPunteggi);
    }
}