package com.mobilemakers.fastfingers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    private static final int MODE_NORMAL = 0;
    private static final int MODE_SURVIVAL = 1;
    private static final int MODE_HARDCORE = 2;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        prepareButtonNormal(rootView);
        prepareButtonSurvival(rootView);
        prepareButtonHardcore(rootView);
        return rootView;
    }

    private void prepareButtonNormal(View rootView) {
        Button buttonNormal = (Button)rootView.findViewById(R.id.button_normal);
        buttonNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                intent.putExtra(GameActivity.GAME_MODE, MODE_NORMAL);
                startActivity(intent);
            }
        });
    }

    private void prepareButtonSurvival(View rootView) {
        Button buttonSurvival = (Button)rootView.findViewById(R.id.button_survival);
        buttonSurvival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                intent.putExtra(GameActivity.GAME_MODE, MODE_SURVIVAL);
                startActivity(intent);
            }
        });
    }

    private void prepareButtonHardcore(View rootView) {
        Button buttonHardcore = (Button)rootView.findViewById(R.id.button_hardcore);
        buttonHardcore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                intent.putExtra(GameActivity.GAME_MODE, MODE_HARDCORE);
                startActivity(intent);
            }
        });
    }

}
