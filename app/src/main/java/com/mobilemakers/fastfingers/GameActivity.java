package com.mobilemakers.fastfingers;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class GameActivity extends ActionBarActivity implements GameFragment.OnTimeIsOverListener, LoseFragment.OnTryAgainListener{

    public static final String GAME_MODE = "game_mode";
    private static final int DEFAULT_MODE = 0;

    int mMode = 0;

    @Override
    public void onTimeIsOver(long score) {
        LoseFragment loseFragment = new LoseFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(LoseFragment.SCORE, score);
        bundle.putInt(LoseFragment.GAME_MODE, mMode);
        loseFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, loseFragment)
                .commit();
    }

    @Override
    public void onTryAgain() {
        GameFragment gameFragment = createGameFragmentWithParamters();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, gameFragment)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if (savedInstanceState == null) {
            mMode = getIntent().getIntExtra(GAME_MODE, DEFAULT_MODE);
            GameFragment gameFragment = createGameFragmentWithParamters();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, gameFragment)
                    .commit();
        }
    }

    private GameFragment createGameFragmentWithParamters() {
        GameFragment gameFragment = new GameFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(GameFragment.GAME_MODE, mMode);
        gameFragment.setArguments(bundle);
        return gameFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
