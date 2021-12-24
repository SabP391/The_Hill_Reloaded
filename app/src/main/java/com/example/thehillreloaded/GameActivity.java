package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.thehillreloaded.Game.Game;
import com.example.thehillreloaded.Game.GameManager;


public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ELEMENTI DEL LAYOUT ---------------------------------------------------------------------
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

        // FRAGMENT --------------------------------------------------------------------------------
        //fragment menu in gioco
        LinearLayout menuLayout = new LinearLayout(this);
        FragmentContainerView menuFragment = new FragmentContainerView(this);
        menuFragment.setId(View.generateViewId());
        LinearLayout.LayoutParams pr = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        menuFragment.setLayoutParams(pr);
        menuFragment.setVisibility(View.GONE);
        menuLayout.addView(menuFragment);
        //fragment menu di pausa
        LinearLayout pauseLayout = new LinearLayout(this);
        FragmentContainerView pauseFragment = new FragmentContainerView(this);
        pauseFragment.setId(View.generateViewId());
        LinearLayout.LayoutParams pr1 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        pr1.gravity = Gravity.CENTER;
        pauseFragment.setLayoutParams(pr1);
        pauseFragment.setVisibility(View.GONE);
        pauseLayout.addView(pauseFragment);

        //parametri bottoneMenu
        LinearLayout.LayoutParams pr2 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pr2.gravity = Gravity.BOTTOM;
        pr2.leftMargin = 10;
        bottoneMenu.setImageResource(R.drawable.ic_menu_di_gioco);
        bottoneMenu.setPadding(30, 30, 30, 40);
        bottoneMenu.setBackgroundResource(R.drawable.bottoni_personalizzati);
        bottoneMenu.setLayoutParams(pr2);
        //parametri bottonePausa
        LinearLayout.LayoutParams pr3 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pr3.gravity = Gravity.RIGHT;
        pr3.rightMargin = 10;
        pr3.topMargin = 10;
        bottonePausa.setImageResource(R.drawable.ic_menu_di_pausa);
        bottonePausa.setPadding(40, 30, 40, 30);
        bottonePausa.setBackgroundResource(R.drawable.bottoni_personalizzati);
        bottonePausa.setLayoutParams(pr3);

        // AGGIUNTA ELEMENTI AL LAYOUT -------------------------------------------------------------
        //aggiunta elementi ai layout (linear)
        inGameMenu.addView(bottoneMenu);
        pauseMenu.addView(bottonePausa);
        //aggiunta elementi al layout (frame) -> ordine importante! GAME DEVE ESSERE IL PRIMO!! <-
        frame.addView(game);
        frame.addView(pauseMenu);
        frame.addView(pauseLayout);
        frame.addView(menuLayout);
        frame.addView(inGameMenu);

        //impostazione contentView al FrameLayout (contiene gli altri elementi)
        setContentView(frame);




        // AZIONI DEI BOTTONI ----------------------------------------------------------------------
        bottoneMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DA MODIFICARE LE CONDIZIONI : if (!GameManager.getInstance().isPaused())
                if (menuFragment.getVisibility() == View.GONE){
                    GameManager.getInstance().pause();
                    menuFragment.setVisibility(View.VISIBLE);
                    creaMenuInGame(menuFragment, new InGameMenuFragment());
                } else {
                    menuFragment.setVisibility(View.GONE);
                    GameManager.getInstance().unPause();
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                        getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });
        bottonePausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DA MODIFICARE LE CONDIZIONI : if (!GameManager.getInstance().isPaused())
                if (pauseFragment.getVisibility() == View.GONE){
                    GameManager.getInstance().pause();
                    pauseFragment.setVisibility(View.VISIBLE);
                    creaMenuPausa(pauseFragment, new PauseMenuFragment());
                } else {
                    pauseFragment.setVisibility(View.GONE);
                    GameManager.getInstance().unPause();
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                        getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });
    }

    public void creaMenuInGame(FragmentContainerView container, InGameMenuFragment fragment){
        FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
        fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fmt.replace(container.getId(), fragment, "inGameFrag");
        fmt.addToBackStack("inGameFrag");
        fmt.commit();
    }

    public void creaMenuPausa(FragmentContainerView container, PauseMenuFragment fragment){
        FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
        fmt.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fmt.replace(container.getId(), fragment, "pauseFrag");
        fmt.addToBackStack("pauseFrag");
        fmt.commit();
    }

}