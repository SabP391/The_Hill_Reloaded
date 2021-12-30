package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.thehillreloaded.Game.Difficulty;
import com.example.thehillreloaded.Game.Game;
import com.example.thehillreloaded.Game.GameManager;
import com.example.thehillreloaded.Game.GameMode;
import com.example.thehillreloaded.Game.RecycleUnitsManager;
import com.example.thehillreloaded.Game.TileMap;
import com.example.thehillreloaded.Services.SoundEffectService;


public class GameActivity extends AppCompatActivity implements ClassicInGameMenuFragment.SoundFX{
    private GameMode gameMode;
    private Difficulty difficulty;
    private TileMap map;

    //variabili per service
    SoundEffectService soundService;
    boolean soundServiceBound = false;
    //variabili per le SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    //boolean per controllare lo stato degli effetti sonori nelle shared preferences
    boolean SFXattivi;

    int menuFragID;
    int menuBottID;

    Intent effettiSonori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        effettiSonori = new Intent(this, SoundEffectService.class);
        map = new TileMap(this);

        // Inizializzazione del GameManager --------------------------------------------------------
        // Recupero della modalità di gioco e della
        // difficoltà dall'intent lanciato nel menu
        Intent intent = getIntent();
        Bundle initInformation = intent.getExtras();
        int mode = initInformation.getInt("GAME_MODE");
        int diff = initInformation.getInt("GAME_DIFF");

        // Assegnazione della modalità di gioco
        switch(mode){
            case 24:
                gameMode = GameMode.CLASSIC;
                break;
            case 42:
                gameMode = GameMode.RELOADED;
                break;
            default:
                break;
        }

        // Assegnazione della difficoltà di gioco
        switch(diff){
            case 1:
                difficulty = Difficulty.EASY;
                break;
            case 2:
                difficulty = Difficulty.NORMAL;
                break;
            case 3:
                difficulty = Difficulty.HARD;
                break;
            default:
                break;
        }
        // Inizializzazione vera e propria del GameManager
        GameManager.getInstance().initInstance(gameMode, difficulty, this, map);

        // Inizializzazione della classe game e del
        // manager delle centrali ------------------------------------------------------------------
        RecycleUnitsManager.getInstance().initInstance(this, map);
        Game game = new Game(this, map);




        // ELEMENTI DEL LAYOUT ---------------------------------------------------------------------
        //dichiarazione layout generale
        FrameLayout frame = new FrameLayout(this);
        //dichiarazione layout degli elementi
        LinearLayout inGameMenu = new LinearLayout(this);
        LinearLayout pauseMenu = new LinearLayout(this);
        pauseMenu.setOrientation(LinearLayout.VERTICAL);
        //dichiarazione elementi (bottoni)
        ImageButton bottoneMenu = new ImageButton(this);
        ImageButton bottonePausa = new ImageButton(this);

        // FRAGMENT --------------------------------------------------------------------------------
        //fragment menu in gioco
        LinearLayout menuLayout = new LinearLayout(this);
        FragmentContainerView menuFragment = new FragmentContainerView(this);
        menuFragment.setId(View.generateViewId());
        menuFragID = menuFragment.getId();
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
        //fragment menu gestione centrali di riciclo
        LinearLayout manageLayout = new LinearLayout(this);
        FragmentContainerView manageFragment = new FragmentContainerView(this);
        manageFragment.setId(View.generateViewId());
        LinearLayout.LayoutParams pr4 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        pr4.gravity = Gravity.BOTTOM;
        manageFragment.setLayoutParams(pr4);
        manageFragment.setVisibility(View.GONE);
        manageLayout.addView(manageFragment);

        // PARAMETRI BOTTONI -----------------------------------------------------------------------
        //parametri bottoneMenu
        LinearLayout.LayoutParams pr2 = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pr2.gravity = Gravity.BOTTOM;
        pr2.leftMargin = 10;
        bottoneMenu.setImageResource(R.drawable.ic_menu_di_gioco);
        bottoneMenu.setPadding(30, 30, 30, 40);
        bottoneMenu.setBackgroundResource(R.drawable.bottoni_personalizzati);
        bottoneMenu.setLayoutParams(pr2);
        bottoneMenu.setId(View.generateViewId());
        menuBottID = bottoneMenu.getId();
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
        frame.addView(menuLayout);
        frame.addView(inGameMenu);
        frame.addView(pauseLayout);

