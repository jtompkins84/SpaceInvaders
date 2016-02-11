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

    /**
     * Array of images to animate. Should be in sequential order of frames.
     */
    private SpriteImage[] frames = null;
    // index of the current frame of animation
    private int currFrame = 0;
    // A ratio that represents the amount of <DPI of device> / <DPI of original image>.
    //
    private float resolutionRatio = 1.0f;


    /**
     * (x, y) makes the top left coordinate of the sprite
     */
    // The "position" of the Sprite. Should never need to be manipulated directly. Sets itself
    // to the center of the hit-box.
    private PointF pos = new PointF();

    // player hit-box
    private RectF[] hitBoxes = null;

    // pixel/second speed of the player
    protected float speed;

    // variable tracks current movement of player
    protected Movement movement = Movement.STOPPED;

    private boolean isResizedForResolution = false;

    protected Sprite(SpriteImage image) {
        this.frames[currFrame] = image;
    }

    protected Sprite(SpriteImage[] frames) {
        this.frames = frames;
    }

    /**
     * Instantiates a <code>Sprite</code> with a single frame and a single hit-box.
     * @param image <code>SpriteImage</code>
     * @param hitBox <code>RectF</code>
     */
    protected Sprite(SpriteImage image, RectF hitBox) {
        frames = new SpriteImage[1];
        hitBoxes = new RectF[1];
        this.frames[currFrame] = image;
        this.hitBoxes[currFrame] = hitBox;
        if(hitBox != null)
            initHitBox(this.hitBoxes[currFrame]);
    }

    /**
     * Instantiates a <code>Sprite</code> with multiple frames of animation. <code>hitBoxes</code>
     * must be an array of length less-than or equal to the length of <code>frames</code>. Each
     * hit-box at an index of <code>hitBoxes</code> will correspond with the frame at the same
     * index of <code>frames</code>. If <code>hitBoxes</code> is of a length less than
     * <code>frames</code>, then all indices greater than <code>hitBoxes</code>'s length are
     * set to <code>null</code>. This essentially means that any frames without hit-boxes
     * associated with them do not interact with other games entities/sprites.
     * @param frames <code>SpriteImage[]</code>
     * @param hitBoxes <code>RectF[]</code>
     */
    protected Sprite(SpriteImage[] frames, RectF[] hitBoxes) {
        this.frames = frames;

        this.hitBoxes = new RectF[this.frames.length];

        for(int i = 0; i < this.frames.length; i++) {
            if(i < hitBoxes.length) this.hitBoxes[i] = hitBoxes[i];
            else this.hitBoxes[i] = null;
        }

        // Initialize hit-boxes
        if(this.hitBoxes != null) {
            for(RectF hb : this.hitBoxes) {
                initHitBox(hb);
            }
        }
    }

    /**
     * Instantiates a <code>Sprite</code> with multiple frames of animation. The
     * hit-box will correspond with all the frames of the <code>frames</code> array.
     * @param frames <code>SpriteImage[]</code>
     * @param hitBox <code>RectF</code>
     */
    protected Sprite(SpriteImage[] frames, RectF hitBox) {
        this.frames = frames;

        if(hitBox != null) {
            this.hitBoxes = new RectF[this.frames.length];

            // set all hit-boxes in the array equal to 'hitBox' argument
            for(int i = 0; i < this.frames.length; i++) {
                hitBoxes[i] = hitBox;
            }

            // Initialize hit-boxes
            for(RectF hb : this.hitBoxes) {
                initHitBox(hb);
            }
        }
    }

