package com.mobilemakers.fastfingers;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
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

    public static final String GAME_MODE = "game_mode";
    private static final int MODE_NORMAL = 0;
    //private static final int MODE_SURVIVAL = 1;
    private static final int MODE_HARDCORE = 2;

    final static long TIME_PER_WORD_NORMAL = 4000;
    final static long COUNT_DOWN_INTERVAL = 100;
    final static long TIME_SURVIVAL = 30000;

    final static String COLOR_RED = "red";
    final static String COLOR_GREEN = "green";

    TextView mTextViewWord;
    CountDown mCounter;
    Random mRandom;
    String[] mWords;
    long mScore = 0;
    long mScorePerWord = TIME_PER_WORD_NORMAL;
    int mMode = 0;

    OnTimeIsOverListener mCallback;

    // Container Activity must implement this interface
    public interface OnTimeIsOverListener {
        public void onTimeIsOver(long score);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        prepareEditText(rootView);
        prepareInitialWord();
        return rootView;
    }

    private void wireUpViews(View rootView) {
        mTextViewWord = (TextView)rootView.findViewById(R.id.text_view_word);
    }

    private void prepareEditText(View rootView) {
        final EditText editTextWord = (EditText)rootView.findViewById(R.id.edit_text_insert_word);
        final TextView textViewActualScore = (TextView)rootView.findViewById(R.id.text_view_actual_score);
        showKeyboard(editTextWord);
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
                    correctWordActions();
                } else {
                    String word = mTextViewWord.getText().toString();
                    String wordToCompare = "";
                    try {
                        wordToCompare = word.substring(0, s.length());
                    } catch (Exception e) {
                        //s.length > word.length
                        incorrectWordActions();
                    }

                    if (s.toString().equals(wordToCompare)) {
                        editTextWord.setTextColor(Color.parseColor(COLOR_GREEN));
                    } else {
                        incorrectWordActions();
                    }
                }
            }

            private void correctWordActions() {
                editTextWord.getText().clear();
                updateScore();
                updateRandomWordForTextView();
                if (mMode == MODE_NORMAL){
                    restartCountDown();
                }
            }

            private void updateScore() {
                if (mMode == MODE_NORMAL){
                    mScore += mScorePerWord;
                }
                else {
                    mScore += mTextViewWord.getText().toString().length()*100;
                }

                textViewActualScore.setText(String.format(getString(R.string.actual_score), mScore));
            }

            private void restartCountDown() {
                mScorePerWord = TIME_PER_WORD_NORMAL;
                mCounter.cancel();
                mCounter.start();
            }

            private void incorrectWordActions(){
                editTextWord.setTextColor(Color.parseColor(COLOR_RED));
                if (mMode == MODE_HARDCORE){
                    gameTerminated();
                }
            }

        });
    }

    private void showKeyboard(EditText editTextWord) {
        if(editTextWord.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    private void prepareInitialWord() {
        mWords = getResources().getStringArray(R.array.string_array_words);
        mRandom = new Random();
        updateRandomWordForTextView();
    }

    private void updateRandomWordForTextView() {
        int position = mRandom.nextInt(mWords.length);
        mTextViewWord.setText(mWords[position]);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retrieveGameMode();
        startCountDown();
    }

    private void retrieveGameMode() {
        if (getArguments().containsKey(GAME_MODE)){
            mMode = getArguments().getInt(GAME_MODE);
        }
    }

    private void startCountDown() {
        TextView textViewTimer = (TextView)getActivity().findViewById(R.id.text_view_timer);
        long time;
        if (mMode == MODE_NORMAL)
        {
            time = TIME_PER_WORD_NORMAL;
        }
        else {
            time = TIME_SURVIVAL;
        }

        mCounter = new CountDown(textViewTimer , time, COUNT_DOWN_INTERVAL);
        mCounter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCounter.cancel();
    }

    private void gameTerminated() {
        mCounter.cancel();
        mCallback.onTimeIsOver(mScore);
    }

    private class CountDown extends CountDownTimer {

        TextView mTextViewTimer;

        public CountDown(TextView textView, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            mTextViewTimer = textView;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String time = formatMillisecondsForDisplay(millisUntilFinished);
            mTextViewTimer.setText(time);
            //Survival and Hardcore don't use this
            mScorePerWord -=  COUNT_DOWN_INTERVAL;
        }

        private String formatMillisecondsForDisplay(long millisUntilFinished) {
            long second = (millisUntilFinished / 1000) % 60;
            long milis = (millisUntilFinished /100) % 10;
            return String.format("%02d:%d", second, milis);
        }

        @Override
        public void onFinish() {
            gameTerminated();
        }

    }
}
