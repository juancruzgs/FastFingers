package com.mobilemakers.fastfingers;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class GameActivity extends ActionBarActivity implements GameFragment.OnTimeIsOverListener, LoseFragment.OnTryAgainListener{

    public static final String GAME_MODE = "game_mode";
    private static final int DEFAULT_MODE = 0;

    @Override
    public void onTimeIsOver(long score) {
        LoseFragment loseFragment = new LoseFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(LoseFragment.SCORE, score);
        loseFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, loseFragment)
                .commit();
    }

    @Override
    public void onTryAgain() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new GameFragment())
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if (savedInstanceState == null) {
            GameFragment gameFragment = new GameFragment();
            int mode = getIntent().getIntExtra(GAME_MODE, DEFAULT_MODE);
            Bundle bundle = new Bundle();
            bundle.putInt(GameFragment.GAME_MODE, mode);
            gameFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, gameFragment)
                    .commit();
        }
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
