package com.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.RectF;

import com.spaceinvaders.game_entities.PlayerLaserShot;
import com.spaceinvaders.game_entities.PowerUp;

import java.util.Random;

public class Invader extends Sprite {

    private boolean isHit = false;
    private boolean isDead = false;
    private boolean isDeathCounted = false;
    private boolean isScoreTallied = false;
    private char invaderType;

    private float width;
    private float height;
    private float xMoveIncrement;
    private float yMoveIncrement;

    private int dropChanceWeapon = 8;
    private int dropChanceShield = 5;

    /**
     *
     * @param xPos
     * @param yPos
     * @param invader_type
     */
    public Invader(float xPos, float yPos, char invader_type) {
        super(new Bitmap[] {null, null, Resources.img_invader_death01, Resources.img_invader_death02,
                Resources.img_invader_death03, Resources.img_invader_death04, Resources.img_invader_death05, Resources.img_blank},
                new RectF[] {null, null, null, null, null, null, null, null});

        float dpiRatio = Resources.DPIRatio;
        invaderType = invader_type;
        // set invader type to have the correct frames
        switch (invader_type) {
            case 'a':
                setFrameImage(0, Resources.img_invader_a01);
                setFrameImage(1, Resources.img_invader_a02);
                setHitBox(0, new RectF(7.0f * dpiRatio, 13.0f * dpiRatio, 98.0f * dpiRatio, 79.0f * dpiRatio));
                setHitBox(1, new RectF(7.0f * dpiRatio, 13.0f * dpiRatio, 98.0f * dpiRatio, 78.0f * dpiRatio));
                break;
            case 'b':
                setFrameImage(0, Resources.img_invader_b01);
                setFrameImage(1, Resources.img_invader_b02);
                setHitBox(0, new RectF(25.0f * dpiRatio, 17.0f * dpiRatio, 81.0f * dpiRatio, 73.0f * dpiRatio));
                setHitBox(1, new RectF(25.0f * dpiRatio, 17.0f * dpiRatio, 81.0f * dpiRatio, 73.0f * dpiRatio));
                break;
            case 'c':
                setFrameImage(0, Resources.img_invader_c01);
                setFrameImage(1, Resources.img_invader_c02);
                setHitBox(0, new RectF(28.0f * dpiRatio, 28.0f * dpiRatio, 77.0f * dpiRatio, 70.0f * dpiRatio));
                setHitBox(1, new RectF(28.0f * dpiRatio, 28.0f * dpiRatio, 77.0f * dpiRatio, 70.0f * dpiRatio));
                break;
            default:
                setFrameImage(0, Resources.img_invader_a01);
                setFrameImage(1, Resources.img_invader_a02);
                setHitBox(0, new RectF(7.0f * dpiRatio, 13.0f * dpiRatio, 98.0f * dpiRatio, 79.0f * dpiRatio));
                setHitBox(1, new RectF(7.0f * dpiRatio, 13.0f * dpiRatio, 98.0f * dpiRatio, 78.0f * dpiRatio));
                break;
        }

        if(Resources.difficulty == Resources.Difficulty.HARD) {
            dropChanceWeapon = 10;
            dropChanceShield = 7;
        }

        this.setSkipFrames((short) 5);
        doAnimate = false;
        setPosition(xPos, yPos);
        setStartAndEndFrames(0, 1);
        width = Resources.img_invader_a01.getWidth();
        height = Resources.img_invader_a01.getHeight();
        xMoveIncrement = width / 2;
        yMoveIncrement = height / 2;
    }

    @Override
    public void update(long fps) {
        if(getCurrFrameIndex() == 7) {
            isDead = true;
            return;
        }
        else if(isHit && getCurrFrameIndex() < 3) {
            setStartAndEndFrames(2, 7);
            doAnimationLoop = false; // do not loop animation.
            doAnimate = true; // do animate the death sequence.
            return;
        }
        else if(!isHit) {

            switch (movement) {
                case LEFT:
                    move(-xMoveIncrement, 0.0f);
                    nextFrame();
                    break;
                case RIGHT:
                    move(xMoveIncrement, 0.0f);
                    nextFrame();
                    break;
                case DOWN:
                    move(0.0f, yMoveIncrement);
                    nextFrame();
                    break;
                case STOPPED:
                    break;
            }
        }
    }

