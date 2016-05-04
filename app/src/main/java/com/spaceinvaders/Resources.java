package com.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.tutorials.joseph.spaceinvaders.R;

/**
 * Initializes all of the resources and provides access to them from anywhere in
 * the app.
 */
public class Resources {
    private static Resources resources = null;

    public static float DPIRatio;

    public enum Difficulty {NORMAL, HARD, DEMO}

    public static Difficulty difficulty = Difficulty.NORMAL;

    public static int player_final_score = 0;

    public static Bitmap img_player;
    public static Bitmap img_player_death01;
    public static Bitmap img_player_death02;
    public static Bitmap img_player_death03;
    public static Bitmap img_player_death04;
    public static Bitmap img_player_shield;
    public static Bitmap img_player_laser_charge01;
    public static Bitmap img_player_laser_charge02;
    public static Bitmap img_player_laser_charge03;
    public static Bitmap img_player_laser_charge04;
    public static Bitmap img_player_laser_charge05;
    public static Bitmap img_player_laser_charge06;
    public static Bitmap img_player_laser_charge07;
    public static Bitmap img_player_laser_charge08;
    public static Bitmap img_player_laser_charge09;
    public static Bitmap img_player_laser_charge10;
    public static Bitmap img_player_laser_charge11;
    public static Bitmap img_player_laser_charge12;
    public static Bitmap img_player_laser_charge13;
    public static Bitmap img_player_laser_charge14;
    public static Bitmap img_player_laser_charge15;
    public static Bitmap img_player_laser;
    public static Bitmap img_invader_a01;
    public static Bitmap img_invader_a02;
    public static Bitmap img_invader_b01;
    public static Bitmap img_invader_b02;
    public static Bitmap img_invader_c01;
    public static Bitmap img_invader_c02;
    public static Bitmap img_invader_death01;
    public static Bitmap img_invader_death02;
    public static Bitmap img_invader_death03;
    public static Bitmap img_invader_death04;
    public static Bitmap img_invader_death05;
    public static Bitmap img_invader_laser01;
    public static Bitmap img_invader_laser_charge01;
    public static Bitmap img_invader_laser_charge02;
    public static Bitmap img_invader_laser_charge03;
    public static Bitmap img_invader_laser_charge04;
    public static Bitmap img_invader_laser_charge05;
    public static Bitmap img_invader_laser_charge06;
    public static Bitmap img_invader_laser_charge07;
    public static Bitmap img_invader_laser_charge08;
    public static Bitmap img_invader_laser_charge09;
    public static Bitmap img_invader_laser_charge10;
    public static Bitmap img_invader_laser_charge11;
    public static Bitmap img_invader_laser_charge12;
    public static Bitmap img_invader_laser_charge13;
    public static Bitmap img_invader_laser_charge14;
    public static Bitmap img_invader_laser_charge15;
    public static Bitmap img_invader_laser_charge16;
    public static Bitmap img_invader_UFO_hit01;
    public static Bitmap img_invader_UFO01;
    public static Bitmap img_invader_UFO_firing01;
    public static Bitmap img_invader_UFO_firing02;
    public static Bitmap img_invader_UFO_firing03;
    public static Bitmap img_projectile_a;
    public static Bitmap img_brick_01;
    public static Bitmap img_brick_02;
    public static Bitmap img_brick_03;
    public static Bitmap img_brick_04;
    public static Bitmap img_brick_aa01;
    public static Bitmap img_brick_aa02;
    public static Bitmap img_brick_aa03;
    public static Bitmap img_brick_aa04;
    public static Bitmap img_brick_ad01;
    public static Bitmap img_brick_ad02;
    public static Bitmap img_brick_ad03;
    public static Bitmap img_brick_ad04;
    public static Bitmap img_brick_cb01;
    public static Bitmap img_brick_cb02;
    public static Bitmap img_brick_cb03;
    public static Bitmap img_brick_cb04;
    public static Bitmap img_brick_cc01;
    public static Bitmap img_brick_cc02;
    public static Bitmap img_brick_cc03;
    public static Bitmap img_brick_cc04;
    public static Bitmap img_powerup_blue01;
    public static Bitmap img_powerup_blue02;
    public static Bitmap img_powerup_blue03;
    public static Bitmap img_powerup_red01;
    public static Bitmap img_powerup_red02;
    public static Bitmap img_powerup_red03;
    public static Bitmap img_powerup_yellow01;
    public static Bitmap img_powerup_yellow02;
    public static Bitmap img_powerup_yellow03;
    public static Bitmap img_countdown_1;
    public static Bitmap img_countdown_2;
    public static Bitmap img_countdown_3;
    public static Bitmap img_icon_player_life;
    public static Bitmap img_blank;
    public static Bitmap img_title_splash;

/******************************************************
 * ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^
 * Bitmaps
 *
 * Animations
 * v v v v v v v v v v v v v v v
 ******************************************************/
    public static Bitmap[] brick;
    public static Bitmap[] brick_aa;
    public static Bitmap[] brick_ad;
    public static Bitmap[] brick_cb;
    public static Bitmap[] brick_cc;

