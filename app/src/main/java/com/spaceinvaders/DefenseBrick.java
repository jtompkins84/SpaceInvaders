package com.spaceinvaders;

import android.graphics.RectF;

/**
 * Each DefenseBrick represents on piece of a DefenseWall.
 */
public class DefenseBrick extends Sprite {
    // NOTE: Defense block will be able to be hit 4 times before being destroyed
    private short health;

    // TODO this is just a placeholder constructor
    public DefenseBrick(SpriteImage[] animation) {
        super(animation, new RectF[] {new RectF(0.0f, 0.0f, 35.0f, 28.0f)});
        hitBox = hitBoxes[0];

        health = 4;
    }

    @Override
    public void update(long fps) {
        // TODO implement update
    }

    /**
     * Ratio necessary for placing bricks in correct spot.
     * @return
     */
    public float getDPIRatio() {
        return this.image.getDPIRatio();
    }
}
