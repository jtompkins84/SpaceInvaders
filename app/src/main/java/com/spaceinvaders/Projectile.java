package com.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created by Joseph on 1/31/2016.
 */
public class Projectile extends Sprite {

    private boolean isFromPlayer = false;
    private boolean isDestroyed = false;

    /**
     * Create an instance of <code>Projectile</code>.
     * @param posX <code>float</code> X-position
     * @param posY <code>float</code> Y-position
     * @param isFromPlayer <code>boolean</code>
     */
    public Projectile(float posX, float posY, boolean isFromPlayer) {
        // TODO add other projectile images/animations
        super(new Bitmap[] {Resources.img_projectile_a},
                new RectF(9.0f * Resources.DPIRatio, 9.0f * Resources.DPIRatio,
                        14.0f * Resources.DPIRatio, 31.0f * Resources.DPIRatio));

        speed = 450;
        setPosition(posX, posY);

        if(isFromPlayer) {
            isFromPlayer(true);
            movement = Movement.UP;
        }
        else {
            isFromPlayer(false);
            movement = Movement.DOWN;
        }
    }

    @Override
    public void update(long fps) {
        if(movement == Movement.UP && fps > 0) {
            this.move(0.0f, -(speed / fps));
        }
        else if(movement == Movement.DOWN && fps > 0) {
            this.move(0.0f, speed / fps);
        }
    }

    public void isFromPlayer(boolean b) {
        isFromPlayer = b;
        if(isFromPlayer) movement = Movement.UP;
        else movement = Movement.DOWN;
    }

    public boolean isFromPlayer() {
        return isFromPlayer;
    }

    public void isDestroyed(boolean b) {
        isDestroyed = b;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}
