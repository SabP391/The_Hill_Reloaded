package com.example.thehillreloaded.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thehillreloaded.Activities.AccessActivity;
import com.example.thehillreloaded.Model.FirebaseUserDataAccount;
import com.example.thehillreloaded.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

//è un fragment contenente la mail dell'utente loggato  e il pulsante di logout
//al momento visibile su UserMenuActivity ma può essere richiamato ovunque serve
//è da stabilire il redirect al momento del logout (riga 82)

public class AuthFragment extends Fragment {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();
    String email = "";
    TextView emailText;
    TextView logoutText;
    Intent tornaAdAccesso;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = this.getActivity().getSharedPreferences("HillR_pref", 0);
        tornaAdAccesso = new Intent(getActivity(), AccessActivity.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auth, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailText = (TextView) view.findViewById(R.id.user_id);
        logoutText = (TextView) view.findViewById(R.id.logout_btn);
        if(checkIfUserIsLogged()) {
            email = getUser().getEmail().concat(", ");
            emailText.setText(email);
            emailText.setVisibility(View.VISIBLE);
            logoutText.setVisibility(View.VISIBLE);
        } else {
            email = "";
            emailText.setVisibility(View.INVISIBLE);
            logoutText.setVisibility(View.INVISIBLE);
        }
        logoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });
    }

    public void logout(View v) {
        FirebaseAuth.getInstance().signOut();
        emailText = getActivity().findViewById(R.id.user_id);
        logoutText = getActivity().findViewById(R.id.logout_btn);
        emailText.setVisibility(View.INVISIBLE);
        logoutText.setVisibility(View.INVISIBLE);
        editor = pref.edit();
        editor.remove("account-utente-loggato");
        editor.commit();
        Toast.makeText(getActivity(), "Logout Eseguito Correttamente!", Toast.LENGTH_SHORT).show();
        //Redirect al menu
        getActivity().getSupportFragmentManager().popBackStack();
        startActivity(tornaAdAccesso);
    }

    public boolean checkIfUserIsLogged() {
        return (pref.getAll().containsKey("account-utente-loggato")) ? true : false;
    }

    public FirebaseUserDataAccount getUser() {
        return gson.fromJson(pref.getAll().get("account-utente-loggato").toString(), FirebaseUserDataAccount.class);
    }
}