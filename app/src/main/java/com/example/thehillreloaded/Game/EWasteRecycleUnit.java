package com.example.thehillreloaded.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.widget.Toast;

import com.example.thehillreloaded.R;

public class EWasteRecycleUnit extends RecycleUnit{

    public EWasteRecycleUnit(TileMap map, Context context){
        super(map, context);
        this.acceptedItemType = ItemType.EWASTE;
        this.sprite = GameAssets.getInstance(context).getEwasteUnit(size);
        position = new Point((int)((map.getMapSize().x * map.getTileSize()) -
                (2 * map.getTileSize() + offsetFromRight)), (int)(map.getTileSize() * 2));
        initMyTiles();
        setProcessSlotsPosition();
    }

    @Override
    public void setProcessSlotsPosition() {
        slotsXPosition = position.x - (int) map.getTileSize() - 5;
        firstSlotLineYPosition = position.y + (int)((grayLine.getStrokeWidth() / 2) + (map.getTileSize()));
        secondSlotLineYPosition = firstSlotLineYPosition + (int)grayLine.getStrokeWidth() + 5;
        thirdSlotLineYPosition = secondSlotLineYPosition + (int)grayLine.getStrokeWidth() + 5;
    }

    @Override
    public void downgradeMessage() {
        Toast.makeText(context, R.string.ew_perso_upgrade, Toast.LENGTH_SHORT).show();
    }

}
