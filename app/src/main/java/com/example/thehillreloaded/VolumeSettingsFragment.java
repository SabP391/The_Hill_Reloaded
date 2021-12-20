package com.example.thehillreloaded;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VolumeSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VolumeSettingsFragment extends Fragment {
    private Switch musicaBottone;
    private boolean statoMusica;
    private SoundFX sfx;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VolumeSettingsFragment() {
        // Required empty public constructor
    }

    public VolumeSettingsFragment(Boolean bool){
        statoMusica = bool;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VolumeSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VolumeSettingsFragment newInstance(String param1, String param2) {
        VolumeSettingsFragment fragment = new VolumeSettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_volume_settings, container, false);
        //Imposta lo stato iniziale dello switch in base allo stato della musica


        musicaBottone = (Switch) view.findViewById(R.id.switch_musica);
        switchInitState();
        musicaBottone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (musicaBottone.isChecked()) {
                    //apre il music player
                    sfx.mettiMusica();
                    statoMusica = false;
                } else if (!musicaBottone.isChecked()) {
                    //chiude il music player
                    sfx.togliMusica();
                    statoMusica = true;
                }
            }
        });

        return view;
    }

    interface SoundFX {
        void suonoBottoni();
        void mettiMusica();
        void togliMusica();
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

    public void switchInitState(){
        musicaBottone.setChecked(statoMusica);
    }

}