package com.spaceinvaders;

import android.content.Context;
import android.graphics.Color;
import android.view.SurfaceView;

/**
 * Created by Joseph on 2/5/2016.
 */
public class UserControllerView extends SurfaceView {

    PlayFieldView playField;

    public UserControllerView(Context context, PlayFieldView playField) {
        super(context);

        this.playField = playField;

        this.setBackgroundColor(Color.argb(255, 50, 50, 50));

        //TODO create button controls here. make public methods that can player movement data to
        //TODO the PlayFieldView, where gameplay updates occur.
    }


}
