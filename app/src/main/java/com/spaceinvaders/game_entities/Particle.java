package com.spaceinvaders.game_entities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

/**
 * Created by Joseph on 4/19/2016.
 *
 * <code>Particle</code> is class that represents a playfield entity that doesn't collide or interact
 * with other objects, unlike the
 */
abstract class Particle  {

    /**
     * Array of images to animate. Should be in sequential order of frames.
     */
    protected Bitmap[] frames = null;
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
    protected boolean doAnimate = true;

    /**
     * (x, y) makes the top left coordinate of the sprite
     */
    // The "position" of the Sprite. Should never need to be manipulated directly. Sets itself
    // to the center of the hit-box.
    private PointF pos = new PointF();

    /**
     * Represents the center x, y coordinate of the image. The indexing matches that of the
     * <code>frames</code> array.
     */
    private PointF[] centerOffset;

    /**
     * Instantiates a <code>Particle</code> with one or more frames of animation.
     * @param frames <code>Bitmap[]</code>
     * @param xPos initial position on the x-axis
     * @param yPos initial position on the y-axis
     */
    protected Particle(Bitmap[] frames, float xPos, float yPos) {
        this.frames = frames;

        centerOffset = new PointF[frames.length];

        for(int i = 0; i < frames.length; i++) {
            if(frames[i] != null) {
                centerOffset[i] = new PointF(frames[i].getWidth() / 2.0f, frames[i].getHeight() / 2.0f);
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
     * Set the current frame of animation directly by passing the desired frame's
     * array index as an argument. Invalid indices will result in failure to
     * execute, and an error message in the log.
     * @param index <code>int</code> - the index of the frame to select.
     */
    public void setCurrFrame(int index) {
        if(index < 0 || index >= this.frames.length) {
            Log.e("Particle.setCurrFrame(" + index + ")", "Not a valid frame index.");
            return;
        }

        currFrame = index;

        if(startFrame == endFrame || index < startFrame || index > endFrame) {
            startFrame = index;
            endFrame = index;
        }
    }

    /**
     *
     * @return Returns the array index value of the current frame of animation.
     */
    public int getCurrFrame() {
        return currFrame;
    }

    /**
     * Sets the frame image of the <code>frames</code> array at the <code>index</code>.
     * @param index must be valid index of <code>frames</code> array of this <code>Sprite</code>
     * @param image
     */
    protected void setFrameImage(int index, Bitmap image) {
        if(index < 0 || index >= this.frames.length) {
            Log.e("Sprite.setFrameImage(" + index + ")", "Not a valid frame index.");
            return;
        }

        frames[index] = image;

        return;
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
        this.currFrame = startIndex;
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
     * @return x-coordinate relative to center of this <code>Sprite</code>'s bitmap.
     */
    public float getX() {
        if(centerOffset[currFrame] != null) {
            return pos.x - centerOffset[currFrame].x;
        }
        else return 0.0f;
    }

    /**
     * This returns the raw x coordinate in pixels. Most likely
     * the left most x position of this sprite's current frame
     * @return the raw x coordinate in pixels
     */
    public float getRawX() {
        return pos.x;
    }

    /**
     * The pixel difference between the x-edge and the center of the current frame's bitmap image.
     * @return
     */
    public float getCenterOffsetX() {
        if(centerOffset[currFrame] != null) {
            return centerOffset[currFrame].x;
        }
        else return 0.0f;
    }

    /**
     * @return y-coordinate relative to center of this <code>Sprite</code>'s bitmap.
     */
    public float getY() {
        if(centerOffset[currFrame] != null) {
            return pos.y - centerOffset[currFrame].y;
        }
        return 0.0f;
    }

    /**
     * This returns the raw y coordinate in pixels. Most likely
     * the top most y position of this sprite's current frame
     * @return the raw y coordinate in pixels
     */
    public float getRawY() {
        return pos.y;
    }

    /**
     * The pixel difference between the x-edge and the center of the current frame's bitmap image.
     * @return
     */
    public float getCenterOffsetY() {
        if(centerOffset[currFrame] != null) {
            return centerOffset[currFrame].y;
        }
        else return 0.0f;
    }

    /**
     * Sets the position of the <code>Sprite</code>. Position is in pixels
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {
        if(centerOffset[currFrame] != null) {
            pos.set(x - centerOffset[currFrame].x,
                    y - centerOffset[currFrame].y);
        }
    }

    public void setPosX(float x) {
        setPosition(x , pos.y);
    }

    public void setPosY(float y) {
        setPosition(pos.x, y);
    }

    /**
     * Set position relative to previous position. The <code>Sprite</code> is moved
     * from its current position by the specified amount.
     * @param dx <code>float</code> - the amount to move the <code>Sprite</code> along the X-axis
     * @param dy <code>float</code> - the amount to move the <code>Sprite</code> along the Y-axis
     */
    public void move(float dx, float dy) {
        pos.set(pos.x + dx, pos.y + dy);
    }

    /**
     * Implement the movement, animation, and other things relative to gameplay here.
     * @param fps
     */
    abstract public void update(long fps);

    /**
     * Draws the particle.
     * @param canvas <code>Canvas</code> object to draw to
     * @param paint <code>Paint</code> object used to draw
     */
    public void draw(Canvas canvas, Paint paint) {
        if (frames[currFrame] != null && doDrawFrame) {

            canvas.drawBitmap(frames[currFrame], pos.x, pos.y, paint); // does draw to canvas

            // when doAnimate is true, the animation progresses frame to next frame.
            if(doAnimate) {
                if (frameSkipCount >= skipFrames) {
                    nextFrame();
                    frameSkipCount = 0;
                }
                else frameSkipCount++;
            }
        }
    }
}