    public boolean doCollision(Sprite sprite) {
        if(sprite != null) {

            if (sprite.getHitBox() != null && getHitBox() != null
                    && sprite.getHitBox().intersect(getHitBox())) {

                if (sprite instanceof Projectile) {

                    if (!(sprite instanceof PlayerLaserShot)) {

                        Projectile p = (Projectile) sprite;

                        if (p.isFromPlayer()) {
                            p.isDestroyed(true);
                            isHit = true;
                        } else return false;
                    } else {
                        PlayerLaserShot laser = ((PlayerLaserShot) sprite);
                        if (sprite.getHitBox() != null && !isHit && laser.getHitBox() != null) {
                            laser.repairHitBox(); // fixes a weird bug. not sure why its necessary.
                            isHit = true;
                            return true;
                        }

                        return false;
                    }
                }
                else if (sprite.getClass() == DefenseBrick.class) {
                    return true;
                }
                else if (sprite instanceof Player) {
                    setStartAndEndFrames(7, 7);
                    isDead = true;
                    ((Player) sprite).doDeath();
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Check to see if the invader should be destroyed (made null)
     * @return
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * The width of the <code>Invader</code>.
     * @return
     */
    public float getWidth() {
        return width;
    }

    /**
     * The height of the <code>Invader</code>.
     * @return
     */
    public float getHeight() {
        return height;
    }

    public char getType() {
        return invaderType;
    }

    /**
     * Creates a non-player projectile from the position of this invader.
     * @param projectiles the relevant <code>ProjectileArray</code> to add a new projectile to
     */
    public void fireProjectile(ProjectileArray projectiles) {
        projectiles.addProjectile(getX(), getY(), false);
    }


    public PowerUp dropPowerup() {
        Random rand = new Random();

        int result = rand.nextInt(100);

        // If the drop chance calculation happens to result in a situation where the Invader could
        // drop either a shield or weapon type power up, create another random chance where it is
        // slightly more likely to drop a shield instead of a weapon type power up.
        if(result % dropChanceWeapon == 0 && result % dropChanceShield == 0) {
            if(rand.nextInt(dropChanceShield) % 10 > rand.nextInt(dropChanceWeapon) % 4) {
                return new PowerUp(getX(), getY(), PowerUp.Color.BLUE);
            }
            else {
                result = rand.nextInt(2);
                if(result == 0) return new PowerUp(getX(), getY(), PowerUp.Color.RED);

                return new PowerUp(getX(), getY(), PowerUp.Color.YELLOW);
            }
        }

        if(result % dropChanceWeapon == 0) {
            result = rand.nextInt(2);
            if(result == 0) return new PowerUp(getX(), getY(), PowerUp.Color.RED);
            return new PowerUp(getX(), getY(), PowerUp.Color.YELLOW);
        }
        else if(result % dropChanceShield == 0)
            return new PowerUp(getX(), getY(), PowerUp.Color.BLUE);

        return null;
    }

    public boolean isHit() {
        return isHit;
    }

    public boolean isScoreTallied() {
        return isScoreTallied;
    }

    public void isScoreTallied(boolean b) {
        isScoreTallied = b;
    }

    public boolean isDeathCounted() {
        return isDeathCounted;
    }

    public void isDeathCounted(boolean b) {
        isDeathCounted = b;
    }

    /**
     * The amount that the invader moves along the x axis in one movement increment.
     * @return
     */
    public float getxMoveIncrement() {
        return xMoveIncrement;
    }

    /**
     * The amount that the invader moves along the y axis in one movement increment.
     * @return
     */
    public float getyMoveIncrement() {
        return yMoveIncrement;
    }
}
