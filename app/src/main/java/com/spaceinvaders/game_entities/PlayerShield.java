package com.spaceinvaders.game_entities;

import android.graphics.Bitmap;

import com.spaceinvaders.Resources;

/**
 * Created by Joseph on 4/19/2016.
 */
public class PlayerShield extends Particle {

    /**
     * This is the y-offset from the player, so that the shield appears above the player.
     */
    private float yOffset = Resources.img_player.getHeight();

    public PlayerShield(float posX, float posY) {
        super(new Bitmap[] {}, posX, posY);

    }

    @Override
    public void setPosition(float x, float y) {

    }

    @Override
    public void update(long fps) {

    }
}
