package com.example.thehillreloaded;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class AccessActivity extends AppCompatActivity {
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
        // Se l'utente ha già effettuato l'accesso viene reindirizzato al menu
        // per utenti registrati
        if(autenticazione.getInstance(this).isLogged(this)){
            startActivity(menuUtente);
        }
    }

    public void onClickUtente(View view) { autenticazione.getInstance(this).login(this);}
    public void onClickOspite(View view) {
        startActivity(menuOspite);
    }

}