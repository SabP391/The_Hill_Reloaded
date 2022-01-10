package com.example.thehillreloaded.Game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.thehillreloaded.GameOverActivity;
import com.example.thehillreloaded.GameWonActivity;

public class GameMultiplayer extends Game {

    public GameMultiplayer(Context context, TileMap map, Bundle bundle) {
        super(context, map, bundle);
    }

    public GameMultiplayer(Context context, TileMap map) {
        super(context, map);
    }

    @Override
    public void gameLogic() {
        super.gameLogic();

        if(!GameManager.getInstance().isPaused()){
            if(GameManager.getInstance().isTimeToSpawn(System.nanoTime())){
                GameItemsManager.getInstance().spawnNewObject();
            }
            for(GameItem i : itemsOnScreen){
                if(i != movingItem){
                    if(i.checkForGameOverPosition()){
                        Intent gameLost = new Intent(context, GameOverActivity.class);
                        gameLost.putExtras(info);
                        stopDrawThread();
                        sFX.suonoGameOver();
                        context.startActivity(gameLost);
                    }
                    i.fall(System.nanoTime());
                    if(i.checkForBuffDestruction()){
                        map.setTileValue(i.getCurrentTile(), 0);
                        itemsOnScreen.remove(i);
                    }
                }
            }
        }
    }
}
