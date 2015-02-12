package com.mobilemakers.fastfingers;

import android.app.Activity;
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
    TextView mTextViewScore;
    TextView mTextViewBest;

    OnTryAgainListener mCallback;

    // Container Activity must implement this interface
    public interface OnTryAgainListener {
        public void onTryAgain();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
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
        wireUpViews(rootView);
        Button buttonTryAgain = (Button)rootView.findViewById(R.id.button_try_again);
        buttonTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onTryAgain();
            }
        });
        return rootView;
    }

    private void wireUpViews(View rootView) {
        mTextViewScore = (TextView)rootView.findViewById(R.id.text_view_your_score);
        mTextViewBest = (TextView)rootView.findViewById(R.id.text_view_best);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retrieveScore();
        mTextViewScore.setText(String.format(getString(R.string.your_score), mScore));
    }

    private void retrieveScore() {
        if (getArguments().containsKey(SCORE)){
            mScore = getArguments().getLong(SCORE);
        }
        else {
            mScore = 0;
        }
    }
}
