package it.parello.tictactoenodejs.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import it.parello.tictactoenodejs.R;

public class MainMenu extends AppCompatActivity {

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
        playOnline.setOnClickListener(l->{
            //TODO
            intent = new Intent(this,MultiPlayer.class);
            startActivity(intent);
        });
        statistics.setOnClickListener(l->{
            //TODO
            Fragment statisticsFragment = getSupportFragmentManager().findFragmentById(R.id.statistics_fragment);
            getSupportFragmentManager().beginTransaction().add(statisticsFragment,"statisticsFragment").commit();
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
    public void onBackPressed(){
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            //TODO notifica che vince l'altro
        }else {
            super.onBackPressed();
        }
    }
}
