package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.example.thehillreloaded.Services.SoundEffectService;

public class CreditsActivity extends AppCompatActivity {
    //variabili per service
    SoundEffectService soundService;
    boolean soundServiceBound = false;
    //variabili per le SharedPreferences
    SharedPreferences pref;

    Intent effettiSonori;
    Boolean SFXattivi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        effettiSonori = new Intent(this, SoundEffectService.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        pref = getApplicationContext().getSharedPreferences("HillR_pref", MODE_PRIVATE);
        bindService(effettiSonori, soundServiceConnection, Context.BIND_AUTO_CREATE);
        SFXattivi = pref.getBoolean("SFX_attivi", true);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(soundServiceBound){
            unbindService(soundServiceConnection);
            soundServiceBound = false;
        }
    }

    public void OnClickRitorno(View view){
        if(SFXattivi){ soundService.suonoBottoni(); }
        finish();
    }

    //Binding Service
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