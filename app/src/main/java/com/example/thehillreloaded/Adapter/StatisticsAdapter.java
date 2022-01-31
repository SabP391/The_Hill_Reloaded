package com.example.thehillreloaded.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thehillreloaded.Model.GameEnded;
import com.example.thehillreloaded.Model.GameStatistics;
import com.example.thehillreloaded.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.MyViewHolder>{

    Context context;
    ArrayList<GameEnded> gameEndeds;

    public StatisticsAdapter(Context context, ArrayList<GameEnded> gameEndeds) {
        this.context = context;
        this.gameEndeds = gameEndeds;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.statistics_game_row, parent, false);
        return new MyViewHolder(v);
    }

    //Mostra i dati
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GameEnded game = gameEndeds.get(position);
        holder.email.setText(game.getEmail());
        holder.score.setText(String.valueOf(game.getTotalScore()));
        holder.time.setText(String.valueOf(game.getMinutes()).concat("m.").concat(String.valueOf(game.getMinutes())).concat("s."));
    }

    @Override
    public int getItemCount() {
        return gameEndeds.size();
    }

    //Inner Class
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView email, score, time;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            email = itemView.findViewById(R.id.tv_user);
            score = itemView.findViewById(R.id.tv_score);
            time = itemView.findViewById(R.id.tv_time);

        }
    }

}
