package com.spaceinvaders;

import com.orm.SugarRecord;

/**
 * Created by Joseph Kent on 4/20/2016.
 *
 * Designed to use the Sugar ORM to track player score and initials locally on a device.
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
