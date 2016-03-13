package com.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.sql.Time;

/**
 * Created by Joseph Tompkins on 2/3/2016.
 */
public abstract class Sprite {

    /**
     * Array of images to animate. Should be in sequential order of frames.
     */
    private Bitmap[] frames = null;
    // index of the current frame of animation
    private int currFrame = 0;
    /**
     * The index of the frame array to start an animation. 0 by default.
     */
    private int startFrame = 0;
    /**
     * The index of the frame array to end an animation. 0 by default.
     */
    private int endFrame = 0;
    /**
     * total number of frames to skip in between animation frames.
     */
    private short skipFrames = 0;
    /**
     * number of frames skipped since last frame not skipped.
     */
    private short frameSkipCount = 0;
    /**
     * The animation will loop between the <code>startFrame</code> index and <code>endFrame</code>
     * index of this <code>Sprite</code> object when this value is set to <code>true</code>.
     */
    protected boolean doAnimationLoop = true;
    /**
     * dictates whether or not to draw the frame when the draw method of this
     * <code>Sprite</code> object is called. Useful to keep "dead" sprites from
     * being rendered on the screen.
     */
    protected boolean doDrawFrame = true;

    /**
     * (x, y) makes the top left coordinate of the sprite
     */
    // The "position" of the Sprite. Should never need to be manipulated directly. Sets itself
    // to the center of the hit-box.
    private PointF pos = new PointF();

    // player hit-box
    private RectF[] hitBoxes = null;
    protected boolean isCollisionDetected = false;
    /**
     * Just a flag to reference whether or not to do hit detection.
     */
    protected boolean doHitDetection = true;

    // pixel/second speed of the player
    protected float speed;

    // variable tracks current movement of player
    protected Movement movement = Movement.STOPPED;

    protected Sprite(Bitmap image) {
        this.frames[currFrame] = image;
    }

    protected Sprite(Bitmap[] frames) {
        this.frames = frames;
    }

