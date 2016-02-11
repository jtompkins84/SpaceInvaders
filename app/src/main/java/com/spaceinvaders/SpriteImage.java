package com.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Joseph on 2/6/2016.
 */
public class SpriteImage {

    private Bitmap bmp;
    private Canvas canvas;
    private float DPIRatio;

    /**
     * Instantiates a <code>SpriteImage</code>.
     * @param   bitmap <code>Bitmap</code> - the bitmap image associated with
     * this <code>SpriteImage</code>
     * @param   DPIRatioMetric <code>float</code> - ratio multiplier used to
     * adjust the size & placement of bitmaps, coordinates, hit-boxes, etc.
     */
    public SpriteImage(Bitmap bitmap, float DPIRatioMetric) {
        this.bmp = bitmap;
        this.canvas = canvas;
        this.DPIRatio = DPIRatioMetric;
    }

    /**
     * <code><b><i>getBitmap</i></b></code><p>
     * @return <code><b>Bitmap</b></code>
     */
    public Bitmap getBitmap() {
        return bmp;
    }

    /**
     * This is the ratio multiplier used to
     * adjust the size & placement of bitmaps, coordinates, hit-boxes, etc.
     * <code><b><i>getDPIRatio</i></b></code><p>
     * @return <code><b>float</b></code>
     */
    public float getDPIRatio() {
        return DPIRatio;
    }
}
