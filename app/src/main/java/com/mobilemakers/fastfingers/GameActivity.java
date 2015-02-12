package com.mobilemakers.fastfingers;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class GameActivity extends ActionBarActivity implements GameFragment.OnTimeIsOverListener, LoseFragment.OnTryAgainListener{

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
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new GameFragment())
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
