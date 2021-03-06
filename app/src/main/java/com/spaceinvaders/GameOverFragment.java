package com.spaceinvaders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.tutorials.joseph.spaceinvaders.R;

public class GameOverFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.game_over_fragment, container, false);

        ImageButton gameOverButton = (ImageButton)view.findViewById(R.id.game_over_button);
        gameOverButton.setOnClickListener((SpaceInvadersActivity) getActivity());

        return view;
    }
}
