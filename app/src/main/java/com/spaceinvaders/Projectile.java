package com.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created by Joseph on 1/31/2016.
 */
public class Projectile extends Sprite {
    public enum Type {PLAYER, PLAYER_SPECIAL, INVADER, LASER, POWERUP};

    protected Type mType = Type.PLAYER;
    private boolean isFromPlayer = false;
    private boolean isDestroyed = false;

    /**
     * Create an instance of <code>Projectile</code>.
     * @param posX <code>float</code> X-position
     * @param posY <code>float</code> Y-position
     * @param isFromPlayer <code>boolean</code>
     */
    @Deprecated
    public Projectile(float posX, float posY, boolean isFromPlayer) {
        // TODO add other projectile images/animations
        super(new Bitmap[] {Resources.img_projectile_a},
                new RectF(9.0f * Resources.DPIRatio, 9.0f * Resources.DPIRatio,
                        14.0f * Resources.DPIRatio, 31.0f * Resources.DPIRatio));

        speed = 450;
        setPosition(posX, posY);

        if(isFromPlayer) {
            mType = Type.PLAYER;
            isFromPlayer(true);
            movement = Movement.UP;
        }
        else {
            mType = Type.INVADER;
            isFromPlayer(false);
            movement = Movement.DOWN;
        }
    }

    /**
     * Create an instance of a <code>Projectile</code> of the specified type at the specified
     * location.
     * @param posX
     * @param posY
     * @param type
     */
    public Projectile(float posX, float posY, Projectile.Type type) {
        super(new Bitmap[1], new RectF[1]);

        mType = type;

        switch (mType) {
            case PLAYER:
//                frames = new Bitmap[] {null}; // TODO replace with correct frames
//                hitBoxes = new RectF[] {null}; // TODO replace with correct frames
                speed = 450;
                isFromPlayer(true);
                break;
            case INVADER:
//                frames = new Bitmap[] {null}; // TODO replace with correct frames
//                hitBoxes = new RectF[] {null}; // TODO replace with correct frames
                speed = 450;
                isFromPlayer(false);
                break;
            case PLAYER_SPECIAL:
//                frames = new Bitmap[] {null}; // TODO replace with correct frames
//                hitBoxes = new RectF[] {null}; // TODO replace with correct frames
//                speed = 450; // TODO replace with correct speed
                break;
            case LASER:
//                frames = new Bitmap[] {null}; // TODO replace with correct frames
//                hitBoxes = new RectF[] {null}; // TODO replace with correct frames
                isFromPlayer = false;
                speed = 0;
                break;
            case POWERUP:
//                frames = new Bitmap[] {null}; // TODO replace with correct frames
//                hitBoxes = new RectF[] {null}; // TODO replace with correct frames
                isFromPlayer = true;
                speed = 175;
                break;
        }

        setPosition(posX, posY);
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

    public Projectile.Type getProjectileType() {
        return mType;
    }

    public void isFromPlayer(boolean b) {
        if(mType == Type.PLAYER || mType == Type.INVADER) {
            isFromPlayer = b;
            if (isFromPlayer) movement = Movement.UP;
            else movement = Movement.DOWN;
        }
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
