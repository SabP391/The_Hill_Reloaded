package com.example.thehillreloaded.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.example.thehillreloaded.R;
import com.example.thehillreloaded.Services.SoundEffectService;

public class GameWonActivity extends AppCompatActivity {
    //variabili per service
    SoundEffectService soundService;
    boolean soundServiceBound = false;

    //variabili per le SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    //boolean per controllare lo stato degli effetti sonori nelle shared preferences
    boolean SFXattivi;

    Intent mainMenu;
    Intent effettiSonori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_won);
        effettiSonori = new Intent(this, SoundEffectService.class);
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
        if(pref.getAll().containsKey("account-utente-loggato")){
            mainMenu = new Intent(this, UserMenuActivity.class);
        }
        else{
            mainMenu = new Intent(this, GuestMenuActivity.class);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(soundServiceBound){
            unbindService(soundServiceConnection);
            soundServiceBound = false;
        }
    }

    public void backToMainMenu(View view){
        if(SFXattivi){ soundService.suonoBottoni(); }
        mainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainMenu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainMenu);
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