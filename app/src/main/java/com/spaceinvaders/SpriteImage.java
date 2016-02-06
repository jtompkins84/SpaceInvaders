package com.spaceinvaders;

import android.graphics.Bitmap;

/**
 * Created by Joseph on 2/6/2016.
 */
public class SpriteImage {

    private Bitmap bmp;
    private float DPIRatio;

    public SpriteImage(Bitmap bitmap, float DPIRatioMetric) {
        this.bmp = bitmap;
        this.DPIRatio = DPIRatioMetric;
    }

    public Bitmap getBitmap() {
        return bmp;
    }

    public float getDPIRatio() {
        return DPIRatio;
    }
}
