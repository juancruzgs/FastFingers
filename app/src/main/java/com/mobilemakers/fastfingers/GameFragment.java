package com.mobilemakers.fastfingers;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class GameFragment extends Fragment {

    TextView mTextViewTimer;

    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        mTextViewTimer = (TextView)rootView.findViewById(R.id.text_view_timer);
        CountDown counter = new CountDown(5000,1000);
        counter.start();
        return rootView;
    }

    private class CountDown extends CountDownTimer {

        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTextViewTimer.setText(String.valueOf(millisUntilFinished/1000));
        }

        @Override
        public void onFinish() {
            mTextViewTimer.setText("Finished");
        }
    }
}
