package com.spaceinvaders.game_entities;

import android.graphics.Bitmap;
import android.graphics.RectF;

import com.spaceinvaders.Resources;

/**
 * Each DefenseBrick represents on piece of a DefenseWall.
 */
public class DefenseBrick extends Sprite {
    // NOTE: Defense block will be able to be hit 4 times before being destroyed
    private short health;
    private boolean isDestroyed;

    public DefenseBrick(Bitmap[] frames) {
        super(frames, new RectF(0.0f, 0.0f,
                                35.0f * Resources.DPIRatio, 28.0f * Resources.DPIRatio));

        doAnimationLoop = false;
        health = 4;
    }

    @Override
    public void update(long fps) {
        // TODO implement update
    }

    public void takeDamage() {
        health--;
        if(health <= 0) {
            setCurrFrame(4);
        }
        setCurrFrame(getCurrFrameIndex() + 1);
    }

    public short getHealth() {
        return health;
    }
}
