package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AccessActivity extends AppCompatActivity {

    //Creazione dell'intent per lanciare il menu con autenticazione
    Intent menuUtente;
    //Creazione dell'intent per lanciare il menu senza autenticazione
    Intent menuOspite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        menuUtente = new Intent(this, UserMenuActivity.class);
        menuOspite = new Intent(this, GuestMenuActivity.class);
    }

    public void onClickUtente(View view) { startActivity(menuUtente); }
    public void onClickOspite(View view) {
        startActivity(menuOspite);
    }
}