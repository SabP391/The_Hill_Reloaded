package com.example.thehillreloaded;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thehillreloaded.Game.GameManager;
import com.example.thehillreloaded.Game.RecycleUnitsManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReloadedInGameMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReloadedInGameMenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReloadedInGameMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReloadedInGameMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReloadedInGameMenuFragment newInstance(String param1, String param2) {
        ReloadedInGameMenuFragment fragment = new ReloadedInGameMenuFragment();
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

        if (!GameManager.getInstance().isMultiplayerGame()) {
            FragmentManager childFM = getChildFragmentManager();
            FragmentTransaction ft = childFM.beginTransaction();
            ft.replace(R.id.mission_frag_layout_r, new MissionsFragment());
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_reloaded_in_game_menu, container, false);

        if (!RecycleUnitsManager.getInstance().isPlasticUnitUnlocked()) {
            TextView costo_plastica = (TextView) view.findViewById(R.id.txt_costo_r);
            costo_plastica.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sole_mini, 0,0, 0);
            costo_plastica.setText(" " + RecycleUnitsManager.getInstance().getCostOfPlasticUnit()); }
        if (!RecycleUnitsManager.getInstance().isPaperUnitUnlocked()) {
            TextView costo_carta = (TextView) view.findViewById(R.id.txt_costo6_r);
            costo_carta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sole_mini, 0,0, 0);
            costo_carta.setText(" " + RecycleUnitsManager.getInstance().getCostOfPaperUnit()); }
        if (!RecycleUnitsManager.getInstance().isSteelUnitUnlocked()) {
            TextView costo_acciaio = (TextView) view.findViewById(R.id.txt_costo4_r);
            costo_acciaio.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sole_mini, 0,0, 0);
            costo_acciaio.setText(" " + RecycleUnitsManager.getInstance().getCostOfSteelUnit()); }
        if (!RecycleUnitsManager.getInstance().isAluminiumUnitUnlocked()) {
            TextView costo_alluminio = (TextView) view.findViewById(R.id.txt_costo5_r);
            costo_alluminio.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sole_mini, 0,0, 0);
            costo_alluminio.setText(" " + RecycleUnitsManager.getInstance().getCostOfAluminiumUnit()); }
        if (!RecycleUnitsManager.getInstance().isEwasteUnitUnlocked()) {
            TextView costo_ewaste = (TextView) view.findViewById(R.id.txt_costo3_r);
            costo_ewaste.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sole_mini, 0,0, 0);
            costo_ewaste.setText(" " + RecycleUnitsManager.getInstance().getCostOfEwasteUnit()); }
        if (!RecycleUnitsManager.getInstance().isCompostUnlocked()) {
            TextView costo_organico = (TextView) view.findViewById(R.id.txt_costo7_r);
            costo_organico.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sole_mini, 0,0, 0);
            costo_organico.setText(" " + RecycleUnitsManager.getInstance().getCostOfCompostUnit()); }

        return view;
    }
}