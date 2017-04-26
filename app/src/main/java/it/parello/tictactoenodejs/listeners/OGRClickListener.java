package it.parello.tictactoenodejs.listeners;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import it.parello.tictactoenodejs.activities.MultiPlayer;
import it.parello.tictactoenodejs.async.SendOnlineGameRequest;
import it.parello.tictactoenodejs.service.AsyncResponse;

<<<<<<< HEAD
import static it.parello.tictactoenodejs.firebase.MyFirebaseInstanceIdService.refreshedToken;

=======
>>>>>>> master
/**
 * Created by edoar on 23/04/2017.
 */

/*
* OnlineGameRequestClickListener
*/
public class OGRClickListener implements View.OnClickListener,AsyncResponse {

    private static final String TAG = "OGRClickListener";
<<<<<<< HEAD
    private static final String URL = "http://192.168.1.220:8888/async/gamerequest";
=======
    private static final String URL = "http://192.168.10.21:8888/async/gamerequest";
>>>>>>> master
    Context context;
    Intent intent;

    public OGRClickListener(Context context){
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        SendOnlineGameRequest sgr = new SendOnlineGameRequest(this);
        Log.d(TAG,"Sending Online Game Request Async Task");
<<<<<<< HEAD

=======
>>>>>>> master
        sgr.execute(URL,"iWannaPlay");
    }

    @Override
    public void onProcessFinished(String responseCode) {
        Log.d(TAG,"Sending Online Game Request Async Task Finish");
        if (responseCode.equals("ok")){
            Log.d(TAG,"Response is OK");
            intent = new Intent(context,MultiPlayer.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
