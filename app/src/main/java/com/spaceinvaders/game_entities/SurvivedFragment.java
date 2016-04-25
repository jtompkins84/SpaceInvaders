package com.spaceinvaders.game_entities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.spaceinvaders.SpaceInvadersActivity;
import com.tutorials.joseph.spaceinvaders.R;

/**
 * Created by Joseph on 4/23/2016.
 */
public class SurvivedFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.survived_fragment, container, false);

        ImageButton gameOverButton = (ImageButton)view.findViewById(R.id.survived_button);
        gameOverButton.setOnClickListener((SpaceInvadersActivity) getActivity());

        return view;
    }
}
