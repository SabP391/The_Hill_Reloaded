package com.example.thehillreloaded;

import static com.example.thehillreloaded.Game.RecycleUnitStatus.BASE;
import static com.example.thehillreloaded.Game.RecycleUnitStatus.UPGRADED_ONCE;
import static com.example.thehillreloaded.Game.RecycleUnitStatus.UPGRADED_TWICE;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thehillreloaded.Game.RecycleUnitsManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrganicUnitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrganicUnitFragment extends Fragment {
    //componenti layout
    TextView costo1;
    TextView costo2;
    TextView costo3;
    TextView costo4;
    TextView ricavo1;
    TextView ricavo2;
    TextView ricavo3;
    TextView ricavo4;
    ImageButton sblocco1;
    ImageButton sblocco2;
    ImageButton sblocco3;
    ImageButton sblocco4;
    TextView unitPoints;
    TextView unitStatus;
    TextView unitWear;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrganicUnitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrganicUnitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrganicUnitFragment newInstance(String param1, String param2) {
        OrganicUnitFragment fragment = new OrganicUnitFragment();
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
        View view = inflater.inflate(R.layout.fragment_organic_unit, container, false);

        // Caratteristiche unità -------------------------------------------------------------------
        unitPoints = (TextView) view.findViewById(R.id.organic_up);
        unitStatus = (TextView) view.findViewById(R.id.organic_status);
        unitWear = (TextView) view.findViewById(R.id.organic_wear);

        setUnitDetails();

        // Caratteristiche oggetti sbloccabili -----------------------------------------------------
        costo1 = (TextView) view.findViewById(R.id.organic_costo1);
        costo2 = (TextView) view.findViewById(R.id.organic_costo2);
        costo3 = (TextView) view.findViewById(R.id.organic_costo3);
        costo4 = (TextView) view.findViewById(R.id.organic_costo4);
        ricavo1 = (TextView) view.findViewById(R.id.organic_ricavo1);
        ricavo2 = (TextView) view.findViewById(R.id.organic_ricavo2);
        ricavo3 = (TextView) view.findViewById(R.id.organic_ricavo3);
        ricavo4 = (TextView) view.findViewById(R.id.organic_ricavo4);

        // Sblocco oggetti in unità ----------------------------------------------------------------
        sblocco1 = (ImageButton) view.findViewById(R.id.organic_unlockable1);
        sblocco2 = (ImageButton) view.findViewById(R.id.organic_unlockable2);
        sblocco3 = (ImageButton) view.findViewById(R.id.organic_unlockable3);
        sblocco4 = (ImageButton) view.findViewById(R.id.organic_unlockable4);

        setLayoutElements();

        sblocco1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RecycleUnitsManager.getInstance().unlockCompostObject(0)) {
                    if (!RecycleUnitsManager.getInstance().getCompostObject(0)) {
                        sblocco(1);
                        RecycleUnitsManager.getInstance().setCompostObject(0);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Oggetto sbloccato!", Toast.LENGTH_SHORT).show();
                    }
                    unitPoints.setText(getString(R.string.text_unit_points,
                            RecycleUnitsManager.getInstance().getCompostUnit().getUnitPoints()));
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
                if (RecycleUnitsManager.getInstance().unlockCompostObject(1)) {
                    if (!RecycleUnitsManager.getInstance().getCompostObject(1)) {
                        sblocco(2);
                        RecycleUnitsManager.getInstance().setCompostObject(1);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Oggetto sbloccato!", Toast.LENGTH_SHORT).show();
                    }
                    unitPoints.setText(getString(R.string.text_unit_points,
                            RecycleUnitsManager.getInstance().getCompostUnit().getUnitPoints()));
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
                if (RecycleUnitsManager.getInstance().unlockCompostObject(2)) {
                    if (!RecycleUnitsManager.getInstance().getCompostObject(2)) {
                        sblocco(3);
                        RecycleUnitsManager.getInstance().setCompostObject(2);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Oggetto sbloccato!", Toast.LENGTH_SHORT).show();
                    }
                    unitPoints.setText(getString(R.string.text_unit_points,
                            RecycleUnitsManager.getInstance().getCompostUnit().getUnitPoints()));
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
                if (RecycleUnitsManager.getInstance().unlockCompostObject(3)) {
                    if (!RecycleUnitsManager.getInstance().getCompostObject(3)) {
                        sblocco(4);
                        RecycleUnitsManager.getInstance().setCompostObject(3);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Oggetto sbloccato!", Toast.LENGTH_SHORT).show();
                    }
                    unitPoints.setText(getString(R.string.text_unit_points,
                            RecycleUnitsManager.getInstance().getCompostUnit().getUnitPoints()));
                } else {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            getString(R.string.unit_non_sufficienti), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        Button upgrade = (Button) view.findViewById(R.id.organic_upgrade_button);
        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RecycleUnitsManager.getInstance().getCompostUnit().upgradeUnit()){
                    setLayoutElements();
                    setUnitDetails();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Non puoi fare cose.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void sblocco(int valore){
        Bundle bundle = new Bundle();
        bundle.putString("tipo_unita", "ORGANICO");
        bundle.putInt("num_unlockable", valore);
        FragmentManager childFM = getChildFragmentManager();
        FragmentTransaction ft = childFM.beginTransaction();
        UnlockablesFragment sbloccato = new UnlockablesFragment();
        sbloccato.setArguments(bundle);
        ft.replace(R.id.unlockable_organic, sbloccato);
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.addToBackStack("fragment_sbloccabili");
        ft.commit();
    }

    public void setLayoutElements(){
        Drawable tempSun = getActivity().getResources().getDrawable(R.drawable.ic_sole);
        tempSun.setTint(getResources().getColor(R.color.verde_scuro));

        sblocco1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.asset3_unlockable_umido_1));
        sblocco1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        sblocco1.setPadding(25,25,25,25);
        costo1.setText(getString(R.string.text_costo_sbloccabili, RecycleUnitsManager.getInstance().getCostOfUnlockable1()));
        costo1.setCompoundDrawablesWithIntrinsicBounds(0, 0 ,R.drawable.ic_unit_points_mini, 0);
        costo1.setCompoundDrawablePadding(5);
        ricavo1.setText(getString(R.string.text_ricavo_sbloccabili, RecycleUnitsManager.getInstance().getGainOfUnlockable1()));
        ricavo1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sole_mini, 0);
        ricavo1.setCompoundDrawablePadding(5);

        if (RecycleUnitsManager.getInstance().getCompostUnit().getUnitStatus() == BASE) {
            sblocco2.setImageDrawable(tempSun);
            sblocco2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            sblocco2.setPadding(25, 25, 25, 25);
            sblocco3.setImageDrawable(tempSun);
            sblocco3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            sblocco3.setPadding(25, 25, 25, 25);
            sblocco4.setImageDrawable(tempSun);
            sblocco4.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            sblocco4.setPadding(25, 25, 25, 25);
            sblocco2.setEnabled(false);
            sblocco3.setEnabled(false);
            sblocco4.setEnabled(false);
        }

        if (RecycleUnitsManager.getInstance().getGlassUnit().getUnitStatus() == UPGRADED_ONCE) {
            sblocco4.setImageDrawable(tempSun);
            sblocco4.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            sblocco4.setPadding(25, 25, 25, 25);
            sblocco2.setEnabled(true);
            sblocco3.setEnabled(true);
            sblocco4.setEnabled(false);
        }

        if (RecycleUnitsManager.getInstance().getCompostUnit().getUnitStatus() == UPGRADED_ONCE
                || RecycleUnitsManager.getInstance().getCompostUnit().getUnitStatus() == UPGRADED_TWICE ) {
            sblocco2.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.asset3_unlockable_umido_2));
            sblocco2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            sblocco2.setPadding(25,25,25,25);
            costo2.setText(getString(R.string.text_costo_sbloccabili, RecycleUnitsManager.getInstance().getCostOfUnlockable2()));
            costo2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_unit_points_mini, 0);
            costo2.setCompoundDrawablePadding(5);
            ricavo2.setText(getString(R.string.text_ricavo_sbloccabili, RecycleUnitsManager.getInstance().getGainOfUnlockable2()));
            ricavo2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sole_mini, 0);
            ricavo2.setCompoundDrawablePadding(5);

            sblocco3.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.asset3_unlockable_umido_3));
            sblocco3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            sblocco3.setPadding(25,25,25,25);
            costo3.setText(getString(R.string.text_costo_sbloccabili, RecycleUnitsManager.getInstance().getCostOfUnlockable3()));
            costo3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_unit_points_mini, 0);
            costo3.setCompoundDrawablePadding(5);
            ricavo3.setText(getString(R.string.text_ricavo_sbloccabili, RecycleUnitsManager.getInstance().getGainOfUnlockable3()));
            ricavo3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sole_mini, 0);
            ricavo3.setCompoundDrawablePadding(5);
        }

        if (RecycleUnitsManager.getInstance().getCompostUnit().getUnitStatus() == UPGRADED_TWICE) {
            sblocco4.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.asset3_unlockable_umido_4));
            sblocco4.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            sblocco4.setPadding(25,25,25,25);
            costo4.setText(getString(R.string.text_costo_sbloccabili, RecycleUnitsManager.getInstance().getCostOfUnlockable4()));
            costo4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_unit_points_mini, 0);
            costo4.setCompoundDrawablePadding(5);
            ricavo4.setText(getString(R.string.text_ricavo_sbloccabili, RecycleUnitsManager.getInstance().getGainOfUnlockable4()));
            ricavo4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sole_mini, 0);
            ricavo4.setCompoundDrawablePadding(5);
            sblocco4.setEnabled(true);
        }
    }


    public void setUnitDetails(){
        int uPoints = RecycleUnitsManager.getInstance().getCompostUnit().getUnitPoints();
        unitPoints.setText(getString(R.string.text_unit_points, uPoints));
        int wear = RecycleUnitsManager.getInstance().getCompostUnit().getCurrentWearLevel();
        unitWear.setText(getString(R.string.text_usura, wear, RecycleUnitsManager.getInstance().getCompostUnit().getMaximumWearLevel()));
        switch (RecycleUnitsManager.getInstance().getCompostUnit().getUnitStatus()){
            case BASE: unitStatus.setText(getString(R.string.text_status, 0));
                break;
            case UPGRADED_ONCE: unitStatus.setText(getString(R.string.text_status, 1));
                break;
            case UPGRADED_TWICE: unitStatus.setText(getString(R.string.text_status, 2));
                break;
        }
    }
}