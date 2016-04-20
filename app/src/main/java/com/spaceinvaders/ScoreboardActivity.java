package com.spaceinvaders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tutorials.joseph.spaceinvaders.R;

/**
 * Created by Joe on 4/18/2016.
 */
public class ScoreboardActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mScoreboard;
    HiScore mScore1 = new HiScore();
    HiScore mScore2 = new HiScore();
    HiScore mScore3 = new HiScore();
    TextView mFirst;
    TextView mSecond;
    TextView mThird;
    TextView mPlayerScore;
    EditText mName;
    Button mSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScoreboard = (LinearLayout)findViewById(R.id.activity_scoreboard);
        setContentView(R.layout.activity_scoreboard);

        mFirst = (TextView) findViewById(R.id.score1);
        mSecond = (TextView) findViewById(R.id.score2);
        mThird = (TextView) findViewById(R.id.score3);
        mPlayerScore = (TextView) findViewById(R.id.new_score);

        mScore1 = HiScore.findById(HiScore.class, 1);
        mScore2 = HiScore.findById(HiScore.class, 2);
        mScore3 = HiScore.findById(HiScore.class, 3);

        mFirst.setText(toString(mScore1));
        mSecond.setText(toString(mScore2));
        mThird.setText(toString(mScore3));
        mPlayerScore.setText("Your Score: " + Resources.player_final_score);

        mName = (EditText) findViewById(R.id.player_name);
        mSubmit = (Button) findViewById(R.id.submit_button);
        mSubmit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mSubmit.getId()) {
            if(mName.getText().length() == 3) {
                if(Resources.player_final_score > mScore1.score) {
                    mScore1.name = (mName.getText()).toString();
                    mScore1.score = Resources.player_final_score;
                }

                else if(Resources.player_final_score > mScore2.score || Resources.player_final_score <= mScore1.score && Resources.player_final_score != mScore2.score) {
                    mScore2.name = (mName.getText()).toString();
                    mScore2.score = Resources.player_final_score;
                }

                else if(Resources.player_final_score > mScore3.score || Resources.player_final_score <= mScore2.score  && Resources.player_final_score != mScore3.score) {
                    mScore3.name = (mName.getText()).toString();
                    mScore3.score = Resources.player_final_score;
                }

                mScore1.save();
                mScore2.save();
                mScore3.save();

                //Not sure if this is necessary.
                /*
                Intent pewPewIntent = new Intent(this, TitleScreenActivity.class);
                startActivity(pewPewIntent);
                */

                finish();
            }
        }
    }

    public String toString(HiScore score) {
        return score.name + ": " + score.score;
    }

    @Override
    public void onBackPressed() {
    }
}
