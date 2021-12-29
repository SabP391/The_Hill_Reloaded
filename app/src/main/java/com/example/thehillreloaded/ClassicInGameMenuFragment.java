package com.example.thehillreloaded;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassicInGameMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassicInGameMenuFragment extends Fragment {
    private SoundFX sfx;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClassicInGameMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InGameMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassicInGameMenuFragment newInstance(String param1, String param2) {
        ClassicInGameMenuFragment fragment = new ClassicInGameMenuFragment();
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

        FragmentManager childFM = getChildFragmentManager();
        FragmentTransaction ft = childFM.beginTransaction();
        ft.replace(R.id.mission_frag_layout_c, new MissionsFragment() );
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_classic_in_game_menu, container, false);

        TextView costo = (TextView) view.findViewById(R.id.txt_costo_c);
        costo.setText("tanti soldi");

        return view;
    }

    interface SoundFX {
        void suonoBottoni();

        //quando saranno attivi i bottoni, aggiungere -> sfx.suonoBottoni(); <-
    }

    //override dei metodi onAttach e onDetach
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sfx = (ClassicInGameMenuFragment.SoundFX) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        sfx = null;
    }
}