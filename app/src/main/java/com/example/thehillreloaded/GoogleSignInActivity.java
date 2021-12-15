package com.example.thehillreloaded;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

@SuppressWarnings("deprecation")
public class GoogleSignInActivity extends AppCompatActivity {
    public static final int RC_SIGN_IN = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Imposta questa activity come non selezionabile
        // tale activity sarà anche non visibile e serve solo per gestire i risultati
        // di onActivityResult, disaccoppiando il GoogleSignInWrapper dalle altre activity
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        // Chiamata all'activity delle google API che gestisce il login tramite Google
        Intent signInIntent =
                GoogleSignInWrapper.getInstance(this).getClientAutenticazioneGoogle().getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Metodo chiamato al ritorno dall'activity lanciata tramite Google API
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Lancia il menu per gli utenti che hanno effettuato l'accesso
       Intent menuUtente = new Intent(this, UserMenuActivity.class);
       startActivity(menuUtente);
    }
}