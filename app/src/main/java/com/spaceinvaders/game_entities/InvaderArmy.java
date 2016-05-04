package com.spaceinvaders.game_entities;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.spaceinvaders.gui.Movement;
import com.spaceinvaders.gui.PlayFieldView;
import com.spaceinvaders.Resources;

import java.util.Random;

/**
 * COMPONENT: Enemy AI
 * Written by Victor Garcia
 * Co-Written by Joseph Tompkins
 *
 * This class dictates the movement of the Invaders, implemented as a 6 X 5 matrix. It also has
 * methods to support collision detection between the Invaders and the various other objects in the game,
 * including the player's projectiles.
 */
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
    private int scoreA = 10;
    private int scoreB = 20;
    private int scoreC = 40;
    /**
     * spacing in between each invader.
     */
    private float xSpacing, ySpacing;
    private float playFieldWidth, playFieldHeight;

    /**
     * number of invaders left in the army.
     */
    private int invadersLeft = 0;
    private int invadersAlive = 0;

    private long lastMoveTime = System.currentTimeMillis();
    private long timeBetweenMoves = 1200l;
    private long timeBtwnMovesMin = 200l;
    private long timeDecrement = 0;

    boolean doMoveDown = false;
    boolean doFire = false;
    boolean doHardFire = true;
    int randFireChance = 6;

    public InvaderArmy(float playFieldWidth, float playFieldHeight, ProjectileArray projectiles, PlayFieldView playFieldView) {
        this.playFieldWidth = playFieldWidth;
        this.playFieldHeight = playFieldHeight;
        this.projectiles = projectiles;
        this.gameThread = playFieldView;

        // Changes some settings for the HARD difficulty.
        if(Resources.difficulty == Resources.Difficulty.HARD) {
            randFireChance = 2;
            timeBetweenMoves = 900l;
            timeBtwnMovesMin = 100l;
            scoreA *= 2;
            scoreB *= 2;
            scoreC *= 2;
        }

        timeDecrement = (timeBetweenMoves - timeBtwnMovesMin) / (rows * cols) ;

        invaders = new Invader[rows][cols];
        xSpacing = Resources.img_invader_a01.getWidth()* 1.125f; // the amount to horizontally space invaders apart from each other
        ySpacing = Resources.img_invader_a01.getHeight(); // the amount to vertical space invaders apart from each other

        buildArmy();
    }

    /**
     * All A.I. behavior if the <code>InvaderArmy</code> is handled here.
     * @param fps <i>Frames-Per-Second</i>
     */
    public void update(long fps) {
        if(invadersAlive <= 0) return;

        boolean doMove = false;
        int fireRow = 0, fireCol = 0;
        long currTime = System.currentTimeMillis();

        // This is here to update the time that the invaders last moved and the position
        // of the army. Needs to happen after updating every invader in the army.
        if(currTime - lastMoveTime >= timeBetweenMoves) {
            doHardFire = true;
            lastMoveTime = System.currentTimeMillis();
            doMove = true;
        }
        // if it's not time move, and the difficulty is set to HARD, then the invaders will
        // have a chance of shooting a projectile half way in between moves.
        else if(randFireChance == 2 && doHardFire && invadersLeft > 0
                && currTime - lastMoveTime >= timeBetweenMoves / 2
                && currTime - lastMoveTime <= timeBetweenMoves - timeBetweenMoves / 4) {

            doHardFire = false;
            if(invadersLeft <= (rows * cols) / 2) {
                Random random = new Random();
                int randFireResult = Math.abs( random.nextInt() % randFireChance );

                if(randFireResult == 0){
                    doFire = true;

                    // for loop is a safety measure to prevent the random number finding from taking too long
                    for(int i = 0; i < 200; i++) {
                        fireRow = random.nextInt(rows);
                        fireCol = random.nextInt(cols);

                        if(!invaders[fireRow][fireCol].isHit()) break;
                    }
                }

            }
        }

        if(invadersLeft <= (rows * cols) / 2 && randFireChance > 4) randFireChance = 4;

        if(doMove) {
            // inside here, do detection of boundaries
            Random random = new Random();
            int randFireResult = Math.abs( random.nextInt() % randFireChance );

            if(randFireResult == 0 && invadersLeft > 0){
                doFire = true;

                // for loop is a safety measure to prevent the random number finding from taking too long
                for(int i = 0; i < 200; i++) {
                    fireRow = random.nextInt(rows);
                    fireCol = random.nextInt(cols);

                    if(!invaders[fireRow][fireCol].isHit()) break;
                }
            }

            // detection for right boundary
            if( (armyPos.x + armyWidth) > (playFieldWidth - invaderWidth) ){
                if(doMoveDown) {
                    armyMovement = Movement.DOWN; // The army reaches the edge and moves down once
                    updateArmyPosition();
                    doMoveDown = false;
                }
                else armyMovement = Movement.LEFT;
            }
            // detection for left boundary
            else if(armyPos.x < invaderWidth) {
                if(doMoveDown) {
                    armyMovement = Movement.DOWN; // The army reaches the edge and moves down once
                    doMoveDown = false;
                    updateArmyPosition();
                }
                else armyMovement = Movement.RIGHT;
            }
            else doMoveDown = true;
        }
// Below is the nested loop to update each invader in the army.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (invaders[i][j] != null) {

                    if(doMove) { invaders[i][j].setMovementState(armyMovement); }

                    invaders[i][j].update(fps);

                    // This has to be here to keep the invaders from moving more than they are meant to.
                    invaders[i][j].setMovementState(Movement.STOPPED);

                    if(invaders[i][j].isDead() && !invaders[i][j].isDeathCounted()) {
                        invaders[i][j].isDeathCounted(true);
                        invadersAlive--;
                    }
                }
            }
        }

        if(doFire){
            if(invaders[fireRow][fireCol] != null)
            invaders[fireRow][fireCol].fireProjectile(projectiles);
            doFire = false;
        }

        // Updating the position of army insures accurate location tracking of the rows and columns
        // for use in the hit detection.
        if(doMove) { if(armyMovement != Movement.DOWN) updateArmyPosition(); }
    }

    /**
     * A nested loop that calls the <code>draw</code> method of each <code>Invader</code> in the
     * army.
     * @param canvas the canvas to draw to
     * @param paint the paint object to use when drawing
     * @param showHitBox display the hit box
     */
    public void draw(Canvas canvas, Paint paint, boolean showHitBox) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (invaders[i][j] != null) {
                    invaders[i][j].draw(canvas, paint, showHitBox);
                }
            }
        }
    }

    /**
     *
     * @return the number of <code>Invader</code>s left alive.
     */
    public int getInvadersLeft() { return invadersAlive; }

    /**
     *
     * @return <code>true</code> if the top-most <code>Invader</code> in the army has passed below
     * the bottom of the play field.
     */
    public boolean isAtBottom() {
        if(armyPos.y + armyHeight > playFieldHeight) {
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < cols; j++) {
                    if(!invaders[i][j].isDead() && !invaders[i][j].isHit()) {
                        if(invaders[i][j].getHitBox().top > playFieldHeight) return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Instantiates each of the <code>Invader</code>s in the army. Also sets some important
     * attributes that track the size-on-screen and position of the army.
     */
    private void buildArmy() {
        float xCoord = playFieldWidth - (xSpacing * 4.625f); // left most coordinate of the invader army
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

        armyWidth = xSpacing * (cols - 1);
        armyHeight = ySpacing * (rows - 1);
        armyCenter = new PointF(armyWidth / 2, armyHeight / 2);
        armyMovement = Movement.LEFT;

        invadersLeft = cols * rows;
        invadersAlive = cols * rows;
    }

    /**
     * Tracks the current upper-left-hand corner of the InvaderArmy.
     */
    private void updateArmyPosition() {

        if(armyPos != null) {
            switch (armyMovement) {
                case LEFT:
                    armyPos.x = armyPos.x - invaderXMovementIncrement;
                    break;
                case RIGHT:
                    armyPos.x = armyPos.x + invaderXMovementIncrement;
                    break;
                case DOWN:
                    armyPos.y += invaderYMovementIncrement;
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

    /**
     * If <code>sprite</code> <i><b>is</b></i> an instance of <code>PlayerLaserShot</code>, this method
     * checks if <code>sprite</code>'s x-position is within the x-range of one of the columns of
     * this <code>InvaderArmy</code>. Reduces collision checks to a specific column rather than
     * to each and every <code>Invader</code> in the <code>InvaderArmy</code>.
     * <div></div>
     * If <code>sprite</code> <i><b>is not</b></i> an instance of <code>PlayerLaserShot</code>,
     * this method checks if <code>sprite</code>'s y-position is within the y-range of one of the
     * rows of this <code>InvaderArmy</code>. Reduces collision checks to a specific row
     * rather than to each and every <code>Invader</code> in the <code>InvaderArmy</code>.
     * @param sprite
     */
    public void doCollision(Sprite sprite) {
        int col = -1;
        int row = -1;

        if(sprite instanceof PlayerLaserShot) { col = getColumnProximity(sprite); }
        else row = getRowProximity(sprite);

        if(col > -1) {
            for (int i = 0; i < rows; i++) {
                if (invaders[i][col] != null) {
                    if (!invaders[i][col].isHit()) {
                        invaders[i][col].doCollision(sprite);

                        if (invaders[i][col].isHit() && !invaders[i][col].isScoreTallied()) {
                            PowerUp powerUp = invaders[i][col].dropPowerup();
                            if (powerUp != null) projectiles.addProjectile(powerUp);

                            timeBetweenMoves -= timeDecrement;
                            invadersLeft--;

                            tallyScore(i, col);
                        }
                    }
                }
            }
            return;
        }
        // if getRowProximity returned a valid index
        else if(row > -1) {

            if(row > -1 && row < rows) {
                for (int i = 0; i < cols; i++) {
                    if (invaders[row][i] != null) {
                        invaders[row][i].doCollision(sprite);

                        if (invaders[row][i].isHit() && !invaders[row][i].isScoreTallied()) {
                            PowerUp powerUp = invaders[row][i].dropPowerup();
                            if (powerUp != null) projectiles.addProjectile(powerUp);

                            // Invader hits a player. Don't tally score. Player & Invader "die"
                            if (invaders[row][i].isHit()
                                    && sprite instanceof Player) {
                                invaders[row][i].isScoreTallied(true);
                                invadersLeft--;
                                return;
                            }

                            invadersLeft--;
                            timeBetweenMoves -= timeDecrement;

                            tallyScore(row, i);
                        }
                    }
                }
            }
        }
    }

    /**
     * Designed specifically to manage <code>InvaderArmy</code> collisions with the a
     * <code>DefenseWall</code>.
     * @param wall
     */
    public void doWallCollision(DefenseWall wall) {
        if (wall != null) {
            float wallTop = wall.getRawPos().y;
            float armyBottomY = armyPos.y + armyHeight;

            if(armyBottomY < wallTop) return;

            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < cols; j++) {
                    if(invaders[i][j] != null ) {
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

    /**
     * Checks to see if the sprite is in proximity to one of the rows of the invader army.
     * If it is, the row's index number will be returned. Otherwise, -1 is returned as a result.
     * @param sprite
     * @return the row index number that the sprite is closest to.
     */
    private int getColumnProximity(Sprite sprite) {
        float colWidth = xSpacing;
        float armyXPos = armyPos.x - (invaderWidth / 2);

        for(int i = 0, col = 0 ; i < cols; i++, col++) {
            if( sprite.getX() >= armyXPos + (colWidth * i)
                    && sprite.getX() <= armyXPos + (colWidth * (i + 1)) ) {

                // the above if statement passes when the sprite is within x-range
                // of one of the columns of the invader army
                return col;
            }
        }
        return -1;
    }

    /**
     * Sub-routine to tally a score for an <code>Invader</code> that has been hit by a player
     * projectile.
     * @param row
     * @param col
     */
    private void tallyScore(int row, int col) {
        switch (invaders[row][col].getType()) {
            case 'a':
                gameThread.addToPlayerScore(scoreA);
                break;
            case 'b':
                gameThread.addToPlayerScore(scoreB);
                break;
            case 'c':
                gameThread.addToPlayerScore(scoreC);
                break;
            default:
                break;
        }

        invaders[row][col].isScoreTallied(true);
    }

    /**
     * For use when resuming the game from a pause. Prevents the <code>InvaderArmy</code> from
     * strange behavior, like firing a dozen projectiles at once because real time has advanced, but
     * the <code>InvaderArmy</code>'s time has not.
     */
    public void updateTimeOnResume() {
        lastMoveTime += (System.currentTimeMillis() - lastMoveTime);
    }
}
