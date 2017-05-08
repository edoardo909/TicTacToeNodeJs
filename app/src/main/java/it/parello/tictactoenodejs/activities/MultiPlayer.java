package it.parello.tictactoenodejs.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;

import it.parello.tictactoenodejs.R;
import it.parello.tictactoenodejs.async.ForfeitGameTask;
import it.parello.tictactoenodejs.firebase.MyFirebaseMessagingService;
import it.parello.tictactoenodejs.listeners.MPClickListener;
import it.parello.tictactoenodejs.service.AsyncResponse;
import it.parello.tictactoenodejs.service.MyAppActivity;

public class MultiPlayer extends MyAppActivity implements AsyncResponse {

    Button restart;
    public static int mpMark[][];
    public static int i, j = 0;
    public static Button mpButtons[][];
    public static TextView mpTextView;
    AlertDialog alertDialog;
    LocalBroadcastManager localBroadcastManager;
    private static final String TAG = "MultiPlayerActivity";
    private static final String URL = "http://192.168.1.220:8888/async/forfeit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);
        setBoard();
        restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(l->{
            setBoard();
        });
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter intentFilter = new IntentFilter(MyFirebaseMessagingService.INTENT_FILTER_GAME_END);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "porco il clero se funziona qua perchè non va???");
            Toast.makeText(getApplicationContext(), "Game forfeit from other player, YOU WIN =) ", Toast.LENGTH_LONG);
            finish();
        }
    };

    private void setBoard() {
        mpButtons = new Button[4][4];
        mpMark = new int[4][4];
        mpButtons[0][2] = (Button) findViewById(R.id.b1);
        mpButtons[0][1] = (Button) findViewById(R.id.b2);
        mpButtons[0][0] = (Button) findViewById(R.id.b3);
        mpButtons[1][2] = (Button) findViewById(R.id.b4);
        mpButtons[1][1] = (Button) findViewById(R.id.b5);
        mpButtons[1][0] = (Button) findViewById(R.id.b6);
        mpButtons[2][2] = (Button) findViewById(R.id.b7);
        mpButtons[2][1] = (Button) findViewById(R.id.b8);
        mpButtons[2][0] = (Button) findViewById(R.id.b9);

        mpTextView = (TextView) findViewById(R.id.dialogue);

        for (i = 0; i <= 2; i++) {
            for (j = 0; j <= 2; j++)
                mpMark[i][j] = 2;

        }

        mpTextView.setText("Click a button to start.");
        for (i = 0; i <= 2; i++) {
            for (j = 0; j <= 2; j++) {
                mpButtons[i][j].setOnClickListener(new MPClickListener(i, j));
                if (!mpButtons[i][j].isEnabled()) {
                    mpButtons[i][j].setText(" ");
                    mpButtons[i][j].setEnabled(true);
                }
            }
        }
    }

    @Override
    public void onBackPressed(){
        finishGame();
    }

    private void finishGame(){
        if(!isGameOver()){
            ForfeitGameTask forfeitGameTask = new ForfeitGameTask(this);
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle("Quit Game?");
            alertBuilder.setMessage("you will forfeit");
            alertBuilder.setCancelable(false);
            alertBuilder.setPositiveButton(R.string.yes, (d, j)-> {
                    forfeitGameTask.execute(URL, String.valueOf(MyFirebaseMessagingService.instanceId));

                    finish();
            }).setNegativeButton(R.string.no, (d, j)->{
                    d.cancel();
            });
            alertDialog = alertBuilder.create();
            alertDialog.show();
        }
    }

    private boolean isGameOver(){
        return false;
    }

    @Override
    public void onProcessFinished(String response) {
        Log.d(TAG,"Sending Online Game Request Async Task Finish");
        if (response.equals("gameCancelled")){
            setGameOver();
            alertDialog.dismiss();
        }
        Log.i(TAG, "multiplayer process finished");
    }

    private void setGameOver() {
        Log.i(TAG, "you lost the game");
        //TODO put value into statistics
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }
}
