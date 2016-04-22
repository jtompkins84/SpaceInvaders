package com.spaceinvaders.game_entities;

import android.graphics.Bitmap;

import com.spaceinvaders.Resources;

public class PlayerShield extends Particle {

    /**
     * This is the y-offset from the player, so that the shield appears above the player.
     */
    private float yOffset = Resources.img_player.getHeight() / 4.0f;

    public PlayerShield(float posX, float posY) {
        super(new Bitmap[] {Resources.img_player_shield}, posX, posY);

        setPosition(posX, posY);
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y - yOffset);
    }

    @Override
    public void update(long fps) {

    }
}
