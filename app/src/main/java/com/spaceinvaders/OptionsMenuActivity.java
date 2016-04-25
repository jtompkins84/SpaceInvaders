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
    ToggleButton mDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOptionsMenu = (LinearLayout)findViewById(R.id.activity_options_menu);
        setContentView(R.layout.activity_options_menu);
        mNormal = (ToggleButton) findViewById(R.id.normal_button);
        mNormal.setOnClickListener(this);
        mHard = (ToggleButton) findViewById(R.id.hard_button);
        mHard.setOnClickListener(this);
        mDemo = (ToggleButton) findViewById(R.id.demo_button);
        mDemo.setOnClickListener(this);

        if(Resources.difficulty == Resources.Difficulty.NORMAL) {
            mNormal.setChecked(true);
            mHard.setChecked(false);
            mDemo.setChecked(false);
        }

        else if(Resources.difficulty == Resources.Difficulty.HARD) {
            mNormal.setChecked(false);
            mHard.setChecked(true);
            mDemo.setChecked(false);
        }

        else if(Resources.difficulty == Resources.Difficulty.DEMO) {
            mNormal.setChecked(false);
            mHard.setChecked(false);
            mDemo.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mNormal.getId()) {
            if(Resources.difficulty == Resources.Difficulty.HARD || Resources.difficulty == Resources.Difficulty.DEMO || mNormal.isChecked() == false || mHard.isChecked() == true || mDemo.isChecked() == true) {
                mNormal.setChecked(true);
                mHard.setChecked(false);
                mDemo.setChecked(false);
                Resources.difficulty = Resources.Difficulty.NORMAL;
            }
        }

        else if(v.getId() == mHard.getId()) {
            if(Resources.difficulty == Resources.Difficulty.NORMAL || Resources.difficulty == Resources.Difficulty.DEMO || mHard.isChecked() == false || mNormal.isChecked() == true || mDemo.isChecked() == true) {
                mHard.setChecked(true);
                mNormal.setChecked(false);
                mDemo.setChecked(false);
                Resources.difficulty = Resources.Difficulty.HARD;
            }
        }

        else if(v.getId() == mDemo.getId()) {
            if(Resources.difficulty == Resources.Difficulty.NORMAL || Resources.difficulty == Resources.Difficulty.HARD || mHard.isChecked() == false || mNormal.isChecked() == true || mHard.isChecked() == true) {
                mDemo.setChecked(true);
                mHard.setChecked(false);
                mNormal.setChecked(false);
                Resources.difficulty = Resources.Difficulty.DEMO;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
