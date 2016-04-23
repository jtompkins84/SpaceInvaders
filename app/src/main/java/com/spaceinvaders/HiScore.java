package com.spaceinvaders;

import com.orm.SugarRecord;

/**
 * Created by Joe on 4/20/2016.
 */
public class HiScore extends SugarRecord {
    String name = "AAA";
    int score = 0;

    public HiScore() {

    }

    public HiScore(String n, int s) {
        this.name = n;
        this.score = s;
    }

    @Override
    public String toString()
    {
        return name + ": " + score;
    }
}
