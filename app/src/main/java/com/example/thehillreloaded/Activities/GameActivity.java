package com.example.thehillreloaded.Activities;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.thehillreloaded.Fragments.AluminiumUnitFragment;
import com.example.thehillreloaded.Fragments.ClassicInGameMenuFragment;
import com.example.thehillreloaded.Fragments.EwasteUnitFragment;
import com.example.thehillreloaded.Game.Difficulty;
import com.example.thehillreloaded.Game.Game;
import com.example.thehillreloaded.Game.GameItemsManager;
import com.example.thehillreloaded.Game.GameManager;
import com.example.thehillreloaded.Game.GameMode;
import com.example.thehillreloaded.Game.QuestManager;
import com.example.thehillreloaded.Game.RecycleUnit;
import com.example.thehillreloaded.Game.RecycleUnitsManager;
import com.example.thehillreloaded.Game.TileMap;
import com.example.thehillreloaded.Fragments.GlassUnitFragment;
import com.example.thehillreloaded.Fragments.MetalUnitFragment;
import com.example.thehillreloaded.Game.TutorialState;
import com.example.thehillreloaded.Model.GameSuspended;
import com.example.thehillreloaded.Model.RecycleUnitSave;
import com.example.thehillreloaded.Fragments.OrganicUnitFragment;
import com.example.thehillreloaded.Fragments.PaperUnitFragment;
import com.example.thehillreloaded.Fragments.PauseMenuFragment;
import com.example.thehillreloaded.Fragments.PlasticUnitFragment;
import com.example.thehillreloaded.R;
import com.example.thehillreloaded.Fragments.ReloadedInGameMenuFragment;
import com.example.thehillreloaded.Services.SoundEffectService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

