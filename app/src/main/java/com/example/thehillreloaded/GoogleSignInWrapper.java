package com.example.thehillreloaded;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

// Classe wrapper per gestire l'autenticazione tramite google
// questa classe è stata costruita come singleton in modo da poter accedere ai dati necessari
// da qualsiasi activity

public class GoogleSignInWrapper {
    private static GoogleSignInWrapper instance;
    private GoogleSignInOptions opzioniAutenticazione;
    private GoogleSignInClient clientAutenticazioneGoogle;
    private GoogleSignInAccount account;

    // Costruttore privato: è possibile accedervi solo tramite instance
    private GoogleSignInWrapper(Context context)
    {
        // Creazione dell'oggetto di opzioni di autenticazione
       opzioniAutenticazione = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

       // Creazione del client di autenticazione
       clientAutenticazioneGoogle = GoogleSignIn.getClient(context, opzioniAutenticazione);
    }

    // Metodo che ritorna l'istanza dell'oggetto creato
    public static GoogleSignInWrapper getInstance(Context context)
    {
        if(instance == null)
            instance = new GoogleSignInWrapper (context);
        return instance;
    }

    // Metodo che controlla se è presente un utente che ha già effettuato l'accesso
    public boolean isLogged(Context context)
    {
        // Questo metodo delle API di google ritorna un account se un utente ha effettuato
        // l'accesso, NULL altrimenti
        account = GoogleSignIn.getLastSignedInAccount(context);
        if(account != null){
            return true;
        }
        else{
            return false;
        }
    }

    // Metodo per il login
    // poichè il metodo di login delle API google ha necessità di un activity per essere completato
    // questo metodo lancia un activty trasparente in cui avviene il login effettivo
    public void login(Context context)
    {
        Intent signInIntent = new Intent(context, GoogleSignInActivity.class);
        context.startActivity(signInIntent);
    }

    // Metodo per il logout
    public void logout()
    {
        clientAutenticazioneGoogle.signOut();
    }

    // Metodo per l'eliminazione di un account
    public void revoke()
    {
        clientAutenticazioneGoogle.revokeAccess();
    }

    // Getter per il client, necessario nell'activity di login
    public GoogleSignInClient getClientAutenticazioneGoogle() {
        return clientAutenticazioneGoogle;
    }
}
