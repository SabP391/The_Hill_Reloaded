package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class AccessActivity extends AppCompatActivity {
    //costante per l'autenticazione
    private final int RC_SIGN_IN = 1;

    //Creazione dell'intent per lanciare il menu con autenticazione
    Intent menuUtente;
    //Creazione dell'intent per lanciare il menu senza autenticazione
    Intent menuOspite;

    GoogleSignInClient clientAccessoGoogle;
    GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        // Inizializzazione dell'oggetto necessario per il login con google
        // vengono richiesti IdUtente, email e profilo di base
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("945803891284-9buic6b21l19oq9k8lahirv7m5hurge8.apps.googleusercontent.com")
                .build();
        // Client per l'accesso con google costruito a partire dalle opzioni specificate in gso
        clientAccessoGoogle = GoogleSignIn.getClient(this, gso);

        // Creazione degli intent per accedere alle schermate successive
        menuUtente = new Intent(this, UserMenuActivity.class);
        menuOspite = new Intent(this, GuestMenuActivity.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Se l'utente ha già effettuato l'accesso viene reindirizzato al menu
        // per utenti registrati
        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            startActivity(menuUtente);
        }
    }

    public void onClickUtente(View view) { signIn(); }
    public void onClickOspite(View view) {
        startActivity(menuOspite);
    }

    @SuppressWarnings("deprecation")
    private void signIn() {
        Intent signInIntent = clientAccessoGoogle.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

}