package it.parello.tictactoenodejs.service;

import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by edoar on 23/04/2017.
 */

public class MyAppActivity extends AppCompatActivity {


    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contextOfApplication = getApplicationContext();

    }
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

}
