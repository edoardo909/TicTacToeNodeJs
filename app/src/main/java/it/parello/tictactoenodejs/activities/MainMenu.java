package it.parello.tictactoenodejs.activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.content.Intent;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;

import it.parello.tictactoenodejs.R;
import it.parello.tictactoenodejs.fragments.Statistics;
import it.parello.tictactoenodejs.listeners.OGRClickListener;
import it.parello.tictactoenodejs.service.MyAppActivity;

public class MainMenu extends MyAppActivity implements Statistics.OnFragmentInteractionListener {

    Button playAlone,playOnline, statistics,exit;
    Intent intent;
    public ProgressBar progressBar;
    public int progressStatus = 0;
    private Handler mHandler = new Handler();

    private static final String TAG = "MainMenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String myToken = prefs.getString("firebase-token","you fucked up somewhere");
        playAlone.setOnClickListener(l->{
            Log.e(TAG,"MY TOKEN: " + myToken);
            intent = new Intent(this,SinglePlayer.class);
            startActivity(intent);
        });
        playOnline.setOnClickListener(new OGRClickListener(getApplicationContext()));
        statistics.setOnClickListener(l->{
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
        progressBar = (ProgressBar) findViewById(R.id.progresss_bar);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
