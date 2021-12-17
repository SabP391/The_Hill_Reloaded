package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

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
                impostazioniVolumeF = new VolumeSettingsFragment();
                impostazioniVolumeFragment(impostazioniVolumeF);
            }
        });
    }

    public void impostazioniVolumeFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.fragment_impostazioni_volume, fragment)
                .addToBackStack("fragment_audio")
                .commit();
    }

    public void OnClickTornaIndietro(View view){ finish(); }
    public void onClickEsci(View view) {
        autenticazione.getInstance(this).logout();
        startActivity(tornaAdAccesso);
    }
}