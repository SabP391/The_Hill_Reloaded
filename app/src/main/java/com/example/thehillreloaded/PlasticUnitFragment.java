package com.example.thehillreloaded;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlasticUnitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlasticUnitFragment extends Fragment {

    Boolean num1 = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlasticUnitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlasticUnitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlasticUnitFragment newInstance(String param1, String param2) {
        PlasticUnitFragment fragment = new PlasticUnitFragment();
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
        View view = inflater.inflate(R.layout.fragment_plastic_unit, container, false);

        ImageButton sblocco1 = (ImageButton) view.findViewById(R.id.plastic_unlockable1);
        sblocco1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("tipo_unita", "PLASTICA");
                bundle.putInt("num_unlockable", 1);
                FragmentManager childFM = getChildFragmentManager();
                FragmentTransaction ft = childFM.beginTransaction();
                UnlockablesFragment sblocca_1 = new UnlockablesFragment();
                sblocca_1.setArguments(bundle);
                ft.replace(R.id.unlockable_plastica, sblocca_1);
                ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        /*
        plastic_unlockable1

        Bundle bundle = new Bundle();
        bundle.putString("tipo_unita", "PLASTICA");
        bundle.putInt("num_unlockable", 1);
        FragmentManager childFM = getChildFragmentManager();
        FragmentTransaction ft = childFM.beginTransaction();
        UnlockablesFragment sblocca_1 = new UnlockablesFragment();
        sblocca_1.setArguments(bundle);
        ft.replace(R.id.unlockable_plastica, sblocca_1);
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.addToBackStack(null);
        ft.commit();
         */

        return view;
    }

    public void sblocca_n1(View view){

    }

}