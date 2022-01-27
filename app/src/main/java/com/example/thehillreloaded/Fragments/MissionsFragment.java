package com.example.thehillreloaded.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thehillreloaded.Game.GameManager;
import com.example.thehillreloaded.Game.GameMode;
import com.example.thehillreloaded.Game.QuestManager;
import com.example.thehillreloaded.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MissionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MissionsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView q1, q2, q3, q4, q5, q6;
    private TextView counter3, counter4, counter6;
    private ImageView check1, check2, check3, check4, check5, check6;

    public MissionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MissionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MissionsFragment newInstance(String param1, String param2) {
        MissionsFragment fragment = new MissionsFragment();
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
        View view = inflater.inflate(R.layout.fragment_missions, container, false);

        FindStuffById(view);

        if (GameManager.getInstance().getGameMode() == GameMode.CLASSIC) {
            //Quest 1
            if (QuestManager.getInstance().isQuest1Complete()) {
                check1.setVisibility(View.VISIBLE);
            } else {
                check1.setVisibility(View.INVISIBLE);
            }

            //Quest 2
            if (QuestManager.getInstance().isQuest2Complete()) {
                check2.setVisibility(View.VISIBLE);
            } else {
                check2.setVisibility(View.INVISIBLE);
            }

            //Quest3
            counter3 = (TextView) view.findViewById(R.id.counter_3);
            if (QuestManager.getInstance().isQuest3Complete()){
                counter3.setText("3/3");
                check3.setVisibility(View.VISIBLE);
            }else {
                counter3.setText(Integer.toString(QuestManager.getInstance().getCounterQuest3()) + "/3");
                check3.setVisibility(View.INVISIBLE);
            }

            counter4.setVisibility(View.INVISIBLE);
            q4.setVisibility(View.INVISIBLE);
            check4.setVisibility(View.INVISIBLE);
            q5.setVisibility(View.INVISIBLE);
            check5.setVisibility(View.INVISIBLE);
            counter6.setVisibility(View.INVISIBLE);
            q6.setVisibility(View.INVISIBLE);
            check6.setVisibility(View.INVISIBLE);
        } else if (GameManager.getInstance().getGameMode() == GameMode.RELOADED) {
                //Quest 1
                if (QuestManager.getInstance().isQuest1Complete()) {
                    check1.setVisibility(View.VISIBLE);
                } else {
                    check1.setVisibility(View.INVISIBLE);
                }

                //Quest 2
                if (QuestManager.getInstance().isQuest2Complete()) {
                    check2.setVisibility(View.VISIBLE);
                } else {
                    check2.setVisibility(View.INVISIBLE);
                }

                //Quest3
                if (QuestManager.getInstance().isQuest3Complete()){
                 counter3.setText("3/3");
                 check3.setVisibility(View.VISIBLE);
                }else {
                 counter3.setText(Integer.toString(QuestManager.getInstance().getCounterQuest3()) + "/3");
                 check3.setVisibility(View.INVISIBLE);
                }

                //Quest 4
                if (QuestManager.getInstance().isQuest4Complete()){
                    counter4.setText("35/35");
                    check4.setVisibility(View.VISIBLE);
                }else{
                    counter4.setText(Integer.toString(QuestManager.getInstance().getCounterQuest4()) + "/35");
                    check4.setVisibility(View.INVISIBLE);
                }

                //Quest 5
                if(QuestManager.getInstance().isQuest5Complete()) {
                    check5.setVisibility(View.VISIBLE);
                }else{
                    check5.setVisibility(View.INVISIBLE);
                }

                //Quest 6
                if (QuestManager.getInstance().isQuest6Complete()){
                    counter6.setText("20/20");
                    check6.setVisibility(View.VISIBLE);
                }else{
                    counter6.setText(Integer.toString(QuestManager.getInstance().getCounterQuest6()) + "/20");
                    check6.setVisibility(View.INVISIBLE);
                }

        }
        return view;
    }

    private void FindStuffById (View view){
        q1 = (TextView) view.findViewById(R.id.quest1_text);
        q2 = (TextView) view.findViewById(R.id.quest2_text);
        q3 = (TextView) view.findViewById(R.id.quest3_text);
        q4 = (TextView) view.findViewById(R.id.quest4_text);
        q5 = (TextView) view.findViewById(R.id.quest5_text);
        q6 = (TextView) view.findViewById(R.id.quest6_text);
        counter3 = (TextView) view.findViewById(R.id.counter_3);
        counter4 = (TextView) view.findViewById(R.id.counter_4);
        counter6 = (TextView) view.findViewById(R.id.counter_6);
        check1 = (ImageView) view.findViewById(R.id.quest1_check);
        check2 = (ImageView) view.findViewById(R.id.quest2_check);
        check3 = (ImageView) view.findViewById(R.id.quest3_check);
        check4 = (ImageView) view.findViewById(R.id.quest4_check);
        check5 = (ImageView) view.findViewById(R.id.quest5_check);
        check6 = (ImageView) view.findViewById(R.id.quest6_check);
    }
}