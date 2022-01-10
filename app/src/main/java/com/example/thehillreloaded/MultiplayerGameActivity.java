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
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.thehillreloaded.Game.Difficulty;
import com.example.thehillreloaded.Game.Game;
import com.example.thehillreloaded.Game.GameItemsManager;
import com.example.thehillreloaded.Game.GameManager;
import com.example.thehillreloaded.Game.GameMode;
import com.example.thehillreloaded.Game.QuestManager;
import com.example.thehillreloaded.Game.RecycleUnitsManager;
import com.example.thehillreloaded.Game.TileMap;
import com.example.thehillreloaded.Services.SoundEffectService;

public class MultiplayerGameActivity extends AppCompatActivity implements QuestManager.SoundFX, GlassUnitFragment.SoundFX, PaperUnitFragment.SoundFX,
        PlasticUnitFragment.SoundFX, EwasteUnitFragment.SoundFX, MetalUnitFragment.SoundFX, AluminiumUnitFragment.SoundFX,
        OrganicUnitFragment.SoundFX, RecycleUnitsManager.SoundFx, Game.SoundFx{

    //variabili per il gioco
    private TileMap map;

    //variabili per service
    SoundEffectService soundService;
    boolean soundServiceBound = false;
    //variabili per le SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    //boolean per controllare lo stato degli effetti sonori nelle shared preferences
    boolean SFXattivi;

    Intent effettiSonori;
    int menuFragID;
    int menuBottID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        effettiSonori = new Intent(this, SoundEffectService.class);

        //inizializzazione del gioco ---------------------------------------------------------------
        map = new TileMap(this);
        GameManager.getInstance().destroy();
        QuestManager.getInstance().destroy();
        RecycleUnitsManager.getInstance().destroy();
        GameItemsManager.getInstance().destroy();
        GameManager.getInstance().initInstance(GameMode.RELOADED, Difficulty.HARD, this, map);
        GameManager.getInstance().setMultiplayerGame(true);
        GameItemsManager.getInstance().initInstance(this, map);
        RecycleUnitsManager.getInstance().initInstance(this, map);

        // ELEMENTI DEL LAYOUT ---------------------------------------------------------------------
        //dichiarazione layout generale
        FrameLayout frame = new FrameLayout(this);
        //dichiarazione layout degli elementi
        LinearLayout inGameMenu = new LinearLayout(this);
        //dichiarazione elementi (bottoni)
        ImageButton bottoneMenu = new ImageButton(this);

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

        // AGGIUNTA ELEMENTI AL LAYOUT -------------------------------------------------------------
        //aggiunta elementi ai layout (linear)
        inGameMenu.addView(bottoneMenu);
        //aggiunta elementi al layout (frame) -> ordine importante! GAME DEVE ESSERE IL PRIMO!! <-
        //frame.addView(game);
        frame.addView(menuLayout);
        frame.addView(inGameMenu);

        //impostazione contentView al FrameLayout (contiene gli altri elementi)
        setContentView(frame);

        // AZIONI DEI BOTTONI ----------------------------------------------------------------------
        bottoneMenu.setOnClickListener(new View.OnClickListener() {
            //gameMode == GameMode.CLASSIC oppure gameMode == GameMode.RELOADED
            @Override
            public void onClick(View v) {
                if(SFXattivi){ soundService.suonoBottoni(); }
                if (!GameManager.getInstance().isPaused()){
                    GameManager.getInstance().pause();
                    menuFragment.setVisibility(View.VISIBLE);
                    creaMenuInGame(menuFragment, new ReloadedInGameMenuFragment());
                } else {
                    menuFragment.setVisibility(View.GONE);
                    GameManager.getInstance().unPause();
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0)
                        getSupportFragmentManager().popBackStackImmediate();
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

    public void creaMenuInGame(FragmentContainerView container, ReloadedInGameMenuFragment fragment){
        FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
        fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
        fmt.replace(container.getId(), fragment, "r_inGameFrag");
        fmt.addToBackStack("r_inGameFrag");
        fmt.commit();
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
        if (RecycleUnitsManager.getInstance().isPaperUnitUnlocked()) {
            if(SFXattivi){ soundService.suonoBottoni(); }
            findViewById(menuBottID).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStackImmediate();
            FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
            fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
            fmt.replace(menuFragID, new PaperUnitFragment());
            fmt.addToBackStack("carta");
            fmt.commit();
        } else {
            if (RecycleUnitsManager.getInstance().unlockPaperUnit()){
                if(SFXattivi){ soundService.suonoCostruzioneUpgrade(); }
                getSupportFragmentManager().popBackStackImmediate();
                GameManager.getInstance().unPause();
            } else {
                if(SFXattivi){ soundService.suonoBottoni(); }
                Toast toast = Toast.makeText(getApplicationContext(), R.string.sunny_non_sufficienti, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void onClickMenuPlastica(View view){
        if (RecycleUnitsManager.getInstance().isPlasticUnitUnlocked()) {
            if(SFXattivi){ soundService.suonoBottoni(); }
            findViewById(menuBottID).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStackImmediate();
            FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
            fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
            fmt.replace(menuFragID, new PlasticUnitFragment());
            fmt.addToBackStack("plastica");
            fmt.commit();
        } else {
            if (RecycleUnitsManager.getInstance().unlockPlasticUnit()) {
                if(SFXattivi){ soundService.suonoCostruzioneUpgrade(); }
                getSupportFragmentManager().popBackStackImmediate();
                GameManager.getInstance().unPause();
            } else {
                if(SFXattivi){ soundService.suonoBottoni(); }
                Toast toast = Toast.makeText(getApplicationContext(), R.string.sunny_non_sufficienti, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void onClickMenuAlluminio(View view){
        if (RecycleUnitsManager.getInstance().isAluminiumUnitUnlocked()) {
            if(SFXattivi){ soundService.suonoBottoni(); }
            findViewById(menuBottID).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStackImmediate();
            FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
            fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
            fmt.replace(menuFragID, new AluminiumUnitFragment());
            fmt.addToBackStack("alluminio");
            fmt.commit();
        } else {
            if (RecycleUnitsManager.getInstance().unlockAluminiumUnit()) {
                if(SFXattivi){ soundService.suonoCostruzioneUpgrade(); }
                getSupportFragmentManager().popBackStackImmediate();
                GameManager.getInstance().unPause();
            } else {
                if(SFXattivi){ soundService.suonoBottoni(); }
                Toast toast = Toast.makeText(getApplicationContext(), R.string.sunny_non_sufficienti, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void onClickMenuEwaste(View view){
        if (RecycleUnitsManager.getInstance().isEwasteUnitUnlocked()) {
            if(SFXattivi){ soundService.suonoBottoni(); }
            findViewById(menuBottID).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStackImmediate();
            FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
            fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
            fmt.replace(menuFragID, new EwasteUnitFragment());
            fmt.addToBackStack("ewaste");
            fmt.commit();
        } else {
            if (RecycleUnitsManager.getInstance().unlockEWasteUnit()) {
                if(SFXattivi){ soundService.suonoCostruzioneUpgrade(); }
                getSupportFragmentManager().popBackStackImmediate();
                GameManager.getInstance().unPause();
            } else {
                if(SFXattivi){ soundService.suonoBottoni(); }
                Toast toast = Toast.makeText(getApplicationContext(), R.string.sunny_non_sufficienti, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void onClickMenuAcciaio(View view){
        if (RecycleUnitsManager.getInstance().isSteelUnitUnlocked()) {
            if(SFXattivi){ soundService.suonoBottoni(); }
            findViewById(menuBottID).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStackImmediate();
            FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
            fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
            fmt.replace(menuFragID, new MetalUnitFragment());
            fmt.addToBackStack("acciaio");
            fmt.commit();
        } else {
            if (RecycleUnitsManager.getInstance().unlockSteelUnit()) {
                if(SFXattivi){ soundService.suonoCostruzioneUpgrade(); }
                getSupportFragmentManager().popBackStackImmediate();
                GameManager.getInstance().unPause();
            } else {
                if(SFXattivi){ soundService.suonoBottoni(); }
                Toast toast = Toast.makeText(getApplicationContext(), R.string.sunny_non_sufficienti, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void onClickMenuOrganico(View view){
        if (RecycleUnitsManager.getInstance().isCompostUnlocked()) {
            if(SFXattivi){ soundService.suonoBottoni(); }
            findViewById(menuBottID).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStackImmediate();
            FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
            fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
            fmt.replace(menuFragID, new OrganicUnitFragment());
            fmt.addToBackStack("organico");
            fmt.commit();
        } else {
            if (RecycleUnitsManager.getInstance().unlockCompostUnit()) {
                if(SFXattivi){ soundService.suonoCostruzioneUpgrade(); }
                getSupportFragmentManager().popBackStackImmediate();
                GameManager.getInstance().unPause();
            } else {
                if(SFXattivi){ soundService.suonoBottoni(); }
                Toast toast = Toast.makeText(getApplicationContext(), R.string.sunny_non_sufficienti, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    //METODI EFFETTI SONORI PER LE INTERFACCE ------------------------------------------------------
    @Override
    public void suonoGameOver() { if(SFXattivi) { soundService.suonoGameOver(); } }

    @Override
    public void missionFX() {
        if(SFXattivi){ soundService.suonoMissioneCompleta(); }
    }

    @Override
    public void suonoUnità() { if (SFXattivi) {
        soundService.bloccaSuonoUnita();
        soundService.suonoUnitaInFunzione();
    } }

    @Override
    public void suonoBottoni()  {
        if (SFXattivi) { soundService.suonoBottoni(); }
    }

    @Override
    public void suonoUpgrade()  {
        if(SFXattivi) { soundService.suonoCostruzioneUpgrade(); }
    }
}