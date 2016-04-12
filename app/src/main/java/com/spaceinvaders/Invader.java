package com.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class Invader extends Sprite {

    private boolean isHit = false;
    private boolean isDead = false;
    private boolean isScoreTallied = false;
    private char invaderType;

    private float width;
    private float height;

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

        this.setSkipFrames((short) 5);
        doAnimate = false;
        setPosition(xPos, yPos);
        setStartAndEndFrames(0, 1);
        width = Resources.img_invader_a01.getWidth();
        height = Resources.img_invader_a01.getHeight();
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
            float moveX = width / 2;
            float moveY = height / 2;

            switch (movement) {
                case LEFT:
                    move(-moveX, 0.0f);
                    nextFrame();
                    break;
                case RIGHT:
                    move(moveX, 0.0f);
                    nextFrame();
                    break;
                case DOWN:
                    move(0.0f, moveY);
                    nextFrame();
                    break;
                case STOPPED:
                    break;
            }
        }
    }

    public boolean doCollision(Sprite sprite) {
        if(sprite != null
                && sprite.getHitBox() != null && getHitBox() != null
                && sprite.getHitBox().intersect(getHitBox())) {

            if(sprite.getClass() == DefenseBrick.class) {
                sprite = null;
                return true;
            }
            else if(sprite.getClass() == Projectile.class) {
                Projectile p = (Projectile) sprite;

                if(p.isFromPlayer()) {
                    p.isDestroyed(true);
                    isHit = true;
                }
                else return false;
            }
            else if(sprite.getClass() == Player.class) {
                doDrawFrame = false;
                setCurrFrame(7); // frame at index 7 is blank
                isDead = true; // should trigger
                return true;
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

    /**
     * TODO still need to implement power-ups
     */
    public void dropPowerup() {
        // TODO implement after power-ups are implemented
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
}
