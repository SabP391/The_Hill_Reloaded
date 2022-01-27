package com.example.thehillreloaded.Game;

import android.content.Context;
import android.os.Bundle;

public class GameMultiplayer extends Game {

    private static final int TIME_TO_PLAY = 4;
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
                GameItemsManager.getInstance().spawnNewObjectMultiplayer();
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
        if(GameManager.getInstance().getPlayTime().getMinutes() >= TIME_TO_PLAY){
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
