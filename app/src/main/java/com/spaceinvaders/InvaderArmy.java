package com.spaceinvaders;

import android.graphics.PointF;

/**
 * Created by Joseph on 4/11/2016.
 */
public class InvaderArmy {
    private Invader[][] invaders;
    /**
     * top-left corner position of invader army.
     */
    private PointF armyPos;
    private float armyWidth, armyHeight, armyCenter;
    private int rows = 6;
    private int cols = 5;
    /**
     * spacing in between each invader.
     */
    private float xSpacing, ySpacing;
    private float playFieldWidth, playFieldHeight;

    /**
     * number of invaders left in the army.
     */
    private int invadersLeft;

    public InvaderArmy(float xPos, float yPos, float playFieldWidth, float playFieldHeight) {
        // Access the individual coordinates of
        invaders = new Invader[rows][cols];
        xSpacing = Resources.img_invader_a01.getWidth()* 1.33f; // the amount to horizontally space invaders apart from each other
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

                - check it has gotten too close to the edge of a boundary on the screen.
                        If it has, move down once, and switch direction.
                        Use invaders[i][j].setMovementState( movement ) to change the direction of movement.
                        Leave the actual movement of the invader to the call to invaders[i][j].update( fps )
                        at the end of this loop.

                - do invader fire projectiles. Pass the "projectiles" attribute of this class
                        to invaders[i][j].fire( projectiles )

                - you could detect collisions here for each projectile, by doing the code
                        for(p : projectiles.getProjectiles()) {
                            if( p != null )
                                invaders[i][j].doCollision( p );
                        }
                        I've already implemented doCollision since its not really "AI"

                - At the end, just run invaders[i][j].update( fps ), and that takes care of the
                        actual movement.
         */
    }

    private void buildArmy() {
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

        armyWidth = (invaders[0][0].getWidth() + xSpacing) * invaders[0].length;
        armyHeight = (invaders[0][0].getHeight() + ySpacing) * invaders.length;
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

        /**
         * invaders.length gives the number of rows.
         * invaders[0].length gives the number of columns.
         */
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
}
