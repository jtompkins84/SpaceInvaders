package com.spaceinvaders.game_entities;

import android.graphics.Canvas;
import android.graphics.Paint;

import static com.spaceinvaders.Resources.img_invader_laser01;

public class InvaderLaser extends Projectile {
    private InvaderLaserCharge laserCharge;
    private short maxUpdateCount = 50;
    private short updateCount = 0;
    private boolean isCharged = false;

    public InvaderLaser(float xPos, float yPos) {
        super(xPos, yPos, Type.LASER);

        setStartAndEndFrames(1, 1);
        setPosition(xPos, yPos + (img_invader_laser01.getHeight() * 0.5f));

        laserCharge = new InvaderLaserCharge(xPos, yPos);

        doAnimationLoop = false;
        doAnimate = false;
        setStartAndEndFrames(0, 0);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, boolean showHitBox) {
        super.draw(canvas, paint, showHitBox);

        laserCharge.draw(canvas, paint);
    }

    @Override
    public void update(long fps) {
        if(laserCharge.isCharged() && !isCharged) {
            isCharged = true;
        }

        /*
            This method was devised to limit the amount of update ticks that the hitbox can be
            "exposed."

            The alternative to using this mod function was to increase the frame skipping. However,
            if the frame skipping method is used, on the frame where the laser's hitbox is exposed,
            hitbox collisions are being calculated the for each frame "skipped" by the animation.
            This would basically defeat the original purpose, which is to limit the amount of
            update ticks during which the laser's hitbox would check for collisions.
         */
        if(isCharged) {
            if(updateCount % 10 == 0) setCurrFrame(1);
            else setCurrFrame(2);

            updateCount++;
        }

        if(updateCount >= maxUpdateCount)
            isDestroyed(true);

        super.update(fps);
    }

    public boolean isCharged() {
        return laserCharge.isCharged();
    }

    @Override
    public void move(float dx, float dy) {
        super.move(dx, dy);

        laserCharge.move(dx, dy);
    }
}
