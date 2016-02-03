package com.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created by Joseph Tompkins on 2/3/2016.
 */
public abstract class Sprite {

    // the sprite's associated image
    protected Bitmap bitmap;

    /**
     * (x, y) makes the top left coordinate of the sprite
     */
    protected float x, y;

    // player hit-box
    protected RectF hitBox;

    // pixel/second speed of the player
    protected float speed;

    // variable tracks current movement of player
    protected Movement movement = Movement.STOPPED;

/******************************************************
* ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
* Memebers
*
* Getters/Setters
* v v v v v v v v v v v v v v v v v v v v v v v v v
*******************************************************/

    public RectF getHitBox() {
        return hitBox;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getLength() {
        return hitBox.left - hitBox.right;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Movement getMovementState() {
        return movement;
    }

    public void setMovementState(Movement movement) {
        this.movement = movement;
    }

/******************************************************
 * ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
 * Getters/Setters
 *
 * Class Methods
 * v v v v v v v v v v v v v v v v v v v v v v v v v
 *******************************************************/

    /**
     * Implement the movement, animation, and other things relative to gameplay here.
     * @param fps
     */
    public void update(long fps) {

    }
}