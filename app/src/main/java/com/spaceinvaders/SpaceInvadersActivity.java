package com.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

@SuppressWarnings("ALL")
public class SpaceInvadersActivity extends AppCompatActivity {
    RelativeLayout gamePlayLayout;
    PlayFieldView playFieldView;
    UserControllerView userControllerView;

    /*
    *
    * RESOURCES
    * v v v v v v v v v
    */
    private Bitmap bmap_player;
    private Bitmap bmap_invader_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        //Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);

        gamePlayLayout = new RelativeLayout(this);


        userControllerView = new UserControllerView(this);
        userControllerView.setId(1111);
        playFieldView = new PlayFieldView(this, userControllerView, size.x, size.x + (size.x / 4));
        playFieldView.setId(2222);

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams q = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        q.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        playFieldView.setLayoutParams(q);
        gamePlayLayout.addView(playFieldView, size.x, size.x + (size.x / 4));

        p.addRule(RelativeLayout.BELOW, playFieldView.getId());
        userControllerView.setLayoutParams(p);
        gamePlayLayout.addView(userControllerView);

        setContentView(gamePlayLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();

        playFieldView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        playFieldView.pause();
    }

}
