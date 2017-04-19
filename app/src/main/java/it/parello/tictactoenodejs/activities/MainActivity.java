package it.parello.tictactoenodejs.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import it.parello.tictactoenodejs.R;

public class MainActivity extends AppCompatActivity {

    Button play,statistics,exit;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        play.setOnClickListener(l->{
            intent = new Intent(this,GameActivity.class);
            startActivity(intent);
        });
        statistics.setOnClickListener(l->{
            //TODO
        });
        exit.setOnClickListener(l->{
            onBackPressed();
        });
    }

    private void findViews(){
        play = (Button) findViewById(R.id.play);
        statistics = (Button) findViewById(R.id.statistics);
        exit = (Button) findViewById(R.id.exit);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
