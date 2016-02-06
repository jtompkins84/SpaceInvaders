package com.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by Joseph Tompkins on 2/3/2016.
 */
public abstract class Sprite {
    private Context context;

    // the sprite's associated image
    protected Bitmap bitmap;
    protected float resolutionRatio = 1.0f;

    /**
     * Array of images to animate. Should be in sequential order of frames.
     */
    protected Bitmap[] animation = null;

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
    /**
     * upper-left origin of hit box relative to the bitmap image. When (0, 0), hit-box
     * will originate from the upper-left-hand corner of the bitmap image.
     */
    protected PointF hitBoxOffset = new PointF(0.0f, 0.0f);
    protected float hitBoxWidth = 0.0f;
    protected float hitBoxHeight = 0.0f;

    // pixel/second speed of the player
    protected float speed;

    // variable tracks current movement of player
    protected Movement movement = Movement.STOPPED;

    private boolean isResizedForResolution = false;

    /**
     * TODO can likely get rid of this later
     * Default empty constructor.
     */
    protected Sprite(Context context) {

    }

    protected Sprite(Context context, Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * <b><u>IMPORTANT</u></b><div></div>
     * Must be called with super() from within a constructor of an inheriting class.
     */
    protected Sprite(Context context, Bitmap bitmap, float hitBoxOffsetX,
                     float hitBoxOffsetY, float hitBoxWidth, float hitBoxHeight) {
        this.bitmap = bitmap;

        initHitBox(hitBoxOffsetX, hitBoxOffsetY,
                hitBoxWidth, hitBoxHeight);
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
        return pos.x + hitBoxOffset.x + (hitBoxWidth / 2);
    }

    /**
     * @return y-coordinate relative to center of hit-box
     */
    public float getY() {
        return pos.y + hitBoxOffset.y + (hitBoxHeight / 2);
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
            pos.set(x - hitBoxOffset.x - (hitBoxWidth / 2),
                    y - hitBoxOffset.y - (hitBoxHeight / 2));

            hitBox.left = pos.x + hitBoxOffset.x;
            hitBox.right = hitBox.left + hitBoxWidth;
            hitBox.top = pos.y + hitBoxOffset.y;
            hitBox.bottom = hitBox.top + hitBoxHeight;
        }
        else {
            pos.set(x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2));
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
            hitBox.left = pos.x + hitBoxOffset.x;
            hitBox.right = hitBox.left + hitBoxWidth;
            hitBox.top = pos.y + hitBoxOffset.y;
            hitBox.bottom = hitBox.top + hitBoxHeight;
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
        canvas.drawBitmap(bitmap, pos.x, pos.y, paint);

        if(hitBox != null && showHitBox == true) {
            int oldColor = paint.getColor();
            paint.setARGB(150, 255, 0, 0);
            canvas.drawRect(hitBox, paint);
            paint.setColor(oldColor);
        }
    }

    /**
     * Resizes the sprite to fit the resolution of the device. Auto-resizes the hit-box, so
     * make sure hit-box is set before calling this method in the <code>Sprite</code>'s
     * constructor.
     */
    @Deprecated
    protected void resizeToResolution() {
        float difRatio = bitmap.getWidth(); // take previous width

        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (bitmap.getWidth() * (resolutionRatio / 1.5f)), (int) (bitmap.getHeight() * (resolutionRatio / 1.5f)), false);
        difRatio = ((float)bitmap.getWidth()) / difRatio;

        hitBoxOffset.x = hitBoxOffset.x * difRatio;
        hitBoxOffset.y = hitBoxOffset.y * difRatio;
        hitBoxWidth = hitBoxWidth * difRatio;
        hitBoxHeight = hitBoxHeight * difRatio;
    }

    private void initHitBox(float offsetX, float offsetY, float width, float height) {
        hitBox = new RectF();

        hitBoxOffset.x = offsetX;
        hitBoxOffset.y = offsetY;
        hitBoxWidth = width;
        hitBoxHeight = height;
    }
}