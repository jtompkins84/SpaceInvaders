package com.spaceinvaders.game_entities;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.spaceinvaders.Projectile;
import com.spaceinvaders.Resources;

public class PlayerLaserShot extends Projectile {
    private PlayerLaserCharge laserCharge;
    private short maxUpdateCount = 20;
    private short updateCount = 0;
    private boolean isCharged = false;

    public PlayerLaserShot(float xPos, float yPos) {
        super(xPos, yPos, Type.PLAYER_SPECIAL);

        laserCharge = new PlayerLaserCharge(xPos,
                yPos + (Resources.img_player_laser.getHeight() / 2.1f ));

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
        if(laserCharge.isFullCharge() && !isCharged) {
            setStartAndEndFrames(1, 2);
            isCharged = true;
        }

        if(isCharged) {
            if(updateCount % 8 == 0) setCurrFrame(1);
            else setCurrFrame(2);

            updateCount++;
        }

        if(updateCount >= maxUpdateCount)
            isDestroyed(true);

        super.update(fps);
    }

    /**
     * Sets the position of the <code>Sprite</code>. Position is in pixels
     * @param x
     * @param y
     */
    @Override
    public void setPosition(float x, float y) {
        pos.set(x - ((float) frames[1].getWidth() / 2.0f),
                y - ((float) frames[1].getHeight() / 2.0f));

        if(hitBoxes != null) {
            // offset hitbox by position of sprite plus offset of hitbox relative to the sprite
            for (RectF hb : hitBoxes) {
                if(hb != null)
                    hb.offsetTo(pos.x + hb.left, pos.y + hb.top);
            }
        }
    }

    /**
     * @return x-coordinate relative to center of this <code>Sprite</code>'s bitmap.
     */
    @Override
    public float getX() {
        float width = frames[1].getWidth();
        return (pos.x + (width / 2));    }

    /**
     * @return y-coordinate relative to center of this <code>Sprite</code>'s bitmap.
     */
    @Override
    public float getY() {
        float height = frames[1].getHeight();
        return (pos.y + (height / 2));
    }
}
