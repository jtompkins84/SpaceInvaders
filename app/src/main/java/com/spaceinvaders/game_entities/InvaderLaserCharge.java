package com.spaceinvaders.game_entities;

import android.graphics.Bitmap;

import com.spaceinvaders.Resources;

public class InvaderLaserCharge extends Particle {

    public InvaderLaserCharge(float xPos, float yPos) {
        super(new Bitmap[] {
                Resources.img_invader_laser_charge01,
                Resources.img_invader_laser_charge02,
                Resources.img_invader_laser_charge03,
                Resources.img_invader_laser_charge04,
                Resources.img_invader_laser_charge05,
                Resources.img_invader_laser_charge06,
                Resources.img_invader_laser_charge07,
                Resources.img_invader_laser_charge08,
                Resources.img_invader_laser_charge09,
                Resources.img_invader_laser_charge10,
                Resources.img_invader_laser_charge11,
                Resources.img_invader_laser_charge12,
                Resources.img_invader_laser_charge13,
                Resources.img_invader_laser_charge14,
                Resources.img_invader_laser_charge15,
                Resources.img_invader_laser_charge16},
                xPos, yPos);

        setStartAndEndFrames(0, 15);
        doAnimationLoop = false;
        setSkipFrames((short)5);
        setPosition(xPos, yPos + (getCurrentFrameSpriteBitmap().getHeight() * 0.3333f));
    }

    @Override
    public void update(long fps) {

    }

    public boolean isCharged() {
        if(getCurrFrame() < frames.length - 1) {
            return false;
        }

        return true;
    }
}
