package it.parello.tictactoenodejs.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import it.parello.tictactoenodejs.R;
import it.parello.tictactoenodejs.async.SendOnlineGameRequest;
import it.parello.tictactoenodejs.firebase.MyFirebaseMessagingService;
import it.parello.tictactoenodejs.fragments.MenuFragment;
import it.parello.tictactoenodejs.fragments.Statistics;
import it.parello.tictactoenodejs.service.AsyncResponse;
import it.parello.tictactoenodejs.service.MyAppActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainMenu extends MyAppActivity implements
        Statistics.OnFragmentInteractionListener, MenuFragment.OnMenuFragmentListener,AsyncResponse {

//    public ProgressBar progressBar;
    private static final String URL = "http://192.168.1.220:8888/async/gamerequest";
    Intent intent;
    private static final String TAG = "MainMenuActivity";
    ProgressBar progressBar;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progresss_bar);
        Fragment menuFragment = new MenuFragment();
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction().add(R.id.main_fragment, menuFragment).addToBackStack(null).commit();
        }
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter intentFilter = new IntentFilter(MyFirebaseMessagingService.INTENT_FILTER_ERROR);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(), "An error occured on the server, try again", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    };

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        progressBar.setVisibility(GONE);
        if (count > 1) {
            getFragmentManager().popBackStack();
        } else {
           exit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void singleplayer() {
        intent = new Intent(this,SinglePlayer.class);
        startActivity(intent);
    }

    @Override
    public void multiplayer() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String myToken = prefs.getString("firebase-token","you fucked up somewhere");
        Log.e(TAG, myToken);
        progressBar.setVisibility(VISIBLE);
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
//            Log.d(TAG,"Response is OK: 200");
//            intent = new Intent(getApplicationContext(),MultiPlayer.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
        } else if(responseCode.equals("500")){
            Log.e(TAG,"Response is ERROR: 500");
            Log.e(TAG,"You are not registered on the server... WHO ARE YOU??");
        } else if(responseCode.equals("503")){
            Log.e(TAG, "503...please wait for other player");
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        progressBar.setVisibility(GONE);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }
}