        //impostazione contentView al FrameLayout (contiene gli altri elementi)
        setContentView(frame);


        // AZIONI DEI BOTTONI ----------------------------------------------------------------------
        bottoneMenu.setOnClickListener(new View.OnClickListener() {
            //gameMode == GameMode.CLASSIC oppure gameMode == GameMode.RELOADED
            @Override
            public void onClick(View v) {
                if(SFXattivi){ soundService.suonoBottoni(); }
                if (!GameManager.getInstance().isPaused()){
                    if(gameMode == GameMode.CLASSIC) {
                        GameManager.getInstance().pause();
                        menuFragment.setVisibility(View.VISIBLE);
                        creaMenuInGameClassic(menuFragment, new ClassicInGameMenuFragment());
                    } else if (gameMode == GameMode.RELOADED) {
                        GameManager.getInstance().pause();
                        menuFragment.setVisibility(View.VISIBLE);
                        creaMenuInGameReloaded(menuFragment, new ReloadedInGameMenuFragment());
                    }
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
                if(SFXattivi){ soundService.suonoBottoni(); }
                if (!GameManager.getInstance().isPaused()){
                    GameManager.getInstance().pause();
                    pauseFragment.setVisibility(View.VISIBLE);
                    creaMenuPausa(pauseFragment, new PauseMenuFragment());
                } else {
                    if(pauseFragment.getVisibility() == View.GONE) {
                        //gioco in pausa e fragment di pausa non attivo = chiude altri fragment e apre menu di pausa
                        pauseFragment.setVisibility(View.VISIBLE);
                        creaMenuPausa(pauseFragment, new PauseMenuFragment());
                    } else if(pauseFragment.getVisibility() == View.VISIBLE) {
                        //gioco in pausa e fragment attivo = chiude fragment e toglie pausa
                        pauseFragment.setVisibility(View.GONE);
                        getSupportFragmentManager().popBackStackImmediate();
                        if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                            GameManager.getInstance().unPause();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //getSharedPreferences può essere chiamato solo DOPO l'onCreate di un'attività
        pref = getApplicationContext().getSharedPreferences("HillR_pref", MODE_PRIVATE);
        editor = pref.edit();
        SFXattivi = pref.getBoolean("SFX_attivi", true);
        //binding del service per gli effetti sonori
        bindService(effettiSonori, soundServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //distrugge bind
        if(soundServiceBound){
            unbindService(soundServiceConnection);
            soundServiceBound = false;
        }
    }

    public void creaMenuInGameClassic(FragmentContainerView container, ClassicInGameMenuFragment fragment){
        FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
        fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fmt.replace(container.getId(), fragment, "c_inGameFrag");
        fmt.addToBackStack("c_inGameFrag");
        fmt.commit();
    }

    public void creaMenuInGameReloaded(FragmentContainerView container, ReloadedInGameMenuFragment fragment){
        FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
        fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fmt.replace(container.getId(), fragment, "r_inGameFrag");
        fmt.addToBackStack("r_inGameFrag");
        fmt.commit();
    }

    public void creaMenuPausa(FragmentContainerView container, PauseMenuFragment fragment){
        FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
        fmt.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fmt.replace(container.getId(), fragment, "pauseFrag");
        fmt.addToBackStack("pauseFrag");
        fmt.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
            GameManager.getInstance().unPause();
            if(findViewById(menuBottID).getVisibility() == View.GONE){
                findViewById(menuBottID).setVisibility(View.VISIBLE);
            }
        } else super.onBackPressed();
    }

    //Necessari per il service binding
    private ServiceConnection soundServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SoundEffectService.LocalBinder binder = (SoundEffectService.LocalBinder) service;
            soundService = binder.getService();
            soundServiceBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            soundServiceBound = false;
        }
    };

    @Override
    public void suonoBottoni() {  if(SFXattivi){ soundService.suonoBottoni(); } }

    //Metodi OnClick per le diverse centrali
    public void onClickMenuVetro(View view){
        if(SFXattivi){ soundService.suonoBottoni(); }
        findViewById(menuBottID).setVisibility(View.GONE);
        getSupportFragmentManager().popBackStackImmediate();
        FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
        fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fmt.replace(menuFragID, new GlassUnitFragment());
        fmt.addToBackStack("vetro");
        fmt.commit();
    }

    public void onClickMenuCarta(View view){
        if(SFXattivi){ soundService.suonoBottoni(); }
        if (RecycleUnitsManager.getInstance().isPaperUnitUnlocked()) {
            findViewById(menuBottID).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStackImmediate();
            FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
            fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
            fmt.replace(menuFragID, new PaperUnitFragment());
            fmt.addToBackStack("carta");
            fmt.commit();
        } else {
            if (RecycleUnitsManager.getInstance().unlockPaperUnit()){
                getSupportFragmentManager().popBackStackImmediate();
                GameManager.getInstance().unPause();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.sunny_non_sufficienti, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void onClickMenuPlastica(View view){
        if(SFXattivi){ soundService.suonoBottoni(); }
        if (RecycleUnitsManager.getInstance().isPlasticUnitUnlocked()) {
            findViewById(menuBottID).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStackImmediate();
            FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
            fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
            fmt.replace(menuFragID, new PlasticUnitFragment());
            fmt.addToBackStack("plastica");
            fmt.commit();
        } else {
            if (RecycleUnitsManager.getInstance().unlockPlasticUnit()) {
                getSupportFragmentManager().popBackStackImmediate();
                GameManager.getInstance().unPause();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.sunny_non_sufficienti, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void onClickMenuAlluminio(View view){
        if(SFXattivi){ soundService.suonoBottoni(); }
        if (RecycleUnitsManager.getInstance().isAluminiumUnitUnlocked()) {
            findViewById(menuBottID).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStackImmediate();
            FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
            fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
            fmt.replace(menuFragID, new AluminiumUnitFragment());
            fmt.addToBackStack("alluminio");
            fmt.commit();
        } else {
            if (RecycleUnitsManager.getInstance().unlockAluminiumUnit()) {
                getSupportFragmentManager().popBackStackImmediate();
                GameManager.getInstance().unPause();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.sunny_non_sufficienti, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void onClickMenuEwaste(View view){
        if(SFXattivi){ soundService.suonoBottoni(); }
        if (RecycleUnitsManager.getInstance().isEwasteUnitUnlocked()) {
            findViewById(menuBottID).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStackImmediate();
            FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
            fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
            fmt.replace(menuFragID, new EwasteUnitFragment());
            fmt.addToBackStack("ewaste");
            fmt.commit();
        } else {
            if (RecycleUnitsManager.getInstance().unlockEWasteUnit()) {
                getSupportFragmentManager().popBackStackImmediate();
                GameManager.getInstance().unPause();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.sunny_non_sufficienti, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void onClickMenuAcciaio(View view){
        if(SFXattivi){ soundService.suonoBottoni(); }
        if (RecycleUnitsManager.getInstance().isSteelUnitUnlocked()) {
            findViewById(menuBottID).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStackImmediate();
            FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
            fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
            fmt.replace(menuFragID, new MetalUnitFragment());
            fmt.addToBackStack("acciaio");
            fmt.commit();
        } else {
            if (RecycleUnitsManager.getInstance().unlockSteelUnit()) {
                getSupportFragmentManager().popBackStackImmediate();
                GameManager.getInstance().unPause();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.sunny_non_sufficienti, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void onClickMenuOrganico(View view){
        if(SFXattivi){ soundService.suonoBottoni(); }
        if (RecycleUnitsManager.getInstance().isCompostUnlocked()) {
            findViewById(menuBottID).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStackImmediate();
            FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
            fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
            fmt.replace(menuFragID, new OrganicUnitFragment());
            fmt.addToBackStack("organico");
            fmt.commit();
        } else {
            if (RecycleUnitsManager.getInstance().unlockCompostUnit()) {
                getSupportFragmentManager().popBackStackImmediate();
                GameManager.getInstance().unPause();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.sunny_non_sufficienti, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void onClickEsciDaMenu(View view){
        if(SFXattivi){ soundService.suonoBottoni(); }
        findViewById(menuBottID).setVisibility(View.VISIBLE);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
            GameManager.getInstance().unPause();
        }
    }
}