/******************************************************
* ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
* Memebers & Super Constructors
*
* Getters/Setters
* v v v v v v v v v v v v v v v v v v v v v v v v v
*******************************************************/

    /**
     *
     * @return the <code>SpriteImage</code> associated with the current frame of animation.
     */
    public SpriteImage getCurrentFrameImage() {
        return frames[currFrame];
    }

    /**
     *
     * @return <code>int</code> representing the index of the current frame in <code>frames[]</code>.
     */
    public int getCurrFrame() {
        return currFrame;
    }

    /**
     *
     * @param frame <code>int</code> - the index of the frame to select.
     */
    public void setCurrFrame(int frame) {
        if(frame < 0 || frame >= this.frames.length){
            Log.e("setCurrFrame(" + frame + ")", "Not a valid frame.");
            return;
        }

        currFrame = frame;
    }

    /**
     * Advances to the next frame of animation. If the current frame is indexed at the end of
     * <code>frames</code>,
     */
    public void advanceFrames() {
        if(currFrame < frames.length - 1) currFrame++;
        else if(currFrame >= frames.length) currFrame = 0;
    }

    /**
     * Returns the hit-box associated with the current frame of animation. If there is no
     * hit-box associated with the frame,
     * @return
     */
    public RectF getHitBox() {
        if(hitBoxes == null) return null;
        if(currFrame < hitBoxes.length && hitBoxes[currFrame] != null) {
            return hitBoxes[currFrame];
        }

        return null;
    }


    /**
     * @return x-coordinate relative to center of hit-box
     */
    public float getX() {
        if(hitBoxes == null || hitBoxes[currFrame] == null) {
            float ratio = frames[currFrame].getDPIRatio();
            float width = frames[currFrame].getBitmap().getWidth();
            return (pos.x + (width * ratio) / 2);
        }

        return pos.x + hitBoxes[currFrame].left
                + (hitBoxes[currFrame].right - hitBoxes[currFrame].left / 2);
    }

    /**
     * @return y-coordinate relative to center of hit-box
     */
    public float getY() {
        if(hitBoxes == null || hitBoxes[currFrame] == null) {
            float ratio = frames[currFrame].getDPIRatio();
            // TODO resolve after SpriteImage is fixed to use .getScaledWidth()
//            float width = frames[currFrame].getBitmap().getScaledWidth();
//            return (pos.x + (width * ratio) / 2);
        }

        return pos.y + hitBoxes[currFrame].top
                + (hitBoxes[currFrame].bottom - hitBoxes[currFrame].top / 2);
    }

    public float getHitBoxLength() {
        if(hitBoxes == null || hitBoxes[currFrame] == null) {
            return 0.0f;
        }
        return hitBoxes[currFrame].left - hitBoxes[currFrame].right;
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
        if(hitBoxes != null || hitBoxes[currFrame] != null) {
            pos.set(x - hitBoxes[currFrame].left - ((hitBoxes[currFrame].right - hitBoxes[currFrame].left) / 2),
                    y - hitBoxes[currFrame].top - ((hitBoxes[currFrame].bottom - hitBoxes[currFrame].top) / 2));
            hitBoxes[currFrame].offsetTo(pos.x + hitBoxes[currFrame].left, pos.y + hitBoxes[currFrame].top);

            Log.v("hitBox MOVED",
                    (hitBoxes[currFrame].right - hitBoxes[currFrame].left)
                    + "x"
                    + (hitBoxes[currFrame].bottom - hitBoxes[currFrame].top));

            return;
        }

        float ratio = frames[currFrame].getDPIRatio();
        float width = frames[currFrame].getBitmap().getWidth();
        pos.set(x - ((frames[currFrame].getBitmap().getWidth() * ratio) / 2),
                y - ((frames[currFrame].getBitmap().getHeight() * ratio) / 2));
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
        canvas.drawBitmap(frames[currFrame].getBitmap(), pos.x, pos.y, paint);

        if(hitBoxes != null || hitBoxes[currFrame] != null && showHitBox == true) {
            int oldColor = paint.getColor();
            paint.setARGB(150, 255, 0, 0);
            canvas.drawRect(hitBoxes[currFrame], paint);
            paint.setColor(oldColor);
        }
    }

    private void initHitBox(RectF hitBox) {
        if (hitBox == null) return;
        float DPICorrectionRatio = frames[currFrame].getDPIRatio();
//        DPICorrectionRatio = (float)frames[0].getBitmap().getScaledWidth(640) / (float)frames[0].getBitmap().getWidth(); // TODO remove line

        float w = frames[0].getBitmap().getWidth();
        float h = frames[0].getBitmap().getHeight();

        hitBox.left = hitBox.left * DPICorrectionRatio;
        hitBox.top = hitBox.top * DPICorrectionRatio;
        hitBox.right = hitBox.right * DPICorrectionRatio;
        hitBox.bottom = hitBox.bottom * DPICorrectionRatio;
//        hitBox.right = hitBox.left + w;
//        hitBox.bottom = hitBox.top + h;
        Log.v("SPRITE DIMENSION", w + "x" + h); // TODO remove line
    }

    /**
     * Set position relative to previous position.
     * @param dx
     * @param dy
     */
    protected void move(float dx, float dy) {
        pos.set(pos.x + dx, pos.y + dy);
        if(hitBoxes != null || hitBoxes[currFrame] != null) {
            hitBoxes[currFrame].offset(dx, dy);
        }
    }
}
