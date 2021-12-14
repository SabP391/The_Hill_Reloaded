package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserMenuActivity extends AppCompatActivity {

    Intent menuImpostazioni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        menuImpostazioni = new Intent(this, SettingsActivity.class);
    }

    public void onClickNuovaPartita(View view) {
        selezionaModalitàFragment(new GameModeFragment());
    }

    private void selezionaModalitàFragment(Fragment fragment){
        //Crea il fragmentmanager
        FragmentManager fragmentManager = getFragmentManager();
        //Crea il fragmenttransaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    public void onClickImpostazioni(View view) { startActivity(menuImpostazioni); }
}