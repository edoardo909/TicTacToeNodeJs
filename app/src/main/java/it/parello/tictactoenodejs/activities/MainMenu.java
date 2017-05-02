package it.parello.tictactoenodejs.activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.content.Intent;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;

import it.parello.tictactoenodejs.R;
import it.parello.tictactoenodejs.async.SendOnlineGameRequest;
import it.parello.tictactoenodejs.fragments.MenuFragment;
import it.parello.tictactoenodejs.fragments.Statistics;
import it.parello.tictactoenodejs.service.AsyncResponse;
import it.parello.tictactoenodejs.service.MyAppActivity;

public class MainMenu extends MyAppActivity implements
        Statistics.OnFragmentInteractionListener, MenuFragment.OnMenuFragmentListener,AsyncResponse {

//    public ProgressBar progressBar;
    private static final String URL = "http://192.168.1.220:8888/async/gamerequest";
    Intent intent;
    private static final String TAG = "MainMenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment menuFragment = new MenuFragment();
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction().add(R.id.main_fragment, menuFragment).addToBackStack(null).commit();
        }

    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            getFragmentManager().popBackStack();
        } else {
           exit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void playAlone() {
        intent = new Intent(this,SinglePlayer.class);
        startActivity(intent);
    }

    @Override
    public void playWithFriends() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String myToken = prefs.getString("firebase-token","you fucked up somewhere");
        SendOnlineGameRequest sgr = new SendOnlineGameRequest(this);
        Log.d(TAG,"Sending Online Game Request Async Task");
        sgr.execute(URL, myToken);
    }

    @Override
    public void exit() {
        finish();
    }

    @Override
    public void statistics() {
        Fragment statisticsFragment = new Statistics();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, statisticsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onProcessFinished(String responseCode) {
        Log.d(TAG,"Sending Online Game Request Async Task Finish");
        if (responseCode.equals("200")){
            Log.d(TAG,"Response is OK: 200");
            intent = new Intent(getApplicationContext(),MultiPlayer.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if(responseCode.equals("500")){
            Log.e(TAG,"Response is ERROR: 500");
            Log.e(TAG,"You are not registered on the server... WHO ARE YOU??");
        }
    }
}
