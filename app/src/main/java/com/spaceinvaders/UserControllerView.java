package com.spaceinvaders;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import com.tutorials.joseph.spaceinvaders.R;

/**
 * Created by Joseph on 2/5/2016.
 */
public class UserControllerView extends SurfaceView {
    private SurfaceHolder ourHolder;
    private Canvas canvas;
    private Paint paint;

    private PlayFieldView playField;
    private Button fire;

    public PlayFieldView getPlayField() {
        return playField;
    }

    @SuppressWarnings("ResourceType")
    public UserControllerView(Context context, PlayFieldView playField) {
        super(context);
        this.playField = playField;

        this.setBackgroundColor(Color.argb(255, 50, 50, 50));

        //TODO create button controls here. make public methods that can player movement data to
        //TODO the PlayFieldView, where gameplay updates occur.

        fire = new Button(context);
        fire.setId(1212);
        fire.setText("Fire");
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            //Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                break;
            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }

    private void draw() {
        //Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();
//            canvas.setDensity(100);

            //Draw the background color
            canvas.drawColor(Color.argb(255, 50, 50, 50));

            // Choose the brush color for the drawing
            paint.setColor(Color.argb(255, 0, 255, 0));

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }
}
