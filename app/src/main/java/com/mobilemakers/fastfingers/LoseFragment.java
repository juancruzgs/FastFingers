package com.mobilemakers.fastfingers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LoseFragment extends Fragment {

    public static final String SCORE = "score";
    public static final String GAME_MODE = "game_mode";
    private static final long DEFAULT_SCORE = 0;

    private static final int MODE_NORMAL = 0;
    private static final int MODE_SURVIVAL = 1;
    private static final int MODE_HARDCORE = 2;

    OnTryAgainListener mCallback;

    // Container Activity must implement this interface
    public interface OnTryAgainListener {
        public void onTryAgain();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnTryAgainListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTryAgainListener");
        }
    }

    public LoseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time_is_over, container, false);
        prepareButton(rootView);
        return rootView;
    }

    private void prepareButton(View rootView) {
        Button buttonTryAgain = (Button)rootView.findViewById(R.id.button_try_again);
        buttonTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onTryAgain();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        long score = retrieveScore();
        int mode = retrieveMode();
        prepareTextViewMode(mode);
        prepareTextViewScore(score);
        prepareTextViewBest(score, mode);
    }

    private int retrieveMode() {
        int mode = 0;
        if (getArguments().containsKey(GAME_MODE)){
            mode = getArguments().getInt(GAME_MODE);
        }
        return mode;
    }

    private long retrieveScore() {
        long score = 0;
        if (getArguments().containsKey(SCORE)){
            score = getArguments().getLong(SCORE);
        }
        return score;
    }

    private void prepareTextViewMode(int mode) {
        TextView textViewMode = (TextView)getActivity().findViewById(R.id.text_view_mode);
        String text;
        switch (mode){
            case MODE_NORMAL: text = getString(R.string.mode_normal);
                break;
            case MODE_SURVIVAL: text = getString(R.string.mode_survival);
                break;
            case MODE_HARDCORE: text = getString(R.string.mode_hardcore);
                break;
            default: text = getString(R.string.mode_normal);
                break;
        }
        textViewMode.setText(text);
    }

    private void prepareTextViewScore(long score) {
        TextView textViewScore = (TextView)getActivity().findViewById(R.id.text_view_your_score);
        textViewScore.setText(String.format(getString(R.string.your_score), score));
    }

    private void prepareTextViewBest(long score, int mode) {
        long bestScore = compareActualAndBestScore(score, mode);
        TextView textViewBest = (TextView)getActivity().findViewById(R.id.text_view_best);
        textViewBest.setText(String.format(getString(R.string.best_score), bestScore));
    }

    private long compareActualAndBestScore(long score, int mode) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String sharedPrefString;
        switch (mode){
            case MODE_NORMAL: sharedPrefString = getString(R.string.saved_best_score_normal);
                break;
            case MODE_SURVIVAL: sharedPrefString = getString(R.string.saved_best_score_survival);
                break;
            case MODE_HARDCORE: sharedPrefString = getString(R.string.saved_best_score_hardcore);
                break;
            default: sharedPrefString = getString(R.string.saved_best_score_normal);
                break;
        }
        long bestScore = sharedPref.getLong(sharedPrefString, DEFAULT_SCORE);
        if (score > bestScore) {
            saveScoreAsBestScore(sharedPref, score, sharedPrefString);
            bestScore = score;
        }
        return bestScore;
    }

    private void saveScoreAsBestScore(SharedPreferences sharedPref, long score, String sharedPrefString) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(sharedPrefString, score);
        editor.apply();
    }

}
