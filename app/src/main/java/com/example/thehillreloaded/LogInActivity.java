package com.example.thehillreloaded;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thehillreloaded.Model.FirebaseUserDataAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

public class LogInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Intent menuUtente;
    private static final String TAG = "LoginActivity";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();
        menuUtente = new Intent(this, UserMenuActivity.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        pref = getApplicationContext().getSharedPreferences("HillR_pref", MODE_PRIVATE);
        editor = pref.edit();

        if(pref.getAll().containsKey("account-utente-loggato")){
            startActivity(menuUtente);
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        // TODO: Se l'utente è loggato andare in MainActivity
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseUserDataAccount loggedDataAccount = new FirebaseUserDataAccount(user.getEmail(), user.getDisplayName(),
                    user.getPhoneNumber(),user.getProviderId(), user.getUid(), user.getTenantId());
            editor.putString("account-utente-loggato", gson.toJson(loggedDataAccount));
            //System.out.println(gson.toJson(loggedDataAccount).toString());
            editor.commit();
            startActivity(menuUtente);
        } else {
            Toast.makeText(getApplicationContext(), "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void tvSignInClick(View view) {
        Log.d("LoginActivity","SignIn Click");
        Intent intent1 = new Intent(this, RegisterActivity.class);
        startActivity(intent1);
    }

    public void btnLoginClick(View view) {
        Log.d("Login Activity","Login Button Click");
        //Collegare le variabili ai Widgets
        EditText mMail =(EditText) findViewById(R.id.etLogEmail);
        EditText mPassword =(EditText) findViewById(R.id.etLogPass);
        //Passo il volore delle variabili globali alle varibili locali
        String password = mPassword.getText().toString();
        String mailUtente = mMail.getText().toString();

        //faccio il Log per vedere se il valore delle variabili viene passato correttamente
        Log.d("LoginActivity", mailUtente);
        Log.d("LoginActivity", password);
        if(!(password.length()>7)||!(mailUtente.contains("@"))) {
            Toast.makeText(getApplicationContext(), "email non valida", Toast.LENGTH_LONG).show();
            return;
        }else if(!(password.length()>7)){
            Toast.makeText(getApplicationContext(), "password non valida", Toast.LENGTH_LONG).show();
            return;
        }else {
            //richiamo metodo per gestire il login con Firebase
            loginUser(mailUtente, password);
        }
    }
}