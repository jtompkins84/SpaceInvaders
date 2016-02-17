package com.spaceinvaders;

import android.graphics.PointF;

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
     * @param spriteImages dimensions of array are: <code>[<i>row</i>][<i>column</i>][<i>animation frames</i>]</code>
     * @return <code>true</code> if <code>spriteImages</code> contained the right number of
     * <code>SpriteImage</code>s.
     */
    public void buildWall(SpriteImage[][][] spriteImages) {
        int length = spriteImages[0].length;
        width = bricksInRow * 35.0f;
        height = bricksInCol * 28.0f;

        // INSTANTIATE BRICKS
        for(int i = 0; i < bricksInCol; i++) {
            for(int j = 0; j < bricksInRow; j++) {
                // create "bricks" in matrix in left-to-right-top-to-bottom order
                bricks[i][j] = new DefenseBrick(spriteImages[i][j]);
            }
        }

        // FIX PIXEL WIDTH AND HEIGHT OF WALL FOR DISPLAY RESOLUTION
        this.width = this.width * bricks[0][0].getDPIRatio();
        this.height = this.height * bricks[0][0].getDPIRatio();
    }

    public void setPos(float x, float y) {
        if(pos == null) { pos = new PointF(); }

        pos.set(x - this.width / 2.0f, y - this.height / 2.0f);

        // PLACE BRICKS
        float brick_width = bricks[0][0].getCurrentFrameSpriteImage().getWidth();
        float brick_height = bricks[0][0].getCurrentFrameSpriteImage().getHeight();
//        Log.v("DefenseWall", "BRICK DIMENSION: " + brick_width + "x" + brick_height); // TODO remove line
//        Log.v("DefenseWall", "WALL POSITION: " + pos.x + "x" + pos.y); // TODO remove line

        for(int i = 0; i < bricksInCol; i++) {
            for(int j = 0; j < bricksInRow; j++) {
                bricks[i][j].setPosition(pos.x + (j * brick_width), pos.y + (i * brick_height));

//                Log.v("DefenseWall", "BRICK POSITION: " + "(" + bricks[i][j].getX() + ", " + bricks[i][j].getY() + ")"); // TODO remove line
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
