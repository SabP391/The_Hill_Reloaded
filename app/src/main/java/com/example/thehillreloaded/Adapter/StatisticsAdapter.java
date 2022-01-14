package com.example.thehillreloaded.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.thehillreloaded.Model.GameStatistics;
import com.example.thehillreloaded.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder>{

    private Activity mActivity;
    private DatabaseReference  mDatabaseReference;
    private String mDisplayName;
    private ArrayList <DataSnapshot> mdataSnapshots;

    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        //ogni volta che un figlio viene aggiunto al nodo Padre Utenti, viene chiamato questo metodo
        public void onChildAdded(DataSnapshot dataSnapshot,String previousChildName) {
            mdataSnapshots.add(dataSnapshot);
            notifyDataSetChanged();

        }

        @Override
        public void onChildChanged(DataSnapshot snapshot,  String previousChildName) {

        }

        @Override
        public void onChildRemoved( DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot snapshot,String previousChildName) {

        }

        @Override
        public void onCancelled(DatabaseError error) {

        }
    };

    public StatisticsAdapter(Activity activity, DatabaseReference ref, String name){
        mActivity = activity;
        mDatabaseReference = ref.child("Utenti");
        mDisplayName = name;
        mdataSnapshots = new ArrayList<>();

        //collego il db
        mDatabaseReference.addChildEventListener(mListener);
    }

    public class StatisticsViewHolder extends RecyclerView.ViewHolder{
        TextView user;
        TextView score;
        TextView gameTime;
        LinearLayout.LayoutParams params;

        public StatisticsViewHolder(View itemView) {
            super(itemView);
            user= (TextView)itemView.findViewById(R.id.tv_user);
            score = (TextView)itemView.findViewById(R.id.tv_score);
            gameTime = (TextView)itemView.findViewById(R.id.tv_time);
            params = (LinearLayout.LayoutParams) user.getLayoutParams();
        }

    }

    //metodi implementati in automatico dopo l'extends
    @Override
    public StatisticsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //recupero il layout inflater
        LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.statistics_game_row, parent, false);
        StatisticsViewHolder vh = new StatisticsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(StatisticsViewHolder holder, int position) {
        //Ricevo da firebase il vettore mdataSnapshots contenente i dati
        DataSnapshot snapshot = mdataSnapshots.get(position);
        //salvo il contenuto di DataSanpshot all'interno dell'oggetto gameSatistics
        GameStatistics gameStatistics = snapshot.getValue(GameStatistics.class);

        holder.user.setText(gameStatistics.getUser());
        holder.gameTime.setText((int) gameStatistics.getGameTime());
        holder.score.setText(gameStatistics.getScore());
    }


    @Override
    public int getItemCount() {
        return mdataSnapshots.size();
    }

    public void clean(){
        mDatabaseReference.removeEventListener(mListener);
    }
}
