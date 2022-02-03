package com.example.thehillreloaded.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.thehillreloaded.R;
import com.example.thehillreloaded.Services.BGMusicService;

public class MainActivity extends AppCompatActivity {
    Intent avviaMusica;

    //variabili per le SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    //boolean per controllare lo stato della musica nelle shared preferences
    boolean statoMusica;
    Intent nuovaActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        avviaMusica = new Intent(this, BGMusicService.class);

        // Inizializzazione dell'animazione del sole
        RotateAnimation animazioneSole = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animazioneSole.setInterpolator(new LinearInterpolator());
        // setRepeatCount è impostato su Animation.INFINITE per ripetere l'animazione in loop
        animazioneSole.setRepeatCount(Animation.INFINITE);
        animazioneSole.setDuration(7000);

        // Inizia l'animazione del sole che ruota
        final ImageView soleRotante = findViewById(R.id.sole_rotante_sp);
        soleRotante.startAnimation(animazioneSole);

        // Fa partire l'activity successiva dopo un tot di millisecondi
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(nuovaActivity);
                finish();
            }
        }, 3000);
    }

    @Override
    protected void onStart() {
        super.onStart();

        pref = getApplicationContext().getSharedPreferences("HillR_pref", MODE_PRIVATE);
        editor = pref.edit();
        /* se nelle shared preferences c'è un valore per la key "Musica_attiva"
        true = la musica deve essere attiva ; false = la musica non deve essere attiva
        il valore viene inserito in "statoMusica" ed usato per chiamare (o no) il service
         */
        statoMusica = pref.getBoolean("Musica_attiva", true);
        if(statoMusica) { startService(avviaMusica); }
        // Creazione dell'intent per avviare l'activity successiva
        // Se un utente è già loggato salta la schermata di accesso
        if(pref.getAll().containsKey("account-utente-loggato")){
            nuovaActivity = new Intent(this, UserMenuActivity.class);
        }
        else{
            nuovaActivity = new Intent(this, AccessActivity.class);
        }
    }

}