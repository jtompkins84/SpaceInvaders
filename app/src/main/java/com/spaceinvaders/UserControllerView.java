package com.spaceinvaders;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import com.tutorials.joseph.spaceinvaders.R;

/**
 * Created by Joseph on 2/5/2016.
 */
public class UserControllerView extends SurfaceView {

    PlayFieldView playField;

    public PlayFieldView getPlayField() {
        return playField;
    }

    public UserControllerView(Context context, PlayFieldView playField) {
        super(context);

        this.playField = playField;

        this.setBackgroundColor(Color.argb(255, 50, 50, 50));

        //TODO create button controls here. make public methods that can player movement data to
        //TODO the PlayFieldView, where gameplay updates occur.

        final Button fire = new Button(context);
        fire.setId(R.id.fireId);
        fire.setText("Fire");
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == fire) {
                    getPlayField().playerFire();
                }
            }
        });
    }
}
