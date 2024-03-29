package com.example.thehillreloaded.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.thehillreloaded.Adapter.StatisticsAdapter;
import com.example.thehillreloaded.Model.GameEnded;
import com.example.thehillreloaded.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                //prelevo i dati dal firebase e creo la mappa, dopodichè creo la lista dalla mappa filtrata
                //tramite il metodo filterHashMap ed eseguo il sort su essa per score
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

    //genera il testo da visualizzare
    private String generatePlainText() {
        String ranking = "";
        int i = 1;
        for (GameEnded gameEnded: gameEndeds) {
            ranking = ranking + i +"°: " + gameEnded.getEmail() +
                    ", score: " + gameEnded.getTotalScore() +
                    ", time: "+ String.valueOf(gameEnded.getMinutes()).concat("m.").concat(String.valueOf(gameEnded.getMinutes())).concat("s.\n");
            i++;
        }
        return ranking;
    }

    //metodo per filtrare la mappa, prendo la partita con punteggio maggiore per singolo utente
    public GameEnded filterHashMap(HashMap<String, Object> hashMap) {
        GameEnded gameEnded = new GameEnded();
        int i = 0;
        for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
            Map<String, GameEnded> map = (Map<String, GameEnded>) entry.getValue();
            Object[] array = map.values().toArray();
            GameEnded obj = new GameEnded((long)array[4],(long)array[1],(long)array[3],(String)array[5],(long) array[2],
                    (long)array[0]);
            if(i==4) {
                gameEnded = obj;
            } else {
                if(gameEnded.getTotalScore()<obj.getTotalScore()) {
                    gameEnded = obj;
                }
            }
        }
        return gameEnded;
    }

    //Eseguo il sort della classifica per score
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

    //metodo che si lancia al click del bottone share per condividere le classifiche
    public void btnShareClick(View view) {
        // The value which we will sending through data via
        // other applications is defined
        // via the Intent.ACTION_SEND (Intent implicito)
        Intent intent2 = new Intent();
        intent2.setAction(Intent.ACTION_SEND);
        // setting type of data shared as text
        intent2.setType("text/plain");
        intent2.putExtra(Intent.EXTRA_SUBJECT, "Ranking");

        // Adding the text to share using putExtra
        intent2.putExtra(Intent.EXTRA_TEXT, generatePlainText());
        //Adding Chooser
        startActivity(Intent.createChooser(intent2, "Share Via"));
    }
}