package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.thehillreloaded.Game.Game;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //DA TOGLIERE
        Intent uh = new Intent(this, UserMenuActivity.class);


        //dichiarazione layout generale
        FrameLayout frame = new FrameLayout(this);
        //dichiarazione layout degli elementi
        LinearLayout inGameMenu = new LinearLayout(this);
        LinearLayout pauseMenu = new LinearLayout(this);
        pauseMenu.setOrientation(LinearLayout.VERTICAL);
        //inizializza la classe di gioco
        Game game = new Game(this);

        //dichiarazione elementi (bottoni)
        ImageButton bottoneMenu = new ImageButton(this);
        ImageButton bottonePausa = new ImageButton(this);

        //parametri bottoneMenu
        LinearLayout.LayoutParams pr = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pr.gravity = Gravity.BOTTOM;
        pr.leftMargin = 10;
        bottoneMenu.setImageResource(R.drawable.ic_menu_di_gioco);
        bottoneMenu.setPadding(30, 30, 30, 40);
        bottoneMenu.setBackgroundResource(R.drawable.bottoni_personalizzati);
        bottoneMenu.setLayoutParams(pr);
        //parametri bottonePausa
        LinearLayout.LayoutParams pr2 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pr2.gravity = Gravity.RIGHT;
        pr2.rightMargin = 10;
        pr2.topMargin = 10;
        bottonePausa.setImageResource(R.drawable.ic_menu_di_pausa);
        bottonePausa.setPadding(40, 30, 40, 30);
        bottonePausa.setBackgroundResource(R.drawable.bottoni_personalizzati);
        bottonePausa.setLayoutParams(pr2);

        //aggiunta elementi ai layout (linear)
        inGameMenu.addView(bottoneMenu);
        pauseMenu.addView(bottonePausa);
        //aggiunta elementi al layout (frame) -> GAME DEVE ESSERE IL PRIMO!! <-
        frame.addView(game);
        frame.addView(inGameMenu);
        frame.addView(pauseMenu);


        //impostazione contentView al FrameLayout (contiene gli altri elementi)
        setContentView(frame);

        bottoneMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DA MODIFICARE
                startActivity(uh);
            }
        });
    }
}