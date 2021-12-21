package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.thehillreloaded.Services.BGMusicService;
import com.example.thehillreloaded.Services.SoundEffectService;

public class SettingsActivity extends AppCompatActivity{
    //variabili per service
    SoundEffectService soundService;
    boolean soundServiceBound = false;
    Intent effettiSonori;
    Intent avviaMusica;
    boolean statoMusica;

    private Switch musicaBottone;
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
        musicaBottone = (Switch) findViewById(R.id.switch_musica);
        statoMusica = isServiceRunning(BGMusicService.class);
        switchInitState();
    }

    @Override
    protected void onStart() {
        super.onStart();

        effettiSonori = new Intent(this, SoundEffectService.class);
        avviaMusica = new Intent(this, BGMusicService.class);
        bindService(effettiSonori, soundServiceConnection, Context.BIND_AUTO_CREATE);
        statoMusica = isServiceRunning(BGMusicService.class);
        switchInitState();
        musicaBottone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (musicaBottone.isChecked()) {
                    //musicService.startMusic();
                    startService(avviaMusica);
                    statoMusica = true;
                } else if (!musicaBottone.isChecked()) {
                    //musicService.stopMusic();
                    stopService(avviaMusica);
                    statoMusica = false;
                }
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

    //Metodi per il controllo dell'audio
    public void switchInitState(){
        musicaBottone.setChecked(statoMusica);
    }


    public void OnClickTornaIndietro(View view){
        soundService.suonoBottoni();
        finish();
    }

    public void onClickEsci(View view) {
        soundService.suonoBottoni();
        autenticazione.getInstance(this).logout();
        startActivity(tornaAdAccesso);
    }

    //Binding Service per gli effetti sonori
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

    //metodo per controllare se il service sta andando o no
    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}