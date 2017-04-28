package it.parello.tictactoenodejs.listeners;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import it.parello.tictactoenodejs.activities.MultiPlayer;
import it.parello.tictactoenodejs.async.SendOnlineGameRequest;
import it.parello.tictactoenodejs.service.AsyncResponse;
import it.parello.tictactoenodejs.service.MyAppActivity;



/**
 * Created by edoar on 23/04/2017.
 */

/*
* OnlineGameRequestClickListener
*/
public class OGRClickListener implements View.OnClickListener, AsyncResponse {

    private static final String TAG = "OGRClickListener";
    private static final String URL = "http://192.168.1.220:8888/async/gamerequest";
//    ProgressBar progressBar;
    Context context;
    Intent intent;

    public OGRClickListener(Context context){
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        SendOnlineGameRequest sgr = new SendOnlineGameRequest(this);
        Log.d(TAG,"Sending Online Game Request Async Task");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyAppActivity.getContextOfApplication());
        String myToken = prefs.getString("firebase-token","you fucked up somewhere");
//        sgr.setProgressBar(progressBar);
        sgr.execute(URL, myToken);
    }

    @Override
    public void onProcessFinished(String responseCode) {
        Log.d(TAG,"Sending Online Game Request Async Task Finish");
        if (responseCode.equals("200")){
            Log.d(TAG,"Response is OK: 200");
            intent = new Intent(context,MultiPlayer.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else if(responseCode.equals("500")){
            Log.e(TAG,"Response is ERROR: 500");
            Log.e(TAG,"You are not registered on the server... WHO ARE YOU??");
        }
    }
}
