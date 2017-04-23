package it.parello.tictactoenodejs.activities;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import it.parello.tictactoenodejs.R;
import it.parello.tictactoenodejs.fragments.Statistics;
import it.parello.tictactoenodejs.listeners.OGRClickListener;

public class MainMenu extends AppCompatActivity implements Statistics.OnFragmentInteractionListener {

    Button playAlone,playOnline, statistics,exit;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        playAlone.setOnClickListener(l->{
            intent = new Intent(this,SinglePlayer.class);
            startActivity(intent);
        });
        playOnline.setOnClickListener(new OGRClickListener(getApplicationContext()));
        statistics.setOnClickListener(l->{
            //TODO
            Fragment statisticsFragment = new Statistics();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.statistics_content, statisticsFragment)
                    .addToBackStack(null)
                    .commit();

        });
        exit.setOnClickListener(l->{
            onBackPressed();
        });
    }

    private void findViews(){
        playAlone = (Button) findViewById(R.id.play_single_player);
        playOnline = (Button) findViewById(R.id.play_multi_player);
        statistics = (Button) findViewById(R.id.statistics);
        exit = (Button) findViewById(R.id.exit);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
