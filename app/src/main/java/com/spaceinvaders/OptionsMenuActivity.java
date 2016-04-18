package com.spaceinvaders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import com.tutorials.joseph.spaceinvaders.R;

/**
 * Created by Joe on 4/16/2016.
 */
public class OptionsMenuActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mOptionsMenu;
    ToggleButton mNormal;
    ToggleButton mHard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOptionsMenu = (LinearLayout)findViewById(R.id.activity_options_menu);
        setContentView(R.layout.activity_options_menu);
        mNormal = (ToggleButton) findViewById(R.id.normal_button);
        mNormal.setOnClickListener(this);
        mHard = (ToggleButton) findViewById(R.id.hard_button);
        mHard.setOnClickListener(this);

        if(Resources.difficulty == Resources.Difficulty.NORMAL) {
            mNormal.setChecked(true);
            mHard.setChecked(false);
        }

        else if(Resources.difficulty == Resources.Difficulty.HARD) {
            mNormal.setChecked(false);
            mHard.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mNormal.getId()) {
            if(Resources.difficulty == Resources.Difficulty.HARD || mNormal.isChecked() == false || mHard.isChecked() == true) {
                mNormal.setChecked(true);
                mHard.setChecked(false);
                Resources.difficulty = Resources.Difficulty.NORMAL;
            }
        }

        else if(v.getId() == mHard.getId()) {
            if(Resources.difficulty == Resources.Difficulty.NORMAL || mHard.isChecked() == false || mNormal.isChecked() == true) {
                mHard.setChecked(true);
                mNormal.setChecked(false);
                Resources.difficulty = Resources.Difficulty.HARD;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
