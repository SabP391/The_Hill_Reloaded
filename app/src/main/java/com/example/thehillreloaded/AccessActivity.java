package com.example.thehillreloaded;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
            finish();
        }
    }

    public void ospiteFragment(View view){
        continuaComeOspiteFragment(new NoLoginAccessFragment());
    }

    private void continuaComeOspiteFragment(Fragment fragment){
        Button google = findViewById(R.id.bottone_google);
        google.setVisibility(View.INVISIBLE);
        Button ospite = findViewById(R.id.bottone_ospite);
        ospite.setVisibility(View.INVISIBLE);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                .add(R.id.fragment_accesso_ospite, fragment)
                .addToBackStack("fragment1")
                .commit();
    }

    public void chiudiFragment(View view){
        Button google = findViewById(R.id.bottone_google);
        google.setVisibility(View.VISIBLE);
        Button ospite = findViewById(R.id.bottone_ospite);
        ospite.setVisibility(View.VISIBLE);
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 0){
            Button google = findViewById(R.id.bottone_google);
            google.setVisibility(View.VISIBLE);
            Button ospite = findViewById(R.id.bottone_ospite);
            ospite.setVisibility(View.VISIBLE);
            getSupportFragmentManager().popBackStackImmediate();
        }
        else super.onBackPressed();
    }

    public void onClickUtente(View view) {
        autenticazione.getInstance(this).login(this);
    }

    public void onClickOspite(View view) {
        startActivity(menuOspite);
        finish();
    }

}