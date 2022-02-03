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

import com.example.thehillreloaded.Fragments.NoLoginAccessFragment;
import com.example.thehillreloaded.R;
import com.example.thehillreloaded.Services.SoundEffectService;
import com.google.gson.Gson;

public class AccessActivity extends AppCompatActivity {
    //variabili per service
    SoundEffectService soundService;
    boolean soundServiceBound = false;
    Intent effettiSonori;
    Intent intentLog;

    //variabili per le SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    //oggetto di tipo Gson che verrà salvato nelle Shared Preferences
    Gson gson = new Gson();
    //boolean per controllare lo stato degli effetti sonori nelle shared preferences
    boolean SFXattivi;

    //Creazione dell'intent per lanciare il menu con autenticazione
    Intent menuUtente;
    //Creazione dell'intent per lanciare il menu senza autenticazione
    Intent menuOspite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        effettiSonori = new Intent(this, SoundEffectService.class);
        intentLog= new Intent(this, LogInActivity.class);
        // Creazione degli intent per accedere alle schermate successive
        menuUtente = new Intent(this, UserMenuActivity.class);
        menuOspite = new Intent(this, GuestMenuActivity.class);
    }

    @Override
    public void onStart() {
        super.onStart();

        pref = getApplicationContext().getSharedPreferences("HillR_pref", MODE_PRIVATE);
        editor = pref.edit();
        SFXattivi = pref.getBoolean("SFX_attivi", true);

        //binding del service per gli effetti sonori
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
    }

    public void ospiteFragment(View view){
        if(SFXattivi){ soundService.suonoBottoni(); }
        continuaComeOspiteFragment(new NoLoginAccessFragment());
    }

    private void continuaComeOspiteFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                .add(R.id.fragment_accesso_ospite, fragment)
                .addToBackStack("fragment1")
                .commit();
    }

    public void chiudiFragment(View view){
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStackImmediate();
        }
        else super.onBackPressed();
    }

    public void onClickOspite(View view) {
        if(SFXattivi){ soundService.suonoBottoni(); }
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

    //accesso activity Login
    public void btnAccessoClick(View view) {
        Intent intentLog = new Intent(this, LogInActivity.class);
        startActivity(intentLog);
    }
}