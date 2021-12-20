package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

import com.example.thehillreloaded.Services.BGMusicService;
import com.example.thehillreloaded.Services.SoundEffectService;

public class AccessActivity extends AppCompatActivity {
    //variabili per service
    SoundEffectService soundService;
    boolean soundServiceBound = false;
    BGMusicService musicService;
    Intent effettiSonori;
    Intent avviaMusica;
    boolean musicaAttiva = false;

    //costante per l'autenticazione
    GoogleSignInWrapper autenticazione;

    //Creazione dell'intent per lanciare il menu con autenticazione
    Intent menuUtente;
    //Creazione dell'intent per lanciare il menu senza autenticazione
    Intent menuOspite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        // Creazione degli intent per accedere alle schermate successive
        menuUtente = new Intent(this, UserMenuActivity.class);
        menuOspite = new Intent(this, GuestMenuActivity.class);

    }

    @Override
    public void onStart() {
        super.onStart();
        effettiSonori = new Intent(this, SoundEffectService.class);
        avviaMusica = new Intent(this, BGMusicService.class);
        startService(avviaMusica);
        bindService(effettiSonori, soundServiceConnection, Context.BIND_AUTO_CREATE);
        // Se l'utente ha già effettuato l'accesso viene reindirizzato al menu
        // per utenti registrati
        if(autenticazione.getInstance(this).isLogged(this)){
            startActivity(menuUtente);
            finish();
        }
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

    public void ospiteFragment(View view){
        soundService.suonoBottoni();
        continuaComeOspiteFragment(new NoLoginAccessFragment());
    }

    private void continuaComeOspiteFragment(Fragment fragment){
        Button google = findViewById(R.id.bottone_google);
        google.setVisibility(View.INVISIBLE);
        Button ospite = findViewById(R.id.bottone_ospite);
        ospite.setVisibility(View.INVISIBLE);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                .add(R.id.fragment_accesso_ospite, fragment)
                .addToBackStack("fragment1")
                .commit();
    }

    public void chiudiFragment(View view){
        Button google = findViewById(R.id.bottone_google);
        google.setVisibility(View.VISIBLE);
        Button ospite = findViewById(R.id.bottone_ospite);
        ospite.setVisibility(View.VISIBLE);
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            Button google = findViewById(R.id.bottone_google);
            google.setVisibility(View.VISIBLE);
            Button ospite = findViewById(R.id.bottone_ospite);
            ospite.setVisibility(View.VISIBLE);
            getSupportFragmentManager().popBackStackImmediate();
        }
        else super.onBackPressed();
    }

    public void onClickUtente(View view) {
        soundService.suonoBottoni();
        autenticazione.getInstance(this).login(this);
    }

    public void onClickOspite(View view) {
        soundService.suonoBottoni();
        startActivity(menuOspite);
        finish();
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

}