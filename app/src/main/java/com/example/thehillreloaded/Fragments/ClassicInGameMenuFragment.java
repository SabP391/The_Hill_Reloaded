package com.example.thehillreloaded.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thehillreloaded.Game.GameManager;
import com.example.thehillreloaded.Game.RecycleUnitsManager;
import com.example.thehillreloaded.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassicInGameMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassicInGameMenuFragment extends Fragment {
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

        if (!RecycleUnitsManager.getInstance().isGlassUnitUnlocked()){
            TextView costo_vetro = (TextView) view.findViewById(R.id.txt_costo7_c);
            costo_vetro.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sole_mini, 0,0, 0);
            costo_vetro.setText(" " + RecycleUnitsManager.getInstance().getCostOfGlassUnit()); }
        if (!RecycleUnitsManager.getInstance().isPlasticUnitUnlocked()){
            TextView costo_plastica = (TextView) view.findViewById(R.id.txt_costo_c);
            costo_plastica.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sole_mini, 0,0, 0);
            costo_plastica.setText(" " + RecycleUnitsManager.getInstance().getCostOfPlasticUnit()); }
        if (!RecycleUnitsManager.getInstance().isPaperUnitUnlocked()) {
            TextView costo_carta = (TextView) view.findViewById(R.id.txt_costo6_c);
            costo_carta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sole_mini, 0,0, 0);
            costo_carta.setText(" " + RecycleUnitsManager.getInstance().getCostOfPaperUnit()); }
        if (!RecycleUnitsManager.getInstance().isSteelUnitUnlocked()) {
            TextView costo_acciaio = (TextView) view.findViewById(R.id.txt_costo4_c);
            costo_acciaio.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sole_mini, 0,0, 0);
            costo_acciaio.setText(" " + RecycleUnitsManager.getInstance().getCostOfSteelUnit()); }
        if (!RecycleUnitsManager.getInstance().isAluminiumUnitUnlocked()) {
            TextView costo_alluminio = (TextView) view.findViewById(R.id.txt_costo5_c);
            costo_alluminio.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sole_mini, 0,0, 0);
            costo_alluminio.setText(" " + RecycleUnitsManager.getInstance().getCostOfAluminiumUnit()); }
        if (!RecycleUnitsManager.getInstance().isEwasteUnitUnlocked()) {
            TextView costo_ewaste = (TextView) view.findViewById(R.id.txt_costo3_c);
            costo_ewaste.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sole_mini, 0,0, 0);
            costo_ewaste.setText(" " + RecycleUnitsManager.getInstance().getCostOfEwasteUnit()); }

        switch (GameManager.getInstance().getTutorialState()) {
            case STARTED: {
                TextView tutorial_label = (TextView) view.findViewById(R.id.txt_glass_build_tut);
                tutorial_label.setVisibility(View.VISIBLE);
                ImageView tutorial_img = (ImageView) view.findViewById(R.id.img_glass_build_tut);
                tutorial_img.setVisibility(View.VISIBLE);
                break;
            }
            case GLASS_BUILT: {
                TextView tutorial_label = (TextView) view.findViewById(R.id.txt_glass_build_tut);
                tutorial_label.setVisibility(View.VISIBLE);
                tutorial_label.setText(R.string.tut_glass_open);
                ImageView tutorial_img = (ImageView) view.findViewById(R.id.img_glass_build_tut);
                tutorial_img.setVisibility(View.VISIBLE);
                break;
            }
        }
        return view;
    }
}