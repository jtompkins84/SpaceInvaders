package com.spaceinvaders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.tutorials.joseph.spaceinvaders.R;

/**
 * Created by Joseph on 4/13/2016.
 */
public class TitleScreenActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_title_screen);

        mStartButton = (ImageButton) findViewById(R.id.title_start_button);
        mStartButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mStartButton.getId()) {
            Intent pewPewIntent = new Intent(this, SpaceInvadersActivity.class);
            startActivity(pewPewIntent);
        }
    }
}
