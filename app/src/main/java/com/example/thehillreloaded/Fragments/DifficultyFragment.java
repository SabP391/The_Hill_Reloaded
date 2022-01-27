package com.example.thehillreloaded.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thehillreloaded.Activities.GameActivity;
import com.example.thehillreloaded.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DifficultyFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DifficultyFragment extends Fragment {
    private SoundFX sfx;
    int gameMode = 0;
    int classicMode = 24;
    int reloadedMode = 42;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClassicDifficultyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DifficultyFragment newInstance(String param1, String param2) {
        DifficultyFragment fragment = new DifficultyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DifficultyFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_difficulty, container, false);
        gameMode = getArguments().getInt("GAME_MODE");


        //gestione del bottone della difficoltà FACILE
        View selDiffFacile = view.findViewById(R.id.bottone_facile);
        selDiffFacile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sfx.suonoBottoni();
                Intent intent = new Intent(getActivity().getBaseContext(), GameActivity.class);
                intent.putExtra("GAME_MODE",gameMode);
                intent.putExtra("GAME_DIFF", 1);
                intent.putExtra("IS_NEW_GAME", true);
                startActivity(intent);
                Log.v("GAME MODE", " "+gameMode);
            }
        });
        //gestione del bottone della difficoltà NORMALE
        View selDiffNormale = view.findViewById(R.id.bottone_normale);
        selDiffNormale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sfx.suonoBottoni();
                Intent intent = new Intent(getActivity().getBaseContext(), GameActivity.class);
                intent.putExtra("GAME_MODE",gameMode);
                intent.putExtra("GAME_DIFF", 2);
                intent.putExtra("IS_NEW_GAME", true);
                startActivity(intent);
                Log.v("GAME MODE", " "+gameMode);
            }
        });
        //gestione del bottone della difficoltà DIFFICILE
        View selDiffDifficile = view.findViewById(R.id.bottone_difficile);
        selDiffDifficile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sfx.suonoBottoni();
                Intent intent = new Intent(getActivity().getBaseContext(), GameActivity.class);
                intent.putExtra("GAME_MODE",gameMode);
                intent.putExtra("GAME_DIFF", 3);
                intent.putExtra("IS_NEW_GAME", true);
                startActivity(intent);
                Log.v("GAME MODE", " "+gameMode);
            }
        });
        return view;
    }

    //interfaccia per effetti sonori
    public interface SoundFX {
        void suonoBottoni();
    }

    //override dei metodi onAttach e onDetach
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sfx = (SoundFX) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        sfx = null;
    }

}