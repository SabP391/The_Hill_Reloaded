package com.example.thehillreloaded.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.thehillreloaded.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EndMultiplayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EndMultiplayerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EndMultiplayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EndMultiplayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EndMultiplayerFragment newInstance(String param1, String param2) {
        EndMultiplayerFragment fragment = new EndMultiplayerFragment();
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
        View view = inflater.inflate(R.layout.fragment_end_multiplayer, container, false);
        RotateAnimation animazioneSole = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animazioneSole.setInterpolator(new LinearInterpolator());
        // setRepeatCount è impostato su Animation.INFINITE per ripetere l'animazione in loop
        animazioneSole.setRepeatCount(Animation.INFINITE);
        animazioneSole.setDuration(7000);

        // Inizia l'animazione del sole che ruota
        final ImageView soleRotante = (ImageView) view.findViewById(R.id.sole_rotante_sp2);
        soleRotante.startAnimation(animazioneSole);
        return view;
    }
}