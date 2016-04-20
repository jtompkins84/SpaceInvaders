package com.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.tutorials.joseph.spaceinvaders.R;

/**
 * Initializes all of the resources and provides access to them from anywhere in
 *  the app. Hello World
 */
public class Resources {
    private static Resources resources = null;
    private static Context context;
    private static boolean initSuccussful = false;

    public static float DPIRatio;

    public enum Difficulty {NORMAL, HARD};
    public static Difficulty difficulty = Difficulty.NORMAL;

    public static Bitmap img_player;
    public static Bitmap img_player_death01;
    public static Bitmap img_player_death02;
    public static Bitmap img_player_death03;
    public static Bitmap img_player_death04;
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
    public static Bitmap img_countdown_1;
    public static Bitmap img_countdown_2;
    public static Bitmap img_countdown_3;
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
        this.context = cntxt;

        // Recalculate DPI to a standard size.
        float dpi = (int)((640.0f / 1440.0f) * context.getResources().getDisplayMetrics().widthPixels); // Adjust to some ratio of desired DPI of 640
        context.getResources().getDisplayMetrics().densityDpi = (int)dpi; // Change Device's DPI to new DPI

        // Tell rendering how to size bitmaps
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inDensity = 480;

        DPIRatio = dpi / (float)opt.inDensity;

        img_player = BitmapFactory.decodeResource(context.getResources(), R.drawable.player, opt);
//        Log.v("PlayFieldView", "Width Ratio <SPRITE_WIDTH> / <SCREEN_WIDTH>: " + img_player.getBitmap().getWidth() + " / " + playFieldWidth + " = " + ((float)img_player.getBitmap().getWidth() / (float)playFieldWidth));
        img_player_death01 = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_death01, opt);
        img_player_death02 = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_death02, opt);
        img_player_death03 = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_death03, opt);
        img_player_death04 = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_death04, opt);
        img_invader_a01 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader_a01, opt);
        img_invader_a02 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader_a02, opt);
        img_invader_b01 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader_b01, opt);
        img_invader_b02 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader_b02, opt);
        img_invader_c01 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader_c01, opt);
        img_invader_c02 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader_c02, opt);
        img_invader_death01 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader_death01, opt);
        img_invader_death02 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader_death02, opt);
        img_invader_death03 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader_death03, opt);
        img_invader_death04 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader_death04, opt);
        img_invader_death05 = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader_death05, opt);
        img_projectile_a = BitmapFactory.decodeResource(context.getResources(), R.drawable.projectile_a, opt);
        img_brick_01 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_01, opt);
        img_brick_02 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_02, opt);
        img_brick_03 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_03, opt);
        img_brick_04 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_04, opt);
        img_brick_aa01 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_aa01, opt);
        img_brick_aa02 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_aa02, opt);
        img_brick_aa03 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_aa03, opt);
        img_brick_aa04 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_aa04, opt);
        img_brick_ad01 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_ad01, opt);
        img_brick_ad02 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_ad02, opt);
        img_brick_ad03 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_ad03, opt);
        img_brick_ad04 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_ad04, opt);
        img_brick_cb01 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_cb01, opt);
        img_brick_cb02 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_cb02, opt);
        img_brick_cb03 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_cb03, opt);
        img_brick_cb04 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_cb04, opt);
        img_brick_cc01 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_cc01, opt);
        img_brick_cc03 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_cc03, opt);
        img_brick_cc02 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_cc02, opt);
        img_brick_cc04 = BitmapFactory.decodeResource(context.getResources(), R.drawable.def_brick_cc04, opt);
        img_countdown_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.countdown_1, opt);
        img_countdown_2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.countdown_2, opt);
        img_countdown_3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.countdown_3, opt);
        img_blank = BitmapFactory.decodeResource(context.getResources(), R.drawable.blank, opt);
//        img_title_splash = BitmapFactory.decodeResource(context.getResources(), R.drawable.title_splash, opt);

        brick = new Bitmap[] {img_brick_01, img_brick_02, img_brick_03, img_brick_04};
        brick_aa = new Bitmap[] {img_brick_aa01, img_brick_aa02, img_brick_aa03, img_brick_aa04};
        brick_ad = new Bitmap[] {img_brick_ad01, img_brick_ad02, img_brick_ad03, img_brick_ad04};
        brick_cb = new Bitmap[] {img_brick_cb01, img_brick_cb02, img_brick_cb03, img_brick_cb04};
        brick_cc = new Bitmap[] {img_brick_cc01, img_brick_cc02, img_brick_cc03, img_brick_cc04};
    }

    public static boolean initResources(Context cntxt) {
        resources = new Resources(cntxt);

        return initSuccussful;
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
