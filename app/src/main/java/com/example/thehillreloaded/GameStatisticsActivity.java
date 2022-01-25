package com.example.thehillreloaded;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.thehillreloaded.Adapter.StatisticsAdapter;
import com.example.thehillreloaded.Game.Game;
import com.example.thehillreloaded.Model.GameEnded;
import com.example.thehillreloaded.Model.GameStatistics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GameStatisticsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    StatisticsAdapter statisticsAdapter;
    ArrayList<GameEnded> gameEndeds;
    FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_statistics);

        mDatabase = FirebaseDatabase.getInstance("https://the-hill-reloaded-f6f3b-default-rtdb.europe-west1.firebasedatabase.app");
        recyclerView = findViewById(R.id.rv_statistics);
        databaseReference = mDatabase.getReference().child("Utenti");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gameEndeds = new ArrayList<>();
        statisticsAdapter = new StatisticsAdapter(this, gameEndeds);
        recyclerView.setAdapter(statisticsAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Object hashMap = dataSnapshot.getValue();
                    HashMap<String, Object> map = (HashMap<String, Object>) hashMap;
                    gameEndeds.add(filterHashMap(map));
                    Collections.reverse(sortList(gameEndeds));
                }
                statisticsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String generatePlainText() {
        String ranking = "";
        int i = 0;
        for (GameEnded gameEnded: gameEndeds) {
            ranking = ranking + i +"°: " + gameEnded.getEmail() +
                    ", score: " + gameEnded.getTotalScore() +
                    ", time: " + TimeUnit.NANOSECONDS.toMinutes(gameEnded.getGameTime()) + " m.\n";
            i++;
        }
        return ranking;
    }

    public GameEnded filterHashMap(HashMap<String, Object> hashMap) {
        GameEnded gameEnded = new GameEnded();
        int i = 0;
        for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
            Map<String, GameEnded> map = (Map<String, GameEnded>) entry.getValue();
            Object[] array = map.values().toArray();
            GameEnded obj = new GameEnded((long)array[2],(long)array[0],(long)array[1],(String)array[3]);
            if(i==0) {
                gameEnded = obj;
            } else {
                if(gameEnded.getTotalScore()<obj.getTotalScore()) {
                    gameEnded = obj;
                }
            }
        }
        return gameEnded;
    }

    private List<GameEnded> sortList(List<GameEnded> list) {
        for (int i = 1; i < list.size(); i++) {
            GameEnded current = list.get(i);
            int j = i - 1;
            while(j >= 0 && current.getTotalScore() < list.get(j).getTotalScore()) {
                list.set(j+1, list.get(j));
                j--;
            }
            // at this point we've exited, so j is either -1
            // or it's at the first element where current >= a[j]
            list.set(j+1, current);
        }
        return list;
    }


    public void btnShareClick(View view) {
        // The value which we will sending through data via
        // other applications is defined
        // via the Intent.ACTION_SEND
        Intent intent2 = new Intent();
        intent2.setAction(Intent.ACTION_SEND);
        // setting type of data shared as text
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_SUBJECT, "Ranking");

        // Adding the text to share using putExtra
        intent2.putExtra(Intent.EXTRA_TEXT, generatePlainText());
        startActivity(Intent.createChooser(intent2, "Share Via"));
    }
}