    /**
     * Instantiates a <code>Sprite</code> with a single frame and a single hit-box.
     * @param image <code>Bitmap</code>
     * @param hitBox <code>RectF</code>
     */
    protected Sprite(Bitmap image, RectF hitBox) {
        frames = new Bitmap[1];
        hitBoxes = new RectF[1];
        this.frames[0] = image;
        this.hitBoxes[0] = hitBox;
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
     * @param frames <code>Bitmap[]</code>
     * @param hitBoxes <code>RectF[]</code>
     */
    protected Sprite(Bitmap[] frames, RectF[] hitBoxes) {
        this.frames = frames;

        // makes this.hitBoxes array the same length as this.frames array to insure that
        // the argument hitBoxes array indices match up in a relevant way to the frames.
        this.hitBoxes = new RectF[this.frames.length];

        // Filling the hitBoxes array with relevant values.
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
     * @param frames <code>Bitmap[]</code>
     * @param hitBox <code>RectF</code>
     */
    protected Sprite(Bitmap[] frames, RectF hitBox) {
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
* Memebers & Constructors
*
* Getters/Setters
* v v v v v v v v v v v v v v v v v v v v v v v v v
*******************************************************/

    /**
     *
     * @return the <code>Bitmap</code> associated with the current frame of animation.
     */
    public Bitmap getCurrentFrameSpriteBitmap() {
        return frames[currFrame];
    }

    /**
     * This returns the index number of the current frame in the
     * @return <code>int</code> representing the index of the current frame in <code>frames[]</code>.
     */
    public int getCurrFrameIndex() {
        return currFrame;
    }

    /**
     * Set the current frame of animation directly by passing the desired frame's
     * array index as an argument. Invalid indices will result in failure to
     * execute, and an error message in the log.
     * @param index <code>int</code> - the index of the frame to select.
     */
    public void setCurrFrame(int index) {
        if(index < 0 || index >= this.frames.length) {
            Log.e("Sprite.setCurrFrame(" + index + ")", "Not a valid frame index.");
            return;
        }

        currFrame = index;

        if(startFrame == endFrame || index < startFrame || index > endFrame) {
            startFrame = index;
            endFrame = index;
        }
    }

    /**
     * Returns the index of the starting frame for the current animation.
     * @return
     */
    public int getStartFrame() {
        return this.startFrame;
    }

    /**
     * Returns the index of the ending frame for the current animation.
     * @return
     */
    public int getEndFrame() {
        return this.endFrame;
    }

    /**
     * Set the animation
     * @param startIndex <code>int</code> - must be valid index.
     * @param endIndex <code>int</code> - must be valid index and greater than or equal to
     *                 <code>startIndex</code>
     */
    public void setStartAndEndFrames(int startIndex, int endIndex) {
        if(startIndex < 0 || startIndex >= this.frames.length) {
            Log.e("Sprite",
                    "setStartAndEndFrames(" + startIndex + ", " +  endIndex + "):"
                            + " Not a valid starting index.");
            return;
        }
        else if(endIndex < 0 || endIndex >= this.frames.length) {
            Log.e("Sprite",
                    "setStartAndEndFrames(" + startIndex + ", " +  endIndex + "):"
                            + " Not a valid starting index.");
            return;
        }
        else if(endIndex < startIndex) {
            Log.e("Sprite",
                    "setStartAndEndFrames(" + startIndex + ", " + endIndex + "):"
                            + " Ending index for a frame must be either equal to or greater than the starting frame index.");
            return;
        }

        this.startFrame = startIndex;
        this.endFrame = endIndex;
    }

    /**
     * Advances to the next frame of animation. If the <code>doAnimationLoop</code> value of this
     * <code>Sprite</code> object is true and the current frame index is equal to the value of
     * <code>endFrame</code>, the animation will be made to loop by setting the current frame
     * the index value of the starting frame.
     * <div></div>
     * If <code>doAnimationLoop</code> is false, this will advance to the next frame only if
     * the current frame is less than the ending frame.
     * <code>frames</code>, the animation will loop back to <code>endFrame</code>.
     */
    public void nextFrame() {
        if(doAnimationLoop && currFrame >= endFrame)
            currFrame = startFrame;
        else if(currFrame < endFrame)
            currFrame++;
    }

    /**
     * Sets the number of frames skipped between animation frames.
     */
    public void setSkipFrames(short skipFrames) {
        this.skipFrames = skipFrames;
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
     * @return x-coordinate relative to center of this <code>Sprite</code>'s bitmap.
     */
    public float getX() {
        if(hitBoxes == null || hitBoxes[currFrame] == null) {
            float width = frames[currFrame].getWidth();
            return (pos.x + (width / 2));
        }

        return hitBoxes[currFrame].left
                + ((hitBoxes[currFrame].right - hitBoxes[currFrame].left) / 2);
    }

    /**
     * @return y-coordinate relative to center of this <code>Sprite</code>'s bitmap.
     */
    public float getY() {
        if(hitBoxes == null || hitBoxes[currFrame] == null) {
            float height = frames[currFrame].getHeight();
            return (pos.y + (height / 2));
        }

        return hitBoxes[currFrame].top
                + ((hitBoxes[currFrame].bottom - hitBoxes[currFrame].top) / 2);
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
     * Sets the position of the <code>Sprite</code>. Position is in pixels
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {
        pos.set(x - ((float) frames[currFrame].getWidth() / 2.0f),
                y - ((float)frames[currFrame].getHeight() / 2.0f));

//        Log.v("Sprite", "Sprite position: (" + pos.x + ", " + pos.y + ")"); // TODO REMOVE DEBUG LINE

        if(hitBoxes != null || hitBoxes[currFrame] != null)
            // offset hitbox by position of sprite plus offset of hitbox relative to the sprite
            hitBoxes[currFrame].offsetTo(pos.x + hitBoxes[currFrame].left, pos.y + hitBoxes[currFrame].top);
    }

    public boolean isCollisionDetected() {
        return isCollisionDetected;
    }

    public void setCollisionDetected(boolean isCollisionDetected) {
        this.isCollisionDetected = isCollisionDetected;
    }

/******************************************************
 * ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
 * Getters/Setters
 *
 * Inherited Methods
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
        if (frames[currFrame] != null && doDrawFrame) {
            canvas.drawBitmap(frames[currFrame], pos.x, pos.y, paint);
            if(frameSkipCount >= skipFrames) {
                nextFrame();
                frameSkipCount = 0;
            }
            else frameSkipCount++;
        }

        if ((hitBoxes != null || hitBoxes[currFrame] != null) && showHitBox == true) {
            int oldColor = paint.getColor();
            paint.setARGB(150, 255, 0, 0);
            canvas.drawRect(hitBoxes[currFrame], paint);
            paint.setColor(oldColor);
        }
    }

    /**
     * Set position relative to previous position. The <code>Sprite</code> is moved
     * from its current position by the specified amount.
     * @param dx <code>float</code> - the amount to move the <code>Sprite</code> along the X-axis
     * @param dy <code>float</code> - the amount to move the <code>Sprite</code> along the Y-axis
     */
    public void move(float dx, float dy) {
        pos.set(pos.x + dx, pos.y + dy);
        if(hitBoxes != null || hitBoxes[currFrame] != null) {
            hitBoxes[currFrame].offset(dx, dy);
        }
    }

    /**
     * TODO --- IF UNUSED, REMOVE THIS METHOD ---
     * Meant to be overridden by the child class.
     * @param collidedWith the <code>Sprite</code> object that collided with this one.
     */
    public void doCollision(Sprite collidedWith) {
        // do action based on collision here.
    }

/******************************************************
 * ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
 * Inherited Methods
 *
 * Private Methods
 * v v v v v v v v v v v v v v v v v v v v v v v v v
 *******************************************************/

    private void initHitBox(RectF hitBox) {
        if (hitBox == null) return;
        float DPICorrectionRatio = Resources.DPIRatio;

//        hitBox.right = hitBox.right * DPICorrectionRatio;
//        hitBox.bottom = hitBox.bottom * DPICorrectionRatio;
    }
}
