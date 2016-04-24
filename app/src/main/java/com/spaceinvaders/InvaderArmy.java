package com.spaceinvaders;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.spaceinvaders.game_entities.PlayerLaserShot;
import com.spaceinvaders.game_entities.PowerUp;

import java.util.Random;

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
    private int scoreC = 30;
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
    private long timeBetweenMoves = 1500l;
    private long timeBtwnMovesMin = 200l;
    private long timeDecrement = 0;

    boolean doMoveDown = false;
    boolean doFire = false;
    int randFireChance = 6;

    public InvaderArmy(float playFieldWidth, float playFieldHeight, ProjectileArray projectiles, PlayFieldView playFieldView) {
        this.playFieldWidth = playFieldWidth;
        this.playFieldHeight = playFieldHeight;
        this.projectiles = projectiles;
        this.gameThread = playFieldView;

        if(Resources.difficulty == Resources.Difficulty.HARD) {
            randFireChance = 2;
            timeBetweenMoves = 900l;
            timeBtwnMovesMin = 100l;
            scoreA *= 2;
            scoreB *= 2;
            scoreC *= 2;
        }

        invaders = new Invader[rows][cols];
        xSpacing = Resources.img_invader_a01.getWidth()* 1.125f; // the amount to horizontally space invaders apart from each other
        ySpacing = Resources.img_invader_a01.getHeight(); // the amount to vertical space invaders apart from each other

        timeDecrement = (timeBetweenMoves - timeBtwnMovesMin) / (rows * cols);

        buildArmy();
    }

    public void update(long fps) {
        if(invadersLeft <= 0) return;

        boolean doMove = false;
        int fireRow = 0, fireCol = 0;
        long currTime = System.currentTimeMillis();
        // This is here to update the time that the invaders last moved and the position
        // of the army. Needs to happen after updating every invader in the army.
        if(currTime - lastMoveTime >= timeBetweenMoves) {
            lastMoveTime = System.currentTimeMillis();
            doMove = true;
        }
        else if(randFireChance == 2
                && currTime - lastMoveTime >= timeBetweenMoves / 2
                && currTime - lastMoveTime <= timeBetweenMoves - timeBetweenMoves / 4) {
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

            if(randFireResult == 0){
                doFire = true;

                // for loop is a safety measure to prevent the random number finding from taking too long
                for(int i = 0; i < 200; i++) {
                    fireRow = random.nextInt(rows);
                    fireCol = random.nextInt(cols);

                    if(!invaders[fireRow][fireCol].isHit()) break;
                }
            }

            // this is only back and for code. you will have to come up with a way
            // make them move down once at the edge

            // detection for right boundary
            if( (armyPos.x + armyWidth) > (playFieldWidth - invaderWidth) ){
                if(doMoveDown) {
                    armyMovement = Movement.DOWN;
                    updateArmyPosition();
                    doMoveDown = false;
                }
                else
                    armyMovement = Movement.LEFT; // for each if statement, make sure to change
                // this to the correct movement.
                // updateArmyPosition uses this value.


            }
            // detection for left boundary
            else if(armyPos.x < invaderWidth) {
                if(doMoveDown) {
                    armyMovement = Movement.DOWN;
                    doMoveDown = false;
                    updateArmyPosition();
                }
                else
                    armyMovement = Movement.RIGHT;
            }
            else doMoveDown = true;
        }


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (invaders[i][j] != null) {

                    if(doMove) {
                        invaders[i][j].setMovementState(armyMovement);
                    }

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

        // This has to be here to keep track of the army, otherwise
        // hit detection will get all screwy when the army starts
        // to move down.
        if(doMove) {
            if(armyMovement != Movement.DOWN)
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
        return invadersAlive;
    }

    public boolean isAtBottom() {

        if(armyPos.y + armyHeight > playFieldHeight) {
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < cols; j++) {
                    if(!invaders[i][j].isDead()) {
                        if(invaders[i][j].getRawY() >= playFieldHeight) return true;
                    }
                }
            }
        }

        return false;
    }

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

    public void doCollision(Sprite sprite) {

        if(sprite instanceof PlayerLaserShot) {
            int colIndex = getColumnProximity(sprite);

            if(colIndex > -1) {
                for (int i = 0; i < rows; i++) {
                    if (invaders[i][colIndex] != null) {
                        if (!invaders[i][colIndex].isHit()) {
                            invaders[i][colIndex].doCollision(sprite);

                            if (invaders[i][colIndex].isHit() && !invaders[i][colIndex].isScoreTallied()) {
                                PowerUp powerUp = invaders[i][colIndex].dropPowerup();
                                if (powerUp != null) projectiles.addProjectile(powerUp);

                                timeBetweenMoves -= timeDecrement;
                                invadersLeft--;

                                switch (invaders[i][colIndex].getType()) {
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

                                invaders[i][colIndex].isScoreTallied(true);
                            }
                        }
                    }
                }
            }

            return;
        }
        // if getRowProximity returned a valid index
        else  {
            int rowIndex = getRowProximity(sprite);

            if(rowIndex > -1 && rowIndex < rows) {
                for (int i = 0; i < cols; i++) {
                    if (invaders[rowIndex][i] != null) {
                        invaders[rowIndex][i].doCollision(sprite);

                        if (sprite instanceof Player) {
                            ((Player) sprite).doDeath();
                            invaders[rowIndex][i] = null;

                            invadersLeft--;
                            return;
                        }

                        if (invaders[rowIndex][i].isHit() && !invaders[rowIndex][i].isScoreTallied()) {
                            PowerUp powerUp = invaders[rowIndex][i].dropPowerup();
                            if (powerUp != null) projectiles.addProjectile(powerUp);

                            invadersLeft--;
                            timeBetweenMoves -= timeDecrement;

                            switch (invaders[rowIndex][i].getType()) {
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

                            invaders[rowIndex][i].isScoreTallied(true);
                        }
                    }
                }
            }

            return;
        }
    }

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
}
