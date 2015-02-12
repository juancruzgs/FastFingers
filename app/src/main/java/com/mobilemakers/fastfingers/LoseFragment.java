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
    long mScore;

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
        retrieveScore();
        prepareTextViewScore();
        prepareTextViewBest();
    }

    private void retrieveScore() {
        if (getArguments().containsKey(SCORE)){
            mScore = getArguments().getLong(SCORE);
        }
        else {
            mScore = 0;
        }
    }

    private void prepareTextViewScore() {
        TextView textViewScore = (TextView)getActivity().findViewById(R.id.text_view_your_score);
        textViewScore.setText(String.format(getString(R.string.your_score), mScore));
    }

    private void prepareTextViewBest() {
        long bestScore = compareActualAndBestScore();
        TextView textViewBest = (TextView)getActivity().findViewById(R.id.text_view_best);
        textViewBest.setText(String.format(getString(R.string.best_score), bestScore));
    }

    private long compareActualAndBestScore() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        long bestScore = sharedPref.getLong(getString(R.string.saved_best_score), 0);
        if (mScore > bestScore) {
            saveScoreAsBestScore(sharedPref);
            bestScore = mScore;
        }
        return bestScore;
    }

    private void saveScoreAsBestScore(SharedPreferences sharedPref) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(getString(R.string.saved_best_score), mScore);
        editor.apply();
    }

}
