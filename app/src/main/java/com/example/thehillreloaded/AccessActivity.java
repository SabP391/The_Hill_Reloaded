package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.thehillreloaded.Model.GoogleLoggedDataAccount;
import com.example.thehillreloaded.Services.BGMusicService;
import com.example.thehillreloaded.Services.SoundEffectService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

public class AccessActivity extends AppCompatActivity {
    //variabili per service
    SoundEffectService soundService;
    boolean soundServiceBound = false;
    Intent effettiSonori;

    //variabili per le SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    //oggetto di tipo Gson che verrà salvato nelle Shared Preferences
    Gson gson = new Gson();
    //boolean per controllare lo stato degli effetti sonori nelle shared preferences
    boolean SFXattivi;

    //costante per l'autenticazione
    //GoogleSignInWrapper autenticazione;
    GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 100;

    //Creazione dell'intent per lanciare il menu con autenticazione
    Intent menuUtente;
    //Creazione dell'intent per lanciare il menu senza autenticazione
    Intent menuOspite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        effettiSonori = new Intent(this, SoundEffectService.class);
        // Creazione degli intent per accedere alle schermate successive
        menuUtente = new Intent(this, UserMenuActivity.class);
        menuOspite = new Intent(this, GuestMenuActivity.class);

        //Autenticazione Google - i commenti a seguire sono quelli della guida ufficiale-
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("315458886685-hilnp7gi0nk2ah0spf46e0alksdov4ll.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // Set the dimensions of the sign-in button.
        Button signInButton = findViewById(R.id.bottone_google);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //viene creato un oggetto di tipo GoogleLoggedDataAccount che contiene le credenziali utenti poi castato a gson
    //e memorizzato su shared preferences con chiave "account-untente-loggato"
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                GoogleLoggedDataAccount googleLoggedDataAccount = new GoogleLoggedDataAccount(acct.getId(),
                        acct.getIdToken(), acct.getDisplayName(), acct.getGivenName(),
                        acct.getFamilyName(), acct.getEmail(), acct.getServerAuthCode(), true);
                editor.putString("account-utente-loggato", gson.toJson(googleLoggedDataAccount));
                editor.commit();

                Toast.makeText(this, "Utente Loggato: ".concat(acct.getEmail()), Toast.LENGTH_SHORT).show();
            }

            startActivity(menuUtente);
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("Messaggio", e.toString());
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    //--fine autenticazione--


    @Override
    public void onStart() {
        super.onStart();

        //getSharedPreferences può essere chiamato solo DOPO l'onCreate di un'attività
        pref = getApplicationContext().getSharedPreferences("HillR_pref", MODE_PRIVATE);
        editor = pref.edit();
        SFXattivi = pref.getBoolean("SFX_attivi", true);

        //binding del service per gli effetti sonori
        bindService(effettiSonori, soundServiceConnection, Context.BIND_AUTO_CREATE);
        // Se l'utente ha già effettuato l'accesso viene reindirizzato al menu
        // per utenti registrati
        /*if(autenticazione.getInstance(this).isLogged(this)){
            startActivity(menuUtente);
            finish();
        }*/
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

    public void ospiteFragment(View view){
        if(SFXattivi){ soundService.suonoBottoni(); }
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
        if(SFXattivi){ soundService.suonoBottoni(); }
        //autenticazione.getInstance(this).login(this);
    }

    public void onClickOspite(View view) {
        if(SFXattivi){ soundService.suonoBottoni(); }
        startActivity(menuOspite);
        finish();
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

}