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

import com.example.thehillreloaded.Model.FirebaseUserDataAccount;
import com.example.thehillreloaded.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
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
        // Check if user is signed in (non-null)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        pref = getApplicationContext().getSharedPreferences("HillR_pref", MODE_PRIVATE);
        editor = pref.edit();

        if(pref.getAll().containsKey("account-utente-loggato") && currentUser != null){
            startActivity(menuUtente);
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            FirebaseUserDataAccount loggedDataAccount = new FirebaseUserDataAccount(currentUser.getEmail(), currentUser.getDisplayName(),
                    currentUser.getPhoneNumber(),currentUser.getProviderId(), currentUser.getUid(), currentUser.getTenantId());
            editor.putString("account-utente-loggato", gson.toJson(loggedDataAccount));
            editor.commit();
            startActivity(menuUtente);
        } else {
            FirebaseAuth.getInstance().signOut();
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
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            switch (errorCode) {
                                case "ERROR_INVALID_CUSTOM_TOKEN":
                                    Toast.makeText(getApplicationContext(), "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                    Toast.makeText(getApplicationContext(), "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_INVALID_CREDENTIAL":
                                    Toast.makeText(getApplicationContext(), "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_INVALID_EMAIL":
                                    Toast.makeText(getApplicationContext(), "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_WRONG_PASSWORD":
                                    Toast.makeText(getApplicationContext(), "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_USER_MISMATCH":
                                    Toast.makeText(getApplicationContext(), "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_REQUIRES_RECENT_LOGIN":
                                    Toast.makeText(getApplicationContext(), "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                    Toast.makeText(getApplicationContext(), "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    Toast.makeText(getApplicationContext(), "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                    Toast.makeText(getApplicationContext(), "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_USER_DISABLED":
                                    Toast.makeText(getApplicationContext(), "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_USER_TOKEN_EXPIRED":
                                    Toast.makeText(getApplicationContext(), "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_USER_NOT_FOUND":
                                    Toast.makeText(getApplicationContext(), "There is no user record corresponding to this identifier. Email not valid.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_INVALID_USER_TOKEN":
                                    Toast.makeText(getApplicationContext(), "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_OPERATION_NOT_ALLOWED":
                                    Toast.makeText(getApplicationContext(), "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_WEAK_PASSWORD":
                                    Toast.makeText(getApplicationContext(), "The given password is invalid.", Toast.LENGTH_LONG).show();
                                    break;
                            }
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
        //richiamo metodo per gestire il login con Firebase
        loginUser(mailUtente, password);
    }
}