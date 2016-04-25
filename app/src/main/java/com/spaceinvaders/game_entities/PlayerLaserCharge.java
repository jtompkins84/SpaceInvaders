package com.spaceinvaders.game_entities;

import android.graphics.Bitmap;

import com.spaceinvaders.Resources;

public class PlayerLaserCharge extends Particle {
    private boolean fullCharge = false;

    /**
     * Instantiates a <code>PlayerLaserCharge</code> particle.
     *
     * @param xPos   initial position on the x-axis
     * @param yPos   initial position on the y-axis
     */
    public PlayerLaserCharge(float xPos, float yPos) {
        super(new Bitmap[] {
                Resources.img_player_laser_charge01,
                Resources.img_player_laser_charge02,
                Resources.img_player_laser_charge03,
                Resources.img_player_laser_charge04,
                Resources.img_player_laser_charge05,
                Resources.img_player_laser_charge06,
                Resources.img_player_laser_charge07,
                Resources.img_player_laser_charge08,
                Resources.img_player_laser_charge09,
                Resources.img_player_laser_charge10,
                Resources.img_player_laser_charge11,
                Resources.img_player_laser_charge12,
                Resources.img_player_laser_charge13,
                Resources.img_player_laser_charge14,
                Resources.img_player_laser_charge15 },
                xPos, yPos);

        setStartAndEndFrames(0, 14);
        doAnimationLoop = false;
        setSkipFrames((short)5);
        setPosition(xPos, yPos);
    }

    @Override
    public void update(long fps) {
    }

    /**
     *
     * @return Returns <code>true</code> if the end of the chargin animation has been reached.
     */
    public boolean isFullCharge() {
        if(getCurrFrame() < frames.length - 1)
            return false;

        return true;
    }
}
