package com.example.thehillreloaded;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
            Log.d("Bundle :", " FUNZIONA");
            tipo_unita = bundle.getString("tipo_unita");
            Log.d("Stringa: ", bundle.toString());
            num_unlockable = bundle.getInt("num_unlockable");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unlockables, container, false);

        if (tipo_unita.equals("PLASTICA")) {
            if (num_unlockable == 1) {
                TextView testo = (TextView) view.findViewById(R.id.unlockable_txt);
                testo.setText("Testo da inserire");
                ImageView img = view.findViewById(R.id.unlockable_img);
                img.setImageDrawable(getResources().getDrawable(R.drawable.asset3_unlockable_plastica_1));
            }
        }

        return view;
    }
}