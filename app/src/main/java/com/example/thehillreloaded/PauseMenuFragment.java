package com.example.thehillreloaded;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.thehillreloaded.Services.BGMusicService;
import com.example.thehillreloaded.Services.SoundEffectService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PauseMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PauseMenuFragment extends Fragment {
    private Switch musicaBottone;
    boolean statoMusica;
    private Switch effettiBottone;
    boolean SFXattivi;
    SoundEffectService soundService;
    Intent avviaMusica;
    //variabili per le SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PauseMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PauseMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PauseMenuFragment newInstance(String param1, String param2) {
        PauseMenuFragment fragment = new PauseMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pause_menu, container, false);

        avviaMusica = new Intent(getActivity().getApplicationContext(), BGMusicService.class);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //getSharedPreferences può essere chiamato solo DOPO l'onCreate di un'attività
        pref = getActivity().getApplicationContext().getSharedPreferences("HillR_pref", Context.MODE_PRIVATE);
        editor = pref.edit();
        effettiBottone = (Switch) getView().findViewById(R.id.switch_sfx_ingame);
        musicaBottone = (Switch) getView().findViewById(R.id.switch_musica_ingame);

        SFXattivi = pref.getBoolean("SFX_attivi", true);
        statoMusica = pref.getBoolean("Musica_attiva", true);
        musicaBottone.setChecked(statoMusica);
        musicaBottone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (musicaBottone.isChecked()) {
                    //attiva la musica in background e salva la scelta nelle SharedPreferences
                    getActivity().startService(avviaMusica);
                    statoMusica = true;
                    editor.putBoolean("Musica_attiva", true);
                    editor.apply();
                } else if (!musicaBottone.isChecked()) {
                    //ferma la musica in background e salva la scelta nelle SharedPreferences
                    getActivity().stopService(avviaMusica);
                    statoMusica = false;
                    editor.putBoolean("Musica_attiva", false);
                    editor.apply();
                }
            }
        });
        effettiBottone.setChecked(SFXattivi);
        effettiBottone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (effettiBottone.isChecked()) {
                    //attiva gli effetti sonori e salva la scelta nelle SharedPreferences
                    editor.putBoolean("SFX_attivi", true);
                    editor.apply();
                    SFXattivi = pref.getBoolean("SFX_attivi", true);
                } else if (!effettiBottone.isChecked()) {
                    //disattiva gli effetti sonori e salva la scelta nelle SharedPreferences
                    editor.putBoolean("SFX_attivi", false);
                    editor.apply();
                    SFXattivi = pref.getBoolean("SFX_attivi", true);
                }
            }
        });

    }
}