    private Resources(Context cntxt) {

        // Recalculate DPI to a standard size.
        float dpi = (int)((640.0f / 1440.0f) * cntxt.getResources().getDisplayMetrics().widthPixels); // Adjust to some ratio of desired DPI of 640
        cntxt.getResources().getDisplayMetrics().densityDpi = (int)dpi; // Change Device's DPI to new DPI

        // Tell rendering how to size bitmaps
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inDensity = 480;
        BitmapFactory.Options opt2 = new BitmapFactory.Options();
        opt2.inDensity = 240;

        DPIRatio = dpi / (float)opt.inDensity;

        img_player = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player, opt);
//        Log.v("PlayFieldView", "Width Ratio <SPRITE_WIDTH> / <SCREEN_WIDTH>: " + img_player.getBitmap().getWidth() + " / " + playFieldWidth + " = " + ((float)img_player.getBitmap().getWidth() / (float)playFieldWidth));
        img_player_death01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_death01, opt);
        img_player_death02 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_death02, opt);
        img_player_death03 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_death03, opt);
        img_player_death04 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_death04, opt);
        img_player_shield = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_shield, opt);
        img_player_laser_charge01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_lazer_charge01, opt);
        img_player_laser_charge02 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_lazer_charge02, opt);
        img_player_laser_charge03 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_lazer_charge03, opt);
        img_player_laser_charge04 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_lazer_charge04, opt);
        img_player_laser_charge05 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_lazer_charge05, opt);
        img_player_laser_charge06 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_lazer_charge06, opt);
        img_player_laser_charge07 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_lazer_charge07, opt);
        img_player_laser_charge08 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_lazer_charge08, opt);
        img_player_laser_charge09 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_lazer_charge09, opt);
        img_player_laser_charge10 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_lazer_charge10, opt);
        img_player_laser_charge11 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_lazer_charge11, opt);
        img_player_laser_charge12 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_lazer_charge12, opt);
        img_player_laser_charge13 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_lazer_charge13, opt);
        img_player_laser_charge14 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_lazer_charge14, opt);
        img_player_laser_charge15 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_lazer_charge15, opt);
        img_player_laser = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.player_laser, opt);
        img_invader_a01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_a01, opt);
        img_invader_a02 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_a02, opt);
        img_invader_b01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_b01, opt);
        img_invader_b02 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_b02, opt);
        img_invader_c01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_c01, opt);
        img_invader_c02 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_c02, opt);
        img_invader_death01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_death01, opt);
        img_invader_death02 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_death02, opt);
        img_invader_death03 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_death03, opt);
        img_invader_death04 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_death04, opt);
        img_invader_death05 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_death05, opt);
        img_invader_laser01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser01, opt);
        img_invader_laser_charge01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser_charge01, opt);
        img_invader_laser_charge02 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser_charge02, opt);
        img_invader_laser_charge03 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser_charge03, opt);
        img_invader_laser_charge04 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser_charge04, opt);
        img_invader_laser_charge05 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser_charge05, opt);
        img_invader_laser_charge06 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser_charge06, opt);
        img_invader_laser_charge07 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser_charge07, opt);
        img_invader_laser_charge08 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser_charge08, opt);
        img_invader_laser_charge09 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser_charge09, opt);
        img_invader_laser_charge10 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser_charge10, opt);
        img_invader_laser_charge11 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser_charge11, opt);
        img_invader_laser_charge12 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser_charge12, opt);
        img_invader_laser_charge13 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser_charge13, opt);
        img_invader_laser_charge14 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser_charge14, opt);
        img_invader_laser_charge15 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser_charge15, opt);
        img_invader_laser_charge16 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_laser_charge16, opt);
        img_invader_UFO_hit01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_ufo_hit01, opt);
        img_invader_UFO01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_ufo_flying01, opt);
        img_invader_UFO_firing01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_ufo_firing01, opt);
        img_invader_UFO_firing02 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_ufo_firing02, opt);
        img_invader_UFO_firing03 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.invader_ufo_firing03, opt);
        img_projectile_a = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.projectile_a, opt);
        img_brick_01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_01, opt);
        img_brick_02 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_02, opt);
        img_brick_03 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_03, opt);
        img_brick_04 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_04, opt);
        img_brick_aa01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_aa01, opt);
        img_brick_aa02 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_aa02, opt);
        img_brick_aa03 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_aa03, opt);
        img_brick_aa04 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_aa04, opt);
        img_brick_ad01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_ad01, opt);
        img_brick_ad02 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_ad02, opt);
        img_brick_ad03 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_ad03, opt);
        img_brick_ad04 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_ad04, opt);
        img_brick_cb01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_cb01, opt);
        img_brick_cb02 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_cb02, opt);
        img_brick_cb03 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_cb03, opt);
        img_brick_cb04 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_cb04, opt);
        img_brick_cc01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_cc01, opt);
        img_brick_cc03 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_cc03, opt);
        img_brick_cc02 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_cc02, opt);
        img_brick_cc04 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.def_brick_cc04, opt);
        img_powerup_blue01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.powerup_blue01, opt);
        img_powerup_blue02 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.powerup_blue02, opt);
        img_powerup_blue03 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.powerup_blue03, opt);
        img_powerup_red01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.powerup_red01, opt);
        img_powerup_red02 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.powerup_red02, opt);
        img_powerup_red03 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.powerup_red03, opt);
        img_powerup_yellow01 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.powerup_yellow01, opt);
        img_powerup_yellow02 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.powerup_yellow02, opt);
        img_powerup_yellow03 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.powerup_yellow03, opt);
        img_countdown_1 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.countdown_1, opt);
        img_countdown_2 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.countdown_2, opt);
        img_countdown_3 = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.countdown_3, opt);
        img_icon_player_life = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.icon_player_life, opt2);
        img_blank = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.blank, opt);
        img_title_splash = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.title_splash, opt);

        brick = new Bitmap[] {img_brick_01, img_brick_02, img_brick_03, img_brick_04};
        brick_aa = new Bitmap[] {img_brick_aa01, img_brick_aa02, img_brick_aa03, img_brick_aa04};
        brick_ad = new Bitmap[] {img_brick_ad01, img_brick_ad02, img_brick_ad03, img_brick_ad04};
        brick_cb = new Bitmap[] {img_brick_cb01, img_brick_cb02, img_brick_cb03, img_brick_cb04};
        brick_cc = new Bitmap[] {img_brick_cc01, img_brick_cc02, img_brick_cc03, img_brick_cc04};
    }

    public static boolean initResources(Context cntxt) {
        resources = new Resources(cntxt);

        return resources != null;
    }

    /**
     * @return If <code>Resources</code> has been initialized successfully, then this returns the
     * global instance of <code>Resources</code>.
     */
    public static Resources getInstance() {
        if(resources == null) {
            Log.e("Resources.getInstance", "Resources have not been initialized.");
            return null;
        }

        return resources;
    }
}
