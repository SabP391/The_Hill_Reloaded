package com.example.thehillreloaded;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.thehillreloaded.Game.RecycleUnitsManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MetalUnitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MetalUnitFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MetalUnitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MetalUnitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MetalUnitFragment newInstance(String param1, String param2) {
        MetalUnitFragment fragment = new MetalUnitFragment();
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
        View view = inflater.inflate(R.layout.fragment_metal_unit, container, false);

        ImageButton sblocco1 = (ImageButton) view.findViewById(R.id.metal_unlockable1);
        ImageButton sblocco2 = (ImageButton) view.findViewById(R.id.metal_unlockable2);
        ImageButton sblocco3 = (ImageButton) view.findViewById(R.id.metal_unlockable3);
        ImageButton sblocco4 = (ImageButton) view.findViewById(R.id.metal_unlockable4);

        sblocco1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RecycleUnitsManager.getInstance().unlockSteelObject(0)) {
                    sblocco(1);
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            getString(R.string.unit_non_sufficienti), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        sblocco2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RecycleUnitsManager.getInstance().unlockSteelObject(1)) {
                    sblocco(2);
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            getString(R.string.unit_non_sufficienti), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        sblocco3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RecycleUnitsManager.getInstance().unlockSteelObject(2)) {
                    sblocco(3);
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            getString(R.string.unit_non_sufficienti), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        sblocco4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RecycleUnitsManager.getInstance().unlockSteelObject(3)) {
                    sblocco(4);
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            getString(R.string.unit_non_sufficienti), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        return view;
    }

    public void sblocco(int valore) {
        Bundle bundle = new Bundle();
        bundle.putString("tipo_unita", "ACCIAIO");
        bundle.putInt("num_unlockable", valore);
        FragmentManager childFM = getChildFragmentManager();
        FragmentTransaction ft = childFM.beginTransaction();
        UnlockablesFragment sbloccato = new UnlockablesFragment();
        sbloccato.setArguments(bundle);
        ft.replace(R.id.unlockable_metal, sbloccato);
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.addToBackStack("fragment_sbloccabili");
        ft.commit();
    }
}