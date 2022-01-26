package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.thehillreloaded.Services.BGMusicService;
import com.example.thehillreloaded.Services.SoundEffectService;

public class SettingsActivity extends AppCompatActivity{
    //variabili per service
    SoundEffectService soundService;
    boolean soundServiceBound = false;

    //variabili per le SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Intent tornaAdAccesso;
    Intent effettiSonori;
    Intent avviaMusica;
    private Switch musicaBottone;
    boolean statoMusica;
    private Switch effettiBottone;
    boolean SFXattivi;
    AuthFragment authFragment = new AuthFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        effettiSonori = new Intent(this, SoundEffectService.class);
        avviaMusica = new Intent(this, BGMusicService.class);
        effettiBottone = (Switch) findViewById(R.id.switch_effetti);
        musicaBottone = (Switch) findViewById(R.id.switch_musica);

        tornaAdAccesso = new Intent(this, AccessActivity.class);
        //if(!autenticazione.getInstance(this).isLogged(this)){
        /*if(!pref.getAll().containsKey("account-utente-loggato")){
            View bottoneImpostazioni = findViewById(R.id.bottone_esci);
            bottoneImpostazioni.setVisibility(View.GONE);
        }*/

    }

    @Override
    protected void onStart() {
        super.onStart();

        //getSharedPreferences può essere chiamato solo DOPO l'onCreate di un'attività
        pref = getApplicationContext().getSharedPreferences("HillR_pref", MODE_PRIVATE);
        editor = pref.edit();
        //binding del service per gli effetti sonori
        bindService(effettiSonori, soundServiceConnection, Context.BIND_AUTO_CREATE);
        //set e controllo degli switch per musica ed effetti sonori
        SFXattivi = pref.getBoolean("SFX_attivi", true);
        statoMusica = isServiceRunning(BGMusicService.class);
        musicaBottone.setChecked(statoMusica);
        musicaBottone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (musicaBottone.isChecked()) {
                    //attiva la musica in background e salva la scelta nelle SharedPreferences
                    startService(avviaMusica);
                    statoMusica = true;
                    editor.putBoolean("Musica_attiva", true);
                    editor.apply();
                } else if (!musicaBottone.isChecked()) {
                    //ferma la musica in background e salva la scelta nelle SharedPreferences
                    stopService(avviaMusica);
                    statoMusica = false;
                    editor.putBoolean("Musica_attiva", false);
                    editor.apply();
                }
            }
        });
        effettiBottone.setChecked(SFXattivi);
        effettiBottone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (effettiBottone.isChecked()) {
                    //attiva gli effetti sonori e salva la scelta nelle SharedPreferences
                    editor.putBoolean("SFX_attivi", true);
                    editor.apply();
                    SFXattivi = pref.getBoolean("SFX_attivi", true);
                } else if (!effettiBottone.isChecked()) {
                    //disattiva gli effetti sonori e salva la scelta nelle SharedPreferences
                    editor.putBoolean("SFX_attivi", false);
                    editor.apply();
                    SFXattivi = pref.getBoolean("SFX_attivi", true);
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

    public void OnClickTornaIndietro(View view){
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