// Activity che ospita il gioco vero e proprio
public class GameActivity extends AppCompatActivity implements QuestManager.SoundFX,
        GlassUnitFragment.SoundFX, PaperUnitFragment.SoundFX,
        PlasticUnitFragment.SoundFX, EwasteUnitFragment.SoundFX,
        MetalUnitFragment.SoundFX, AluminiumUnitFragment.SoundFX,
        OrganicUnitFragment.SoundFX, RecycleUnitsManager.SoundFx, Game.SoundFx{
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
    private boolean SFXattivi;

    int menuFragID;
    int menuBottID;
    int menuPausaID;

    Intent effettiSonori;

    //creo oggetto Gson con codice per esclusione errori
    Gson gson = new Gson();

    @SuppressLint("RtlHardcoded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        effettiSonori = new Intent(this, SoundEffectService.class);
        pref = getApplicationContext().getSharedPreferences("HillR_pref", MODE_PRIVATE);

        Intent intent = getIntent();
        Bundle initInformation = intent.getExtras();
        boolean isNewGame = initInformation.getBoolean("IS_NEW_GAME");

        // Distruzione dei manager eventualmente presenti ancora in memoria
        GameManager.getInstance().destroy();
        QuestManager.getInstance().destroy();
        RecycleUnitsManager.getInstance().destroy();
        GameItemsManager.getInstance().destroy();

        // Inizializzazione della partita ----------------------------------------------------------

        // Controlla che si stia iniziando una nuova partita
        Game game;
        if (isNewGame) {
            // Se è una nuova partita crea una nupva tilemap
            map = new TileMap(this);

            // Recupero della modalità di gioco e della
            // difficoltà dall'intent lanciato nel menu
            int mode = initInformation.getInt("GAME_MODE");
            int diff = initInformation.getInt("GAME_DIFF");

            // Assegnazione della modalità di gioco
            switch (mode) {
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
            switch (diff) {
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

            // Inizializzazione dei manager di gioco
            GameManager.getInstance().initInstance(gameMode, difficulty, this, map);
            GameItemsManager.getInstance().initInstance(this, map);
            RecycleUnitsManager.getInstance().initInstance(this, map);
            QuestManager.getInstance().initInstance(this);

            // Creazione della partita
        } else {
            // Se si sta invece continuando una partita salvata
            // Recupero dalle SharedPreferences del salvataggio
            GameSuspended gameSuspended = gson.fromJson(pref.getString("game-pause", null),
                    GameSuspended.class);

            // Caricamento della tilemap
            map = new TileMap(this, gameSuspended.getTileMap());

            // Caricamento di difficoltà e modalità di gioco
            gameMode = gameSuspended.getGameMode();
            difficulty = gameSuspended.getDifficulty();
            // Caericamento del game manager
            GameManager.getInstance().gameManagerReload(gameSuspended.getSunnyPoints(),
                    gameSuspended.getTimeAtGameStart(),gameSuspended.getDifficulty(),
                    gameSuspended.getGameMode(), gameSuspended.getPlayTime(), this, map);
            // Caricamento del GameItemsManager
            GameItemsManager.getInstance().gameItemsManagerReload(this, map);
            // Caricamentro del RecycleUnitsManager
            RecycleUnitsManager.getInstance().recycleUnitsManagerReload(gameSuspended.isPaperUnitUnlocked(),
                    gameSuspended.isCompostUnlocked(),gameSuspended.isAluminiumUnitUnlocked(),
                    gameSuspended.isSteelUnitUnlocked(),
                    gameSuspended.isPlasticUnitUnlocked(),gameSuspended.isEwasteUnitUnlocked(),
                    gameSuspended.isGlassUnitUnlocked(),
                    this, map, gameSuspended.getRecycleUnitSave());
            // Caricamento del QuestManager
            QuestManager.getInstance().questManagerReload(gameSuspended.isQuest1(),gameSuspended.isQuest2(),
                    gameSuspended.isQuest3(), gameSuspended.getCounterQuest3(),
                    gameSuspended.isQuest4(),gameSuspended.getCounterQuest4(),gameSuspended.isQuest5(),
                    gameSuspended.isQuest6(),gameSuspended.getCounterQuest6(), this);

            // Creazione della partita
        }
        game = new Game(this, map, initInformation);


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
        menuPausaID = pauseFragment.getId();
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
                    //Richiamo il metodo che torna il bundle con l'oggetto GameSuspended
                    creaMenuPausa(pauseFragment, new PauseMenuFragment(), setBundleOnPausedGame());
                } else {
                    if(pauseFragment.getVisibility() == View.GONE) {
                        //gioco in pausa e fragment di pausa non attivo = chiude altri fragment e apre menu di pausa
                        pauseFragment.setVisibility(View.VISIBLE);
                        //Richiamo il metodo che torna il bundle con l'oggetto GameSuspended
                        creaMenuPausa(pauseFragment, new PauseMenuFragment(), setBundleOnPausedGame());
                    } else if(pauseFragment.getVisibility() == View.VISIBLE) {
                        //gioco in pausa e fragment attivo = chiude fragment e toglie pausa
                        pauseFragment.setVisibility(View.GONE);
                        getSupportFragmentManager().popBackStackImmediate();
                        if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                            GameManager.getInstance().unPause();
                        }
                        SFXattivi = pref.getBoolean("SFX_attivi", true);
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

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

    //Ho aggiunto il Bundle (che contiene l'oggetto GameSuspended) da passare al fragment PauseMenuFragment
    public void creaMenuPausa(FragmentContainerView container, PauseMenuFragment fragment, Bundle bundle){
        // Setto l'argomento da passare al fragment contenente il bundle
        fragment.setArguments(bundle);
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
        } else {
            GameManager.getInstance().pause();
            findViewById(menuPausaID).setVisibility(View.VISIBLE);
            //Richiamo il metodo che torna il bundle con l'oggetto GameSuspended
            creaMenuPausa(findViewById(menuPausaID), new PauseMenuFragment(), setBundleOnPausedGame());
            //super.onBackPressed();
        }
        SFXattivi = pref.getBoolean("SFX_attivi", true);
    }

    //Necessari per il service binding
    private final ServiceConnection soundServiceConnection = new ServiceConnection() {
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
        if(RecycleUnitsManager.getInstance().isGlassUnitUnlocked()) {
            if (SFXattivi) { soundService.suonoBottoni(); }
            findViewById(menuBottID).setVisibility(View.GONE);
            getSupportFragmentManager().popBackStackImmediate();
            FragmentTransaction fmt = getSupportFragmentManager().beginTransaction();
            fmt.setCustomAnimations(R.anim.slide_up, R.anim.slide_down);
            fmt.replace(menuFragID, new GlassUnitFragment());
            fmt.addToBackStack("vetro");
            fmt.commit();
        } else {
            if (RecycleUnitsManager.getInstance().unlockGlassUnit()){
                if(SFXattivi){ soundService.suonoCostruzioneUpgrade(); }
                getSupportFragmentManager().popBackStackImmediate();
                GameManager.getInstance().unPause();
            } else {
                if(SFXattivi){ soundService.suonoBottoni(); }
                Toast toast = Toast.makeText(getApplicationContext(), R.string.sunny_non_sufficienti, Toast.LENGTH_LONG);
                toast.show();
            }
        }
        switch (GameManager.getInstance().getTutorialState()){
            case STARTED:
                GameManager.getInstance().setTutorialState(TutorialState.GLASS_BUILT);
        }
    }

    public void onClickMenuCarta(View view){
        if (RecycleUnitsManager.getInstance().isPaperUnitUnlocked()) {
            if(SFXattivi){ soundService.suonoBottoni(); }
            QuestManager.getInstance().isQuest1Complete();
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
            QuestManager.getInstance().isQuest1Complete();
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
            QuestManager.getInstance().isQuest1Complete();
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
            QuestManager.getInstance().isQuest1Complete();
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
            QuestManager.getInstance().isQuest1Complete();
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
        if (RecycleUnitsManager.getInstance().isCompostUnitUnlocked()) {
            if(SFXattivi){ soundService.suonoBottoni(); }
            QuestManager.getInstance().isQuest1Complete();
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

    public void onClickEsciDaMenu(View view){
        if(SFXattivi){ soundService.suonoBottoni(); }
        findViewById(menuBottID).setVisibility(View.VISIBLE);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
            GameManager.getInstance().unPause();
        }
        SFXattivi = pref.getBoolean("SFX_attivi", true);
    }

    //metodo per la chiusura dei fragment innestati
    public void closeChildFrag(View view){
        //chiusura fragment fattibile solo da activity, gestione del backstack sconsigliata nei fragment
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag.isVisible()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                    return;
                }
            }
        }
    }

    //chiusura gioco senza salvataggio
    public void closeGameNotSaving(View view){
        finish();
    }

    @Override
    public void missionFX() {
        if(SFXattivi){ soundService.suonoMissioneCompleta(); }
    }

    @Override
    public void suonoBottoni() {
        if (SFXattivi) { soundService.suonoBottoni(); }
    }

    @Override
    public void suonoUpgrade() {
        if(SFXattivi) { soundService.suonoCostruzioneUpgrade(); }
    }

    @Override
    public void suonoUnita() { if (SFXattivi) {
        soundService.bloccaSuonoUnita();
        soundService.suonoUnitaInFunzione();
    } }

    @Override
    public void suonoGameOver() { if(SFXattivi) { soundService.suonoGameOver(); } }

    //Setto il bundle per il salvataggio degli oggetti da passare al fragment PauseMenuFragment
    public Bundle setBundleOnPausedGame() {
        List<RecycleUnitSave> listaRu = new ArrayList<>();
        for(RecycleUnit recycleUnit : RecycleUnitsManager.getInstance().getUnlockedUnits()){
            RecycleUnitSave rus = new RecycleUnitSave(recycleUnit.getUnitStatus(),recycleUnit.getAcceptedItemType(),recycleUnit.getCurrentWearLevel(),recycleUnit.getUnitPoints());
            listaRu.add(rus);
        }

        Bundle bundle = new Bundle();
        bundle.putString("game-pause", gson.toJson(new GameSuspended(GameManager.getInstance().isPaused(), GameManager.getInstance().getSunnyPoints(),
                GameManager.getInstance().getTimeAtGameStart(), System.nanoTime(),
                RecycleUnitsManager.getInstance().isPaperUnitUnlocked(), RecycleUnitsManager.getInstance().isCompostUnitUnlocked(),
                RecycleUnitsManager.getInstance().isAluminiumUnitUnlocked(), RecycleUnitsManager.getInstance().isSteelUnitUnlocked(),
                RecycleUnitsManager.getInstance().isPlasticUnitUnlocked(), RecycleUnitsManager.getInstance().isEwasteUnitUnlocked(),
                RecycleUnitsManager.getInstance().isGlassUnitUnlocked(),
                QuestManager.getInstance().isQuest1Complete(), QuestManager.getInstance().isQuest2Complete(),
                QuestManager.getInstance().isQuest3Complete(), QuestManager.getInstance().getCounterQuest3(),
                QuestManager.getInstance().isQuest4Complete(), QuestManager.getInstance().getCounterQuest4(),
                QuestManager.getInstance().isQuest5Complete(), QuestManager.getInstance().isQuest6Complete(),
                QuestManager.getInstance().getCounterQuest6(), GameManager.getInstance(), listaRu, map.getTileMap(),
                GameManager.getInstance().getDifficulty(),GameManager.getInstance().getGameMode(),GameManager.getInstance().getPlayTime())));
        return bundle;
    }
}