package com.example.thehillreloaded;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.thehillreloaded.Adapter.StatisticsAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameStatisticsActivity extends AppCompatActivity {

    private FirebaseDatabase mScore;
    private DatabaseReference mdatabaseReference;

    private StatisticsAdapter statisticsAdapter;
    private RecyclerView rvStatisticsGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_statistics);

        initUI();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvStatisticsGame.setLayoutManager(linearLayoutManager);
        mdatabaseReference = FirebaseDatabase.getInstance().getReference();
        statisticsAdapter = new StatisticsAdapter(this,mdatabaseReference,"j");
        rvStatisticsGame.setAdapter(statisticsAdapter);

    }

    private void initUI(){
        rvStatisticsGame = (RecyclerView)findViewById(R.id.rv_statistics);
    }
    @Override
    protected void onStop() {
        super.onStop();
        statisticsAdapter.clean();
    }
}