package com.spaceinvaders;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.tutorials.joseph.spaceinvaders.R;

/**
 * Created by Joseph on 4/7/2016.
 */
public class PauseMenu extends Fragment implements AdapterView.OnItemClickListener{

    private View buttonPanel;
    private View pauseMenuView;
    private Button resumeButton;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.pause_menu_layout, container, false);

        pauseMenuView = view.findViewById(R.id.pause_menu);
        buttonPanel = view.findViewById(R.id.pause_button_panel);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
