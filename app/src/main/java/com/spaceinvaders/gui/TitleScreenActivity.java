package com.spaceinvaders.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.tutorials.joseph.spaceinvaders.R;

/**
 * Created by Joseph on 4/13/2016.
 */
public class TitleScreenActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout mTitleScreen;
    ImageButton mStartButton;
    ImageButton mOptionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTitleScreen = (LinearLayout)findViewById(R.id.activity_title_screen);
        setContentView(R.layout.activity_title_screen);
        mStartButton = (ImageButton) findViewById(R.id.title_start_button);
        mStartButton.setOnClickListener(this);
        mOptionsButton = (ImageButton) findViewById(R.id.options_button);
        mOptionsButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == mStartButton.getId()) {
            Intent pewPewIntent = new Intent(this, SpaceInvadersActivity.class);
            startActivity(pewPewIntent);
        }

        else if(v.getId() == mOptionsButton.getId()) {
            Intent pewPewIntent2 = new Intent(this, OptionsMenuActivity.class);
            startActivity(pewPewIntent2);
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}