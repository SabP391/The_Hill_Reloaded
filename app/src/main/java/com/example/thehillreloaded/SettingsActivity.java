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

import com.example.thehillreloaded.Services.BGMusicService;
import com.example.thehillreloaded.Services.SoundEffectService;

public class SettingsActivity extends AppCompatActivity implements VolumeSettingsFragment.SoundFX {
    //variabili per service
    SoundEffectService soundService;
    BGMusicService musicService;
    boolean bgMusicServiceBound = false;
    boolean soundServiceBound = false;
    Intent effettiSonori;
    Intent avviaMusica;
    boolean statoMusica;

    Intent tornaAdAccesso;
    Button impostazioniVolume;
    Fragment impostazioniVolumeF;
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

        impostazioniVolume = findViewById(R.id.bottone_impostazioni_volume);
        impostazioniVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundService.suonoBottoni();
                statoMusica = isServiceRunning(BGMusicService.class);
                impostazioniVolumeF = new VolumeSettingsFragment(statoMusica);
                impostazioniVolumeFragment(impostazioniVolumeF);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        effettiSonori = new Intent(this, SoundEffectService.class);
        avviaMusica = new Intent(this, BGMusicService.class);
        bindService(avviaMusica, bgMusicServiceConnection, Context.BIND_AUTO_CREATE);
        bindService(effettiSonori, soundServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //distrugge bind
        if(soundServiceBound){
            unbindService(soundServiceConnection);
            soundServiceBound = false;
        }
        if(bgMusicServiceBound) {
            unbindService(bgMusicServiceConnection);
            bgMusicServiceBound = false;
        }
    }

    public void impostazioniVolumeFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.fragment_impostazioni_volume, fragment)
                .addToBackStack("fragment_audio")
                .commit();
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

    private ServiceConnection bgMusicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BGMusicService.LocalBinder binder = (BGMusicService.LocalBinder) service;
            musicService = binder.getService();
            bgMusicServiceBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            bgMusicServiceBound = false;
        }
    };


    //metodi dell'interface
    @Override
    public void suonoBottoni() {
        soundService.suonoBottoni();
    }

    @Override
    public void togliMusica() {
        musicService.stopMusic();
        unbindService(bgMusicServiceConnection);
    }

    @Override
    public void mettiMusica() {
        musicService.startMusic();
        bindService(avviaMusica, bgMusicServiceConnection, Context.BIND_AUTO_CREATE);
    }
/*
    @Override
    public void togliEffetti() {
        unbindService(soundServiceConnection);
    }

    @Override
    public void mettiEffetti() {
        bindService(effettiSonori, soundServiceConnection, Context.BIND_AUTO_CREATE);
    }
 */

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