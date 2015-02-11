package com.mobilemakers.fastfingers;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class GameFragment extends Fragment {

    final static long TOTAL_SCORE_PER_WORD = 11000;
    final static long COUNT_DOWN_INTERVAL = 100;

    TextView mTextViewTimer;
    TextView mTextViewWord;
    CountDown mCounter;
    Random mRandom;
    String[] mWords;
    long mScore;
    long mScorePerWord;

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
    }

    private void startCountDown() {
        mScorePerWord = TOTAL_SCORE_PER_WORD;
        mCounter = new CountDown(TOTAL_SCORE_PER_WORD, COUNT_DOWN_INTERVAL);
        mCounter.start();
    }

    private void prepareEditText(View rootView) {
        final EditText editTextWord = (EditText)rootView.findViewById(R.id.edit_text_insert_word);
        if(editTextWord.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        editTextWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(mTextViewWord.getText().toString())) {
                    editTextWord.getText().clear();
                    //TODO Calculate score based on time
                    mScore += mScorePerWord;
                    Log.d("mScore", String.valueOf(mScore));
                    restartCountDown();
                    //TODO Decrement time (create new countdown)
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
            mTextViewTimer.setText(String.valueOf(millisUntilFinished/1000));
            mScorePerWord -=  COUNT_DOWN_INTERVAL;
            Log.d("mScorePerWord", String.valueOf(mScorePerWord));
        }

        @Override
        public void onFinish() {
            //TODO You Lose and show score
            mTextViewTimer.setText("Finished");
            Toast.makeText(getActivity(), String.valueOf(mScore), Toast.LENGTH_LONG).show();
            mScore = 0;
            cancel();
        }
    }
}
