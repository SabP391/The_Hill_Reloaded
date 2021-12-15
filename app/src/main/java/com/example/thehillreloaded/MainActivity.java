package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inizializzazione dell'animazione del sole
        RotateAnimation animazioneSole = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animazioneSole.setInterpolator(new LinearInterpolator());
        // setRepeatCount è impostato su Animation.INFINITE per ripetere l'animazione in loop
        animazioneSole.setRepeatCount(Animation.INFINITE);
        animazioneSole.setDuration(7000);

        // Inizia l'animazione del sole che ruota
        final ImageView soleRotante = (ImageView) findViewById(R.id.sole_rotante_sp);
        soleRotante.startAnimation(animazioneSole);

        //Creazione dell'intent per lanciare la schermata successiva
        //BISOGNA CAMBIARE USERMENUACTIVITY CON ACCESSACTIVITY ATTENZIONEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        Intent nuovaActivity = new Intent(this, AccessActivity.class);

        //funzione temporanea che serve a passare alla schermata successiva
        //tappando sul sole
        soleRotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(nuovaActivity);
            }
        });

    }
}