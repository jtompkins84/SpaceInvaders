package com.spaceinvaders;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import com.spaceinvaders.game_entities.SurvivedFragment;
import com.tutorials.joseph.spaceinvaders.R;

@SuppressWarnings("ALL")
public class SpaceInvadersActivity extends AppCompatActivity implements View.OnClickListener {
    private FrameLayout gamePlayLayout;
    private PlayFieldView playFieldView;
    private PauseMenu pauseMenu;
    private GameOverFragment gameOverFragment;
    private VictoryFragment victoryFragment;
    private SurvivedFragment survivedFragment;

    boolean paused = false;

    /*
    *
    * RESOURCES
    * v v v v v v v v v
    */
    private Bitmap bmap_player;
    private Bitmap bmap_invader_a;
    private boolean doingGameOver = false;
    private boolean doVictory = false;
    private boolean doSurvived = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Resources.initResources(this) == false) {
            Log.e("SpaceInvadersActivity", "Resource initialization failed!");
        }

        pauseMenu = new PauseMenu();
        gameOverFragment = new GameOverFragment();
        victoryFragment = new VictoryFragment();

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        //Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);

        gamePlayLayout = new FrameLayout(this);
        gamePlayLayout.setId(R.id.play_field_fragment);

        playFieldView = new PlayFieldView(this, size.x, size.y);
        playFieldView.setId(R.id.play_field_view);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        playFieldView.setLayoutParams(params);
        gamePlayLayout.addView(playFieldView);

        setContentView(gamePlayLayout);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
                playFieldView.resumeNoCountdown();
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
            playFieldView.resume();
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
        else if(R.id.game_over_button == v.getId()) {
            Intent gameOver = new Intent(this, ScoreboardActivity.class);
            startActivity(gameOver);

            finish();
        }
        else if(R.id.victory_button == v.getId()) {
            doVictory = false;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(victoryFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            playFieldView.initializePlayField();
            playFieldView.resume();
        }
        else if(R.id.survived_button == v.getId()) {
            doSurvived = false;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(survivedFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            playFieldView.initializePlayField();
            playFieldView.resume();
        }
        else if(playFieldView.getPlayerLives() == 0 && !doingGameOver) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(gamePlayLayout.getId(), gameOverFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            doingGameOver = true;
        }
        else if(doVictory) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(gamePlayLayout.getId(), victoryFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            playFieldView.pause();
        }
        else if(doSurvived) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(gamePlayLayout.getId(), survivedFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            playFieldView.pause();
        }
    }
    public void doGameOver() {
        onClick(playFieldView);
    }

    public void doVictory() {
        doVictory = true;
        onClick(playFieldView);
    }

    public void doSurvived() {
        doSurvived = true;
        onClick(playFieldView);
    }
}
