package com.spaceinvaders;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tutorials.joseph.spaceinvaders.R;

@SuppressWarnings("ALL")
public class SpaceInvadersActivity extends AppCompatActivity implements View.OnClickListener {
    FrameLayout gamePlayLayout;
    PlayFieldView playFieldView;
    PauseMenu pauseMenu;

    boolean paused = false;

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

        if(Resources.initResources(this) == false) {
            Log.e("SpaceInvadersActivity", "Resource initialization failed!");
        }

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        //Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);

        gamePlayLayout = new FrameLayout(this);
        gamePlayLayout.setId(R.id.play_field_fragment);

        playFieldView = new PlayFieldView(this, size.x, size.x);
        playFieldView.setId(R.id.play_field_view);

        pauseMenu = new PauseMenu();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        playFieldView.setLayoutParams(params);
        gamePlayLayout.addView(playFieldView);

        setContentView(gamePlayLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!paused) {
            playFieldView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        playFieldView.pause();
    }

    @Override
    public void onBackPressed() {
        if(pauseMenu != null) {
            if (!paused) {
                paused = true;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(gamePlayLayout.getId(), this.pauseMenu);
                transaction.addToBackStack(null);
                transaction.commit();
                playFieldView.pause();
            }
            else if(paused) {
                paused = false;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(this.pauseMenu);
                transaction.addToBackStack(null);
                transaction.commit();
                playFieldView.resume();
            }
        }
    }

    public void unPause() {
        if(paused) {
            paused = false;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(this.pauseMenu);
            transaction.addToBackStack(null);
            transaction.commit();
            playFieldView.resumeNoCountdown();
        }
    }

    @Override
    public void onClick(View v) {
        if(R.id.resume_button == v.getId()) {
            paused = false;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(this.pauseMenu);
            transaction.addToBackStack(null);
            transaction.commit();
            playFieldView.resumeNoCountdown();
        }
        else if(R.id.quit_button == v.getId()) {
            finish();
        }
    }
}
