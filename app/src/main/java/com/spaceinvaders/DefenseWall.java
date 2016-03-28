package com.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    private PointF center = new PointF();

    /**
     * Constructs and initializes the wall object.
     * @param x x-position of wall
     * @param y y-position of wall
     */
    public DefenseWall(float x, float y) {
        Bitmap[][][] bricksInWall = new Bitmap[3][4][4];
        bricksInWall[0] = new Bitmap[][] {Resources.brick_aa, Resources.brick, Resources.brick, Resources.brick_ad};
        bricksInWall[1] = new Bitmap[][] {Resources.brick, Resources.brick, Resources.brick, Resources.brick};
        bricksInWall[2] = new Bitmap[][] {Resources.brick, Resources.brick_cb, Resources.brick_cc, Resources.brick};

        bricksInRow = 4;
        bricksInCol = 3;

        bricks = new DefenseBrick[bricksInCol][bricksInRow];
        buildWall(bricksInWall);

        // setPos must come after buildWall during instantiation, or a NullException will
        // be thrown.
        setPos(x, y);
        center.set(pos.x + (width / 2), pos.y + (height / 2));
    }

    /**
     * Initializes <code>DefenseBrick</code>s of this wall by loading <code>images</code>
     * in order of left-right top-bottom. Fail-safes are built in, in case <code>images</code>
     * doesn't contain enough images.
     * @param images dimensions of array are: <code>[<i>row</i>][<i>column</i>][<i>animation frames</i>]</code>
     * @return <code>true</code> if <code>images</code> contained the right number of
     * <code>Bitmap</code>s.
     */
    public void buildWall(Bitmap[][][] images) {
        int length = images[0].length;
        width = bricksInRow * Resources.img_brick_01.getWidth();
        height = bricksInCol * Resources.img_brick_01.getHeight();

        // INSTANTIATE BRICKS
        for(int i = 0; i < bricksInCol; i++) {
            for(int j = 0; j < bricksInRow; j++) {
                // create "bricks" in matrix in left-to-right-top-to-bottom order
                bricks[i][j] = new DefenseBrick(images[i][j]);
            }
        }

        // FIX PIXEL WIDTH AND HEIGHT OF WALL FOR DISPLAY RESOLUTION
//        this.width = this.width * Resources.DPIRatio;
//        this.height = this.height * Resources.DPIRatio;
    }

    public void setPos(float x, float y) {
        if(pos == null) { pos = new PointF(); }

        pos.set(x - this.width / 2.0f, y - this.height / 2.0f);

        // PLACE BRICKS
        float brick_width = bricks[0][0].getCurrentFrameSpriteBitmap().getWidth();
        float brick_height = bricks[0][0].getCurrentFrameSpriteBitmap().getHeight();

        for(int i = 0; i < bricksInCol; i++) {
            for(int j = 0; j < bricksInRow; j++) {
                bricks[i][j].setPosition(pos.x + (j * (brick_width)), pos.y + (i * brick_height));
            }
        }
    }

    public void update(long fps) {
        for(int i = 0; i < bricksInCol; i++) {
            for(int j = 0; j < bricksInRow; j++) {
                if(bricks[i][j] != null)
                    bricks[i][j].update(fps);
            }
        }
    }

    public void draw(Canvas canvas, Paint paint, boolean showHitbox) {
        for(int i = 0; i < bricksInCol; i++) {
            for(int j = 0; j < bricksInRow; j++) {
                if(bricks[i][j] != null)
                    bricks[i][j].draw(canvas, paint, showHitbox);
            }
        }
    }

    public void doCollisions(Sprite sprite) {
        if(sprite != null && withinProximity(sprite) ) {
            for (int i = 0; i < bricksInCol; i++) {
                for(int j = 0; j < bricksInRow; j++) {
                    if(bricks[i][j] != null) {
                        if(bricks[i][j].getHitBox().intersect(sprite.getHitBox()) == true) {
                            if (sprite.getClass() == Projectile.class){
                                // if brick health is at or below 1, destroy brick
                                if (bricks[i][j].getHealth() <= 1) bricks[i][j] = null;
                                    // otherwise, damage brick
                                else bricks[i][j].takeDamage();
                                ((Projectile) sprite).isDestroyed(true);
                                // return so that only one block can be damaged per projectile
                                return;
                            }
                            // if anything other than a projectile hits a brick, destroy brick.
                            else {
                                bricks[i][j] = null;
                            }
                        }

                    }
                }
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

    private boolean withinProximity(Sprite sprite) {
        if(Math.abs(sprite.getX() - center.x) < (width * 1.0f) && Math.abs(sprite.getY() - center.y) < (height * 1.0f))
            return true;

        return false;
    }
}
