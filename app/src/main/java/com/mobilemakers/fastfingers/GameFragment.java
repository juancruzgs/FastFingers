package com.mobilemakers.fastfingers;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class GameFragment extends Fragment {

    final static long TOTAL_SCORE_PER_WORD = 4000;
    final static long COUNT_DOWN_INTERVAL = 100;

    TextView mTextViewTimer;
    TextView mTextViewWord;
    TextView mTextViewActualScore;
    EditText mEditTextWord;
    CountDown mCounter;
    Random mRandom;
    String[] mWords;
    long mScore;
    long mScorePerWord;

    OnTimeIsOverListener mCallback;

    // Container Activity must implement this interface
    public interface OnTimeIsOverListener {
        public void onTimeIsOver(long score);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnTimeIsOverListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTimeIsOverListener");
        }
    }

    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        wireUpViews(rootView);
        startCountDown();
        prepareEditText(rootView);
        prepareInitialWord();
        mScore = 0;
        return rootView;
    }

    private void wireUpViews(View rootView) {
        mTextViewWord = (TextView)rootView.findViewById(R.id.text_view_word);
        mTextViewTimer = (TextView)rootView.findViewById(R.id.text_view_timer);
        mTextViewActualScore = (TextView)rootView.findViewById(R.id.text_view_actual_score);
    }

    private void startCountDown() {
        mScorePerWord = TOTAL_SCORE_PER_WORD;
        mCounter = new CountDown(TOTAL_SCORE_PER_WORD, COUNT_DOWN_INTERVAL);
        mCounter.start();
    }

    private void prepareEditText(View rootView) {
        mEditTextWord = (EditText)rootView.findViewById(R.id.edit_text_insert_word);
        if(mEditTextWord.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        mEditTextWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().equals(mTextViewWord.getText().toString())) {
                    mEditTextWord.getText().clear();
                    mScore += mScorePerWord;
                    mTextViewActualScore.setText(String.format(getString(R.string.actual_score), mScore));
                    restartCountDown();
                } else {
                    String word = mTextViewWord.getText().toString();
                    String wordToCompare = null;
                    try {
                        wordToCompare = word.substring(0, s.length());
                    } catch (Exception e) {
                        //s.length > word.length
                        mEditTextWord.setTextColor(Color.parseColor("red"));
                    }

                    if (s.toString().equals(wordToCompare)) {
                        mEditTextWord.setTextColor(Color.parseColor("green"));
                    } else {
                        mEditTextWord.setTextColor(Color.parseColor("red"));
                    }
                }
            }
        });
    }

    private void restartCountDown() {
        setTextViewRandomWord();
        mScorePerWord = TOTAL_SCORE_PER_WORD;
        mCounter.cancel();
        mCounter.start();
    }

    private void prepareInitialWord() {
        mWords = getResources().getStringArray(R.array.string_array_words);
        mRandom = new Random();
        setTextViewRandomWord();
    }

    private void setTextViewRandomWord() {
        int position = mRandom.nextInt(mWords.length);
        mTextViewWord.setText(mWords[position]);
    }

    private class CountDown extends CountDownTimer {

        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long second = (millisUntilFinished / 1000) % 60;
            long milis = (millisUntilFinished /100) % 10;
            String time = String.format("%02d:%d", second, milis);
            mTextViewTimer.setText(time);

            mScorePerWord -=  COUNT_DOWN_INTERVAL;
        }

        @Override
        public void onFinish() {
            cancel();
            mCallback.onTimeIsOver(mScore);
        }
    }
}
