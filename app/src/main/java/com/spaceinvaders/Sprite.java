package com.spaceinvaders;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by Joseph Tompkins on 2/3/2016.
 */
public abstract class Sprite {

    // the sprite's associated image
    protected SpriteImage image;
    protected float resolutionRatio = 1.0f;

    /**
     * Array of images to animate. Should be in sequential order of frames.
     */
    protected SpriteImage[] animation = null;

    //TODO make setter method for animDelay. sets values less than 0 equal to 0.
    /**
     * Delay multiplier for number of game frames to skip before next animation frame update. Must
     * be >= 0.
     */
    private short animDelay = 0;

    /**
     * (x, y) makes the top left coordinate of the sprite
     */
    protected float x, y;
    // The "position" of the Sprite. Should never need to be manipulated directly. Sets itself
    // to the center of the hit-box.
    private PointF pos = new PointF();

    // player hit-box
    protected RectF hitBox = null;

    // pixel/second speed of the player
    protected float speed;

    // variable tracks current movement of player
    protected Movement movement = Movement.STOPPED;

    private boolean isResizedForResolution = false;

    /**
     * TODO can likely get rid of this later
     * Default empty constructor.
     */
    protected Sprite() {
    }

    protected Sprite(SpriteImage image) {
        this.image = image;
    }

    protected Sprite(SpriteImage[] animation) {
        this.animation = animation;
    }

    /**
     *
     */
    protected Sprite(SpriteImage image, RectF hitBox) {
        this.image = image;
        this.hitBox = hitBox;
        initHitBox();
    }

    protected Sprite(SpriteImage[] animation, RectF[] hitBoxes) {
        this.animation = animation;

        initHitBox();
    }

/******************************************************
* ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
* Memebers & Super Constructors
*
* Getters/Setters
* v v v v v v v v v v v v v v v v v v v v v v v v v
*******************************************************/

    public RectF getHitBox() {
        return hitBox;
    }

    /**
     * @return x-coordinate relative to center of hit-box
     */
    public float getX() {
        return pos.x + hitBox.left + (hitBox.right - hitBox.left / 2);
    }

    /**
     * @return y-coordinate relative to center of hit-box
     */
    public float getY() {
        return pos.y + hitBox.top + (hitBox.bottom - hitBox.top / 2);
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

    /**
     * Sets the position of the <code>Sprite</code>.
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {
        if(hitBox != null) {
            pos.set(x - hitBox.left - ((hitBox.right - hitBox.left) / 2),
                    y - hitBox.top - ((hitBox.bottom - hitBox.top) / 2));
            hitBox.offsetTo(pos.x + hitBox.left, pos.y + hitBox.top);
//            Log.v("hitBox MOVED", " left = " + hitBox.left + " top = " + hitBox.top + " right = " + hitBox.right + " bottom = " + hitBox.bottom);
        }
        else {
            pos.set(x - (image.getBitmap().getWidth() / 2), y - (image.getBitmap().getHeight() / 2));
        }
    }

    /**
     * Set position relative to previous position.
     * @param dx
     * @param dy
     */
    protected void move(float dx, float dy) {
        pos.set(pos.x + dx, pos.y + dy);
        if(hitBox != null) {
            hitBox.offset(dx, dy);
        }
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
    abstract public void update(long fps);

    /**
     * Draws the sprite.
     * @param canvas <code>Canvas</code> object to draw to
     * @param paint <code>Paint</code> object used to draw
     */
    public void draw(Canvas canvas, Paint paint, boolean showHitBox) {
        canvas.drawBitmap(image.getBitmap(), pos.x, pos.y, paint);

        if(hitBox != null && showHitBox == true) {
            int oldColor = paint.getColor();
            paint.setARGB(150, 255, 0, 0);
            canvas.drawRect(hitBox, paint);
            paint.setColor(oldColor);
        }
    }

    private void initHitBox() {
        float DPICorrectionRatio = image.getDPIRatio();
        Log.v("DIP Ratio", " = " + image.getDPIRatio());

//        hitBox = new RectF(hitBox.left * DPICorrectionRatio, hitBox.top * DPICorrectionRatio,
//                hitBox.right * DPICorrectionRatio, hitBox.bottom * DPICorrectionRatio);

        Log.v("BEFORE", " left = " + hitBox.left + " top = " + hitBox.top + " right = " + hitBox.right + " bottom = " + hitBox.bottom);
        hitBox.left = hitBox.left * DPICorrectionRatio;
        hitBox.top = hitBox.top * DPICorrectionRatio;
        Log.v("MID", " left = " + hitBox.left + " top = " + hitBox.top + " right = " + hitBox.right + " bottom = " + hitBox.bottom);
        hitBox.right = hitBox.right * DPICorrectionRatio;
        hitBox.bottom = hitBox.bottom * DPICorrectionRatio;
        Log.v("AFTER", " left = " + hitBox.left + " top = " + hitBox.top + " right = " + hitBox.right + " bottom = " + hitBox.bottom);
    }
}