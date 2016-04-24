package com.spaceinvaders.game_entities;

import android.graphics.Bitmap;
import android.graphics.RectF;

import com.spaceinvaders.Projectile;
import com.spaceinvaders.Resources;

/**
 * Created by Joseph on 4/23/2016.
 */
public class PowerUp extends Projectile {
    public enum Color {BLUE, RED, YELLOW}

    private Color mColor;


    /**
     * Instantiates a new powerup
     * @param xPos
     * @param yPos
     * @param color color types are<code>{BLUE, RED, YELLOW}</code>
     */
    public PowerUp(float xPos, float yPos, PowerUp.Color color) {
        super(xPos, yPos, Type.POWERUP);

        mColor = color;

        switch (color) {
            case BLUE:
                frames = new Bitmap[] {
                        Resources.img_powerup_blue01,
                        Resources.img_powerup_blue02,
                        Resources.img_powerup_blue03,
                        Resources.img_powerup_blue02,
                        null
                };
                break;
            case RED:
                frames = new Bitmap[] {
                        Resources.img_powerup_red01,
                        Resources.img_powerup_red02,
                        Resources.img_powerup_red03,
                        Resources.img_powerup_red02,
                        null
                };
                break;
            case YELLOW:
                frames = new Bitmap[] {
                        Resources.img_powerup_yellow01,
                        Resources.img_powerup_yellow02,
                        Resources.img_powerup_yellow03,
                        Resources.img_powerup_yellow02,
                        null
                };
                break;
        }

        hitBoxes = new RectF[] {
                new RectF(22 * Resources.DPIRatio, 24 * Resources.DPIRatio, 39 * Resources.DPIRatio, 41 * Resources.DPIRatio),
                new RectF(22 * Resources.DPIRatio, 24 * Resources.DPIRatio, 39 * Resources.DPIRatio, 41 * Resources.DPIRatio),
                new RectF(22 * Resources.DPIRatio, 24 * Resources.DPIRatio, 39 * Resources.DPIRatio, 41 * Resources.DPIRatio),
                new RectF(22 * Resources.DPIRatio, 24 * Resources.DPIRatio, 39 * Resources.DPIRatio, 41 * Resources.DPIRatio),
                null
        };

        doAnimationLoop = true;
        setStartAndEndFrames(0, 3);
        setSkipFrames((short)5);

        setPosition(xPos, yPos);
    }


    @Override
    public void isDestroyed(boolean b) {
        if(b) {
            doAnimate = false;
            doDrawFrame = false;
            setCurrFrame(4);
        }
    }

    public Color getColor() {
        return mColor;
    }
}
