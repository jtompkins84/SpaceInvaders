package com.spaceinvaders;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tutorials.joseph.spaceinvaders.R;

import java.util.ArrayList;

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

    ArrayList<HiScore> hiScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScoreboard = (LinearLayout)findViewById(R.id.activity_scoreboard);
        setContentView(R.layout.activity_scoreboard);

        mFirst = (TextView) findViewById(R.id.score1);
        mSecond = (TextView) findViewById(R.id.score2);
        mThird = (TextView) findViewById(R.id.score3);
        mPlayerScore = (TextView) findViewById(R.id.new_score);

        hiScores = (ArrayList) HiScore.listAll(HiScore.class, "score desc");

        if(hiScores.size() < 3) {
            mScore1 = new HiScore("JIT", 200);
            mScore2 = new HiScore("JNK", 100);
            mScore3 = new HiScore("VCG", 50);

            HiScore.save(mScore1);
            HiScore.save(mScore2);
            HiScore.save(mScore3);
        }
        else {
            mScore1 = hiScores.get(0);
            mScore2 = hiScores.get(1);
            mScore3 = hiScores.get(2);
        }

        mFirst.setText(mScore1.toString());
        mSecond.setText(mScore2.toString());
        mThird.setText(mScore3.toString());

        mPlayerScore.setText(Resources.player_final_score);

        mName = (EditText) findViewById(R.id.player_name);
        mSubmit = (Button) findViewById(R.id.submit_button);
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mSubmit.getId()) {
            if(mName.getText().length() == 3) {
                ((ViewGroup) mSubmit.getParent()).removeView(mSubmit);
                ((ViewGroup) mName.getParent()).removeView(mName);
//                if(Resources.player_final_score > mScore1.score) {
//                    mScore1.name = (mName.getText()).toString();
//                    mScore1.score = Resources.player_final_score;
//                }
//
//                else if(Resources.player_final_score <= mScore1.score
//                        || Resources.player_final_score >= mScore2.score){
//                    mScore2.name = (mName.getText()).toString();
//                    mScore2.score = Resources.player_final_score;
//                }
//
//                else if(Resources.player_final_score <= mScore2.score
//                        || Resources.player_final_score >= mScore1.score) {
//                    mScore3.name = (mName.getText()).toString();
//                    mScore3.score = Resources.player_final_score;
//                }

                HiScore newScore = new HiScore(mName.getText().toString(),
                        Resources.player_final_score);

                newScore.save();

                hiScores = (ArrayList) HiScore.listAll(HiScore.class, "score desc");

                mScore1 = hiScores.get(0);
                mScore2 = hiScores.get(1);
                mScore3 = hiScores.get(2);

                mFirst.setText(mScore1.toString());
                mSecond.setText(mScore2.toString());
                mThird.setText(mScore3.toString());
            }
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
