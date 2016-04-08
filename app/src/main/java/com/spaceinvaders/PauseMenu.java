package com.spaceinvaders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.tutorials.joseph.spaceinvaders.R;

/**
 * Created by Joseph on 4/7/2016.
 */
public class PauseMenu extends Fragment{

    private View buttonPanel;
    private View pauseMenuView;
    private Button resumeButton;



    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.pause_menu_layout, container, false);

        pauseMenuView = view.findViewById(R.id.pause_menu);
        buttonPanel = view.findViewById(R.id.pause_button_panel);
        ImageButton resumeButton = (ImageButton) view.findViewById(R.id.resume_button);
        resumeButton.setOnClickListener((SpaceInvadersActivity) this.getActivity());
        ImageButton quitButton = (ImageButton) view.findViewById(R.id.quit_button);
        quitButton.setOnClickListener((SpaceInvadersActivity) this.getActivity());

        return view;
    }
}
