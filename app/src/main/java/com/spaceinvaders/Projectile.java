package com.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.RectF;

import static com.spaceinvaders.Resources.*;


public class Projectile extends Sprite {
    public enum Type {PLAYER, PLAYER_SPECIAL, INVADER, LASER, POWERUP};

    protected Type mType = Type.PLAYER;
    private boolean isFromPlayer = false;
    private boolean isDestroyed = false;

    public Projectile() {
        super();
    }

    /**
     * Create an instance of <code>Projectile</code>.
     * @param posX <code>float</code> X-position
     * @param posY <code>float</code> Y-position
     * @param isFromPlayer <code>boolean</code>
     */
    @Deprecated
    public Projectile(float posX, float posY, boolean isFromPlayer) {
        // TODO add other projectile images/animations
        super(new Bitmap[] {img_projectile_a},
                new RectF(9.0f * DPIRatio, 9.0f * DPIRatio,
                        14.0f * DPIRatio, 31.0f * DPIRatio));

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
                frames = new Bitmap[] {img_projectile_a};
                hitBoxes = new RectF[] {new RectF(9.0f * DPIRatio, 9.0f * DPIRatio,
                        14.0f * DPIRatio, 31.0f * DPIRatio)};
                movement = Movement.UP;
                speed = 450;
                isFromPlayer(true);
                break;
            case INVADER:
                frames = new Bitmap[] {img_projectile_a};
                hitBoxes = new RectF[] {new RectF(9.0f * DPIRatio, 9.0f * DPIRatio,
                        14.0f * DPIRatio, 31.0f * DPIRatio)};
                movement = Movement.DOWN;
                speed = 450;
                isFromPlayer(false);
                break;
            case PLAYER_SPECIAL:
                frames = new Bitmap[] {
                        null,
                        img_player_laser,
                        img_player_laser};
                hitBoxes = new RectF[] {
                        null,
                        new RectF(10.0f * DPIRatio, 0.0f * DPIRatio,
                        14.0f * DPIRatio, 1500.0f * DPIRatio),
                        null};
                isFromPlayer = true;
                speed = 0;
                break;
            case LASER:
                frames = new Bitmap[] {
                        null,
                        img_invader_laser01,
                        img_invader_laser01};
                hitBoxes = new RectF[] {
                        null,
                        new RectF(8 * DPIRatio, 0.0f, 16 * DPIRatio, 1500 * DPIRatio),
                        null};
                isFromPlayer = false;
                speed = 0;
                return;
            case POWERUP:
                movement = Movement.DOWN;
                speed = 200;
                return;
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
