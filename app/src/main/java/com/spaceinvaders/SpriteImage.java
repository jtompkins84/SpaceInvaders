package com.spaceinvaders;

import android.graphics.Bitmap;

/**
 * Created by Joseph on 2/6/2016.
 */
public class SpriteImage {

    private Bitmap bitmap;
    private int width;
    private int height;

    public SpriteImage(Bitmap bitmap, int imageWidth, int imageHeight) {
        this.bitmap = bitmap;
        this.width = imageWidth;
        this.height = imageHeight;
    }
}
