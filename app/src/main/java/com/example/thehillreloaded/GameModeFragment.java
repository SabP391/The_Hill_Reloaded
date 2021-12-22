package com.example.thehillreloaded;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameModeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameModeFragment extends Fragment {
    private SoundFX sfx;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GameModeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameModeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameModeFragment newInstance(String param1, String param2) {
        GameModeFragment fragment = new GameModeFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_mode, container, false);
        //gestione del bottone della modalità CLASSICA (apre fragment delle difficoltà)
        View selModClassica = view.findViewById(R.id.bottone_classica);
        selModClassica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sfx.suonoBottoni();
                DifficultyFragment modalitaClassica = new DifficultyFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("GAME_MODE",24);
                modalitaClassica.setArguments(bundle);
                selDiffClassicaFragment(modalitaClassica);
            }
        });
        //gestione del bottone della modalità RELOADED (apre fragment delle difficoltà)
        View selModReloaded = view.findViewById(R.id.bottone_reloaded);
        selModReloaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sfx.suonoBottoni();
                DifficultyFragment modalitaReloaded = new DifficultyFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("GAME_MODE",42);
                modalitaReloaded.setArguments(bundle);
                selDiffReloadedFragment(modalitaReloaded);
            }
        });
        return view;
    }

    public void selDiffClassicaFragment(Fragment fragment){
        getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                .add(R.id.fragment_difficolta, fragment)
                .addToBackStack("FragmentClassic")
                .commit();
    }

    public void selDiffReloadedFragment(Fragment fragment){
        getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                .add(R.id.fragment_difficolta, fragment)
                .addToBackStack("FragmentReloaded")
                .commit();
    }

    interface SoundFX {
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