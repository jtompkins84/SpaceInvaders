package com.spaceinvaders;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

public class InvaderArmy {
    private Invader[][] invaders;
    private ProjectileArray projectiles;
    private PlayFieldView gameThread;
    /**
     * armyPos is top-left most corner position of invader army.
     */
    private PointF armyPos;
    private PointF armyCenter;
    private Movement armyMovement = Movement.LEFT;
    private float armyWidth, armyHeight, invaderWidth, invaderHeight;
    private float invaderXMovementIncrement;
    private float invaderYMovementIncrement;
    private int rows = 6;
    private int cols = 5;
    private int counter = 0;//counter needed to help catch boundaries for the invaderArmy
    /**
     * spacing in between each invader.
     */
    private float xSpacing, ySpacing;
    private float playFieldWidth, playFieldHeight;

    /**
     * number of invaders left in the army.
     */
    private int invadersLeft = 0;

    private long lastMoveTime = System.currentTimeMillis();
    private long timeBetweenMoves = 1500l;

    public InvaderArmy(float playFieldWidth, float playFieldHeight, ProjectileArray projectiles, PlayFieldView playFieldView) {
        this.playFieldWidth = playFieldWidth;
        this.playFieldHeight = playFieldHeight;
        this.projectiles = projectiles;
        this.gameThread = playFieldView;

        invaders = new Invader[rows][cols];
        invadersLeft = rows * cols;
        xSpacing = Resources.img_invader_a01.getWidth()* 1.125f; // the amount to horizontally space invaders apart from each other
        ySpacing = Resources.img_invader_a01.getHeight(); // the amount to vertical space invaders apart from each other

        buildArmy();
    }

    public void update(long fps) {
        /*
            This is where AI is implemented in a nested loop.
            The things the army needs to do:

            - check if invaders[i][j] is null, if not:

                - check invaders[i][j].isDead(), if it is, set that invader to null. This
                        should be first perhaps, but do a random chance for power-up drop, invaders[i][j].dropPowerup,
                        and then do a "break;" to skip this iteration over the loop.

                        Also, 'invadersLeft--' (increment this value down)

                - check if army has gotten too close to the edge of a boundary on the screen.

                        If it has, move down once OR If 'armyMovement' is
                        already equal to Movement.Down, change it to move away from whatever edge
                        its close to.
                        It might be useful to set new private
                        class attributes to track if it should move LEFT or RIGHT next, after
                        it moves down.

                        Set attribute armyMovement to new correct value.

                        Use invaders[i][j].setMovementState( armyMovement ) to change the
                        direction of movement for each invader.

                        Leave the actual movement of the invader to the call to invaders[i][j].update( fps )
                        at the end of this loop.

                - do invader fire projectiles. This is where you'll need to implement the random
                        chance for a projectile to fire. Add whatever class attributes you think
                        are necessary.

                        Pass the "projectiles" class attribute
                        to invaders[i][j].fire( projectiles ) when an invader is supposed to fire.

                - At the end, just run invaders[i][j].update( fps ), and that takes care of the
                        actual movement.
         */

        boolean doMove = false;
        // This is here to update the time that the invaders last moved and the position
        // of the army. Needs to happen after updating every invader in the army.
        if(System.currentTimeMillis() - lastMoveTime >= timeBetweenMoves) {
            lastMoveTime = System.currentTimeMillis();
            doMove = true;
        }
        // FEEL FREE TO RE-WRITE THIS CODE.
        // This is just here to test a few things.
        // it still needs to implement boundary detection, as mentioned above, as well as
        // projectile firing, etc.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (invaders[i][j] != null) {

                    if(doMove) {
                        // inside here, do detection of boundaries

                        // this is only back and for code. you will have to come up with a way
                        // make them move down once at the edge

                        // detection for right boundary
                        if( (armyPos.x + armyWidth)  > (playFieldWidth - invaderWidth) ){
                            armyMovement = Movement.LEFT; // for each if statement, make sure to change
                                                          // this to the correct movement.
                                                          // updateArmyPosition uses this value.
                        }
                        // detection for left boundary
                        else if(armyPos.x < invaderWidth) {
                                armyMovement = Movement.RIGHT;
                        }

                        // The armyMovement now determines the movement state of the invaders
                        invaders[i][j].setMovementState(armyMovement);
                    }

                    // This has to be here in this order so that the invaders move.
                    // Write it after the movement is changed to stop STOPPED, and nothing happens.
                    invaders[i][j].update(fps);

                    // This has to be here to keep the invaders from moving more than they are meant to.
                    invaders[i][j].setMovementState(Movement.STOPPED);
                }
            }
        }

        // This has to be here to keep track of the army, otherwise
        // hit detection will get all screwy when the army starts
        // to move down.
        if(doMove) {
            updateArmyPosition();
        }
        /**
         * Also, you might make a class attribute boolean value to indicate that the invader army has reached
         * the bottom of the playfield. At some point, in order to end the game, the game loop
         * will have to check if the invader army has reached the bottom, because if any
         * invaders reach the bottom, the round is over.
         *
         * If you choose to go this route, then make a public method to return this boolean value.
         */
    }

    public void draw(Canvas canvas, Paint paint, boolean showHitBox) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (invaders[i][j] != null) {
                    invaders[i][j].draw(canvas, paint, showHitBox);
                }
            }
        }
    }

    public int getInvadersLeft() {
        return invadersLeft;
    }

    private void buildArmy() {
        float xCoord = playFieldWidth - (xSpacing * 5.0f); // left most coordinate of the invader army
        float yCoord = ySpacing * 3.0f; // top most coordinate of the invader army
        setPosition(xCoord, yCoord);

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                // creates the top 2 rows of the army
                if(i < 2) {
                    invaders[i][j] = new Invader(armyPos.x + (xSpacing * j), armyPos.y + (ySpacing * i), 'c');
                }
                // creates the middle 2 rows of the army
                else if(i >= 2 && i < 4) {
                    invaders[i][j] = new Invader(armyPos.x + (xSpacing * j), armyPos.y + (ySpacing * i), 'b');
                }
                // creates the bottom 2 rows of the army
                else {
                    invaders[i][j] = new Invader(armyPos.x + (xSpacing * j), armyPos.y + (ySpacing * i), 'a');
                }
            }
        }

        invaderWidth = invaders[0][0].getWidth();
        invaderHeight = invaders[0][0].getHeight();
        invaderXMovementIncrement = invaders[0][0].getxMoveIncrement();
        invaderYMovementIncrement = invaders[0][0].getyMoveIncrement();

        armyWidth = xSpacing * cols;
        armyHeight = ySpacing * rows;
        armyCenter = new PointF(armyWidth / 2, armyHeight / 2);
        armyMovement = Movement.LEFT;
    }

    private void updateArmyPosition() {

        if(armyPos != null) {

            Log.v("AI movement: ", "armyPos.x = " + armyPos.x + " :: invaderWidth = " + invaderWidth);

            switch (armyMovement) {
                case LEFT:
                    armyPos.x = armyPos.x - invaderXMovementIncrement;
                    break;
                case RIGHT:
                    armyPos.x = armyPos.x + invaderXMovementIncrement;
                    break;
                case DOWN:
                    armyPos.y = armyPos.y + invaderYMovementIncrement;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * WARNING! Don't use this method to move the invader army. Its only purposes are to
     * set the initial position of the army, and also just in case it
     * proves useful to adjust the position of the army after it has be instantiated.
     * @param xPos
     * @param yPos
     */
    public void setPosition(float xPos, float yPos) {
        if (armyPos == null) {
            armyPos = new PointF(xPos, yPos);
            return;
        }

        armyPos.x = xPos;
        armyPos.y = yPos;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (invaders[i][j] != null) {
                    invaders[i][j].setPosition(armyPos.x + (xSpacing * j),
                            armyPos.y + (ySpacing * i));
                }
            }
        }

        return;
    }

    public void doCollision(Sprite sprite) {
        int rowIndex = getRowProximity(sprite);

        // if getRowProximity returned a valid index
        if(rowIndex > -1) {
            for (int i = 0; i < cols; i++) {
                invaders[rowIndex][i].doCollision(sprite);

                if(invaders[rowIndex][i].isHit() && !invaders[rowIndex][i].isScoreTallied()) {

                    switch (invaders[rowIndex][i].getType()) {
                        case 'a':
                            gameThread.addToPlayerScore(10);
                            break;
                        case 'b':
                            gameThread.addToPlayerScore(20);
                            break;
                        case 'c':
                            gameThread.addToPlayerScore(20);
                            break;
                        default:
                            break;
                    }

                    invaders[rowIndex][i].isScoreTallied(true);
                }
            }
        }
    }

    public void doWallCollision(DefenseWall wall) {
        if (wall != null) {
            float wallTop = wall.getPos().y;
            float armyBottomY = armyPos.y + armyHeight;
            float armyRowHeight = invaderHeight + ySpacing;

            for(int i = 0; i < rows; i++) {
                if(wallTop >= armyBottomY - (armyRowHeight * i)
                        && wallTop < armyBottomY - (armyRowHeight * (i - 1)) ) {

                    // the above if statement passes when the wall is within range
                    // of one of the rows of the invader army
                    for(int j = 0; j < cols; j++) {
                        wall.doCollisions(invaders[i][j]);
                    }
                }
            }
        }
    }

    /**
     * Checks to see if the sprite is in proximity to one of the rows of the invader army.
     * If it is, the row's index number will be returned. Otherwise, -1 is returned as a result.
     * @param sprite
     * @return the row index number that the sprite is closest to.
     */
    private int getRowProximity(Sprite sprite) {
        if(sprite != null) {
            float y = sprite.getRawY();
            float armyBottomY = armyPos.y + armyHeight;
            float armyRowHeight = ySpacing;

            for(int i = 0, row = rows - 1 ; i < rows; i++, row--) {
                if(y <= (armyBottomY - (armyRowHeight * i))
                        && y > (armyBottomY - (armyRowHeight * (i + 1))) ) {

                    // the above if statement passes when the sprite is within range
                    // of one of the rows of the invader army
                    return row;
                }
            }
        }
        return -1;
    }
}
