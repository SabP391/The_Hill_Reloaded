package com.example.thehillreloaded.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thehillreloaded.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText mConfermaPassword;
    private FirebaseAuth mAuth;
    EditText mEmail;
    EditText mPassword;
    EditText mNome;
    // Costanti
    static final String CHAT_PREFS = "ChatPrefs";
    static final String EMAIL_KEY = "email";

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Toast.makeText(this, "Utente già loggato", Toast.LENGTH_SHORT).show();
        //updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //referenzia tutte le view
        initUI();
        mConfermaPassword = (EditText)findViewById(R.id.etRegPassConf);
        mAuth = FirebaseAuth.getInstance();
    }

    private void initUI() {
        mEmail= (EditText)findViewById(R.id.etRegEmail);
        mPassword= (EditText)findViewById(R.id.etRegPass);
        mConfermaPassword= (EditText)findViewById(R.id.etRegPassConf);
        mNome= (EditText)findViewById(R.id.etRegName);
    }

    //crea un nuovo utente in Firebase
    private void createFirebaseUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LoginRegistrazione", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Toast.makeText(RegisterActivity.this,"Registration success.", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LoginRegistrazione", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this,"Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    // TODO: Salvare il campo nome all'interno delle SharedPreferences
    private void salvaNome(){
        String nome = mEmail.getText().toString();
        SharedPreferences prefs = getSharedPreferences(CHAT_PREFS, 0);
        prefs.edit().putString(EMAIL_KEY, nome).apply();
    }


    public void btnSignInClick(View view) {
        Log.d("Register Activity","Button SignIn clicked");
        String nome = mNome.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        //validazione dati
        if(!nomeValido(nome))
            Toast.makeText(RegisterActivity.this,"Nome non valido.", Toast.LENGTH_SHORT).show();
        else if (!emailValida(email)){
            Toast.makeText(RegisterActivity.this,"Email non valida.", Toast.LENGTH_SHORT).show();
        }
        else if(! passwordValida(password)){
            Toast.makeText(RegisterActivity.this,"Password non valida.", Toast.LENGTH_SHORT).show();
        }else{
            createFirebaseUser(email, password);
            Toast.makeText(RegisterActivity.this,"Registration success.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        }


    }

    public void tvLoginClick(View view) {
        Log.d("LoginActivity","Login Click");
        Intent intent2 = new Intent(this, LogInActivity.class);
        startActivity(intent2);
    }

    //funzioni di validazione input activity di registrazione
    private boolean nomeValido (String nome){
        if(nome.length()>3){
            return true;
        }else{
            return false;
        }
    }

    private boolean emailValida (String email){
        return email.contains("@");
    }

    private boolean passwordValida (String password){
        String confermaPassword = mConfermaPassword.getText().toString();
        return confermaPassword.equals(password) && password.length()>7;
    }
}