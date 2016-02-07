package com.spaceinvaders;

import android.graphics.PointF;
import android.util.Log;

/**
 * Created by Joseph on 2/6/2016.
 */
public class DefenseWall {

    private DefenseBrick[][] bricks;
    private int bricksInRow;
    private int bricksInCol;
    private float width, height;
    private PointF pos = new PointF(0.0f, 0.0f);

    /**
     * Constructs and initializes the wall object.
     * @param spriteImages dimensions of array are: row, column, animation frames
     * @param x x-position of wall
     * @param y y-position of wall
     */
    public DefenseWall(SpriteImage[][][] spriteImages, float x, float y) {
        bricksInRow = 4;
        bricksInCol = 3;

        bricks = new DefenseBrick[bricksInCol][bricksInRow];
        buildWall(spriteImages);

        // setPos must come after buildWall during instantiation, or a NullException will
        // be thrown.
        setPos(x, y);
    }

    /**
     * Initializes <code>DefenseBrick</code>s of this wall by loading <code>spriteImages</code>
     * in order of left-right top-bottom. Fail-safes are built in, in case <code>spriteImages</code>
     * doesn't contain enough images.
     * @param spriteImages dimensions of array are: row, column, animation frames
     * @return <code>true</code> if <code>spriteImages</code> contained the right number of
     * <code>SpriteImage</code>s.
     */
    public boolean buildWall(SpriteImage[][][] spriteImages) {
        int length = spriteImages[0].length;
        width = bricksInRow * 35.0f;
        height = bricksInCol * 28.0f;

        // INSTANTIATE BRICKS
        for(int i = 0; i < bricksInCol; i++) {
            for(int j = 0; j < bricksInRow; j++) {
                bricks[i][j] = new DefenseBrick(spriteImages[i][j]);
            }
        }

        // FIX PIXEL WIDTH AND HEIGHT OF WALL FOR DISPLAY RESOLUTION
        Log.v("DefenseWall", width + "x" + height);
        this.width = this.width * bricks[0][0].getDPIRatio();
        this.height = this.height * bricks[0][0].getDPIRatio();
        Log.v("DefenseWall", width + "x" + height);

        return true;
    }

    public void setPos(float x, float y) {
        if(pos == null) pos = new PointF();
//        pos.set(x - this.width / 2.0f, y - this.height / 2.0f);
        pos.set(x - this.width / 2.0f, y - this.height / 2.0f);
        // PLACE BRICKS
        for(int i = 0; i < bricksInCol; i++) {
            for(int j = 0; j < bricksInRow; j++) {
                bricks[i][j].setPosition(j * 34.5f * bricks[i][j].getDPIRatio() + pos.x, i * 27.5f * bricks[i][j].getDPIRatio() + pos.y);
            }
        }
    }

    /**
     * Returns the array matrix of bricks in this wall.
     * @return
     */
    public DefenseBrick[][] getBricks() {
        return bricks;
    }

    public PointF getDimensions() {
        return  new PointF(width, height);
    }
}
