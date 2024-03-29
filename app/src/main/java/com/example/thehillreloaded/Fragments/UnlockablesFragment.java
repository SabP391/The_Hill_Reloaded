package com.example.thehillreloaded.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thehillreloaded.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UnlockablesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnlockablesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String tipo_unita;
    private int num_unlockable;

    public UnlockablesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UnlockablesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UnlockablesFragment newInstance(String param1, int param2) {
        UnlockablesFragment fragment = new UnlockablesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            tipo_unita = bundle.getString("tipo_unita");
            num_unlockable = bundle.getInt("num_unlockable");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unlockables, container, false);
        Button close = (Button) view.findViewById(R.id.unlockable_close);
        TextView testo = (TextView) view.findViewById(R.id.unlockable_txt);
        ImageView img = view.findViewById(R.id.unlockable_img);

        if (tipo_unita.equals("PLASTICA")) {
            switch (num_unlockable) {
                case 1:
                    testo.setText(getString(R.string.str_plastica1));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_plastica_1));
                    break;
                case 2:
                    testo.setText(getString(R.string.str_plastica2));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_plastica_2));
                    break;
                case 3:
                    testo.setText(getString(R.string.str_plastica3));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_plastica_3));
                    break;
                case 4:
                    testo.setText(getString(R.string.str_plastica4));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_plastica_4));
                    break;
            }
        }

        if (tipo_unita.equals("CARTA")) {
            switch (num_unlockable) {
                case 1:
                    testo.setText(getString(R.string.str_carta1));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_carta_1));
                    break;
                case 2:
                    testo.setText(getString(R.string.str_carta2));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_carta_2));
                    break;
                case 3:
                    testo.setText(getString(R.string.str_carta3));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_carta_3));
                    break;
                case 4:
                    testo.setText(getString(R.string.str_carta4));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_carta_4));
                    break;
            }
        }

        if (tipo_unita.equals("VETRO")) {
            switch (num_unlockable) {
                case 1:
                    testo.setText(getString(R.string.str_vetro1));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_vetro_1));
                    break;
                case 2:
                    testo.setText(getString(R.string.str_vetro2));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_vetro_2));
                    break;
                case 3:
                    testo.setText(getString(R.string.str_vetro3));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_vetro_3));
                    break;
                case 4:
                    testo.setText(getString(R.string.str_vetro4));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_vetro_4));
                    break;
            }
        }

        if (tipo_unita.equals("ALLUMINIO")) {
            switch (num_unlockable) {
                case 1:
                    testo.setText(getString(R.string.str_alluminio1));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_alluminio_1));
                    break;
                case 2:
                    testo.setText(getString(R.string.str_alluminio2));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_alluminio_2));
                    break;
                case 3:
                    testo.setText(getString(R.string.str_alluminio3));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_alluminio_3));
                    break;
                case 4:
                    testo.setText(getString(R.string.str_alluminio4));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_alluminio_4));
                    break;
            }
        }

        if (tipo_unita.equals("ACCIAIO")) {
            switch (num_unlockable) {
                case 1:
                    testo.setText(getString(R.string.str_acciaio1));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_acciaio_1));
                    break;
                case 2:
                    testo.setText(getString(R.string.str_acciaio2));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_acciaio_2));
                    break;
                case 3:
                    testo.setText(getString(R.string.str_acciaio3));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_acciaio_3));
                    break;
                case 4:
                    testo.setText(getString(R.string.str_acciaio4));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_acciaio_4));
                    break;
            }
        }

        if (tipo_unita.equals("EWASTE")) {
            switch (num_unlockable) {
                case 1:
                    testo.setText(getString(R.string.str_ewaste1));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_ewaste_1));
                    break;
                case 2:
                    testo.setText(getString(R.string.str_ewaste2));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_ewaste_2));
                    break;
                case 3:
                    testo.setText(getString(R.string.str_ewaste3));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_ewaste_3));
                    break;
                case 4:
                    testo.setText(getString(R.string.str_ewaste4));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_ewaste_4));
                    break;
            }
        }

        if (tipo_unita.equals("ORGANICO")) {
            switch (num_unlockable) {
                case 1:
                    testo.setText(getString(R.string.str_umido));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_umido_1));
                    break;
                case 2:
                    testo.setText(getString(R.string.str_umido));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_umido_2));
                    break;
                case 3:
                    testo.setText(getString(R.string.str_umido));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_umido_3));
                    break;
                case 4:
                    testo.setText(getString(R.string.str_umido));
                    img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_umido_4));
                    break;
            }
        }

        // if per ogni tipo di unità di riciclo
        // switch in ogni if per i 4 tipi di unlockables

        return view;
    }

}