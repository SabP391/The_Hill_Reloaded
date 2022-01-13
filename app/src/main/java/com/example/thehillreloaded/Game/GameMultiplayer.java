package com.example.thehillreloaded.Game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.thehillreloaded.GameOverActivity;
import com.example.thehillreloaded.GameWonActivity;
import com.example.thehillreloaded.MultiplayerActivity;
import com.example.thehillreloaded.MultiplayerGameActivity;
import com.example.thehillreloaded.UserMenuActivity;

public class GameMultiplayer extends Game {

    private static final int TIME_TO_PLAY = 1;
    private GameEndFrag frag;

    public GameMultiplayer(Context context, TileMap map, Bundle bundle) {
        super(context, map, bundle);
    }

    public GameMultiplayer(Context context, TileMap map) {
        super(context, map);
        frag = (GameMultiplayer.GameEndFrag) context;
    }

    @Override
    public void gameLogic() {
        long timeNow = System.nanoTime();
        if(!GameManager.getInstance().isPaused()){
            if(GameManager.getInstance().isTimeToSpawn(System.nanoTime())){
                GameItemsManager.getInstance().spawnNewObject();
            }
            for(GameItem i : itemsOnScreen){
                if(i != movingItem){
                    if(i.checkForGameOverPosition()){
                        frag.gameEndChecker();

                    }
                    i.fall(System.nanoTime());
                    if(i.checkForBuffDestruction()){
                        map.setTileValue(i.getCurrentTile(), 0);
                        itemsOnScreen.remove(i);
                    }
                }
            }
        }
        // Aggiornamento del tempo di gioco
        if((timeNow - elapsedTime) / 1000000000 >= 1){
            GameManager.getInstance().getPlayTime().increasePlayTime();
            elapsedTime = timeNow;
        }
        if(GameManager.getInstance().getPlayTime().getSeconds() > 20 && GameManager.getInstance().getPlayTime().getMinutes() >= 0){
            frag.gameEndChecker();
            stopDrawThread();
            frag.endGame();
        }
    }

    public interface GameEndFrag {
        boolean gameEndChecker();
        void endGame();
    }

}
