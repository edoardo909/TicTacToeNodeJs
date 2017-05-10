package it.parello.tictactoenodejs.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import it.parello.tictactoenodejs.R;
import it.parello.tictactoenodejs.async.ForfeitGameTask;
import it.parello.tictactoenodejs.async.SendGameDataTask;
import it.parello.tictactoenodejs.firebase.MyFirebaseMessagingService;
import it.parello.tictactoenodejs.listeners.MPClickListener;
import it.parello.tictactoenodejs.service.AsyncResponse;
import it.parello.tictactoenodejs.service.MyAppActivity;

public class MultiPlayer extends MyAppActivity implements AsyncResponse {

    Button restart;
    public static int mpMark[][];
    int resultArray[] = {3, 3, 3, 3, 3, 3, 3, 3, 3};
    public static int i, j = 0;
    public static Button mpButtons[][];
    public static TextView mpTextView;
    AlertDialog alertDialog;
    LocalBroadcastManager localBroadcastManager;
    private static final String TAG = "MultiPlayerActivity";
    private static final String forfeitURL = "http://192.168.1.220:8888/async/forfeit";
    private static final String URL = "http://192.168.1.220:8888/async/game";
    SharedPreferences sharedPreferences;
    String playerMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);
        setBoard();
        restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(l->{
            resetBoard();
        });
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter intentFilter = new IntentFilter(MyFirebaseMessagingService.INTENT_FILTER_GAME_END);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        Log.e(TAG,"player Marker: " + playerMarker.toString());
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "Game forfeit from other player, YOU WIN =)");
            Toast.makeText(getApplicationContext(), "Game was forfeit", Toast.LENGTH_LONG).show();
            final Handler handler = new Handler();
            handler.postDelayed(()-> {}, Toast.LENGTH_LONG +10);
            finish();
        }
    };

    private void setBoard() {
        mpButtons = new Button[3][3];
        mpMark = new int[3][3];
        mpButtons[0][0] = (Button) findViewById(R.id.b1);
        mpButtons[0][1] = (Button) findViewById(R.id.b2);
        mpButtons[0][2] = (Button) findViewById(R.id.b3);
        mpButtons[1][0] = (Button) findViewById(R.id.b4);
        mpButtons[1][1] = (Button) findViewById(R.id.b5);
        mpButtons[1][2] = (Button) findViewById(R.id.b6);
        mpButtons[2][0] = (Button) findViewById(R.id.b7);
        mpButtons[2][1] = (Button) findViewById(R.id.b8);
        mpButtons[2][2] = (Button) findViewById(R.id.b9);
        mpTextView = (TextView) findViewById(R.id.dialogue);
        mpTextView.setText("Click a button to start.");
        playerMarker = String.valueOf(getIntent().getStringExtra("playerMarker"));
        if (playerMarker.equals("X")){
            mpTextView.setText("You are player X: wait for O");
            for (i = 0; i < 3; i++) {
                for (j = 0; j < 3; j++){
                    mpButtons[i][j].setEnabled(false);
                }
            }
        }else if(playerMarker.equals("O")){
            mpTextView.setText("You are player O: play!");
        }

        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++)
                mpMark[i][j] = 2;
        }

        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                int x = i;
                int y = j;
                mpButtons[i][j].setOnClickListener(l-> {
                        Log.d(TAG,"button "+ mpButtons[x][y] + " clicked");
                        int resultArrayIndex = (3*x)+y;
                        if (mpButtons[x][y].isEnabled()) {
                            mpButtons[x][y].setEnabled(false);
                            if(playerMarker.equals("O")) {
                                mpButtons[x][y].setText("O");
                                mpMark[x][y] = 0;
                                resultArray[resultArrayIndex] = mpMark[x][y];
                            }else if(playerMarker.equals("X")){
                                //todo get game data from player "O"
                                mpButtons[x][y].setText("X");
                                mpMark[x][y] = 1;
                                resultArray[resultArrayIndex] = mpMark[x][y];
                            }
                            //x convertire in array: (modulo(3 xke 3x3)* riga) + posizione es: mpButtons(0,2) -> (3*0)+2 = 2
                            sendGameDataJson(resultArray);
                            mpTextView.setText("");
                        }
                });

            }
        }
    }


    private void resetBoard(){
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                mpButtons[i][j].setText(" ");
                mpButtons[i][j].setEnabled(true);
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
                    forfeitGameTask.execute(forfeitURL, String.valueOf(MyFirebaseMessagingService.instanceId));
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

    private void sendGameDataJson(int [] resultArray){
        JSONObject gameData = new JSONObject();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String playerID = sharedPreferences.getString("firebase-token","you fucked up somewhere");
        try {
            gameData.put("player_id", playerID);
            gameData.put("game_id", MyFirebaseMessagingService.instanceId);
            gameData.put("board_data", Arrays.toString(resultArray) );
            gameData.put("winner", false);
            Log.d(TAG,"Executing task: sending gamedata");
            new SendGameDataTask().execute(URL,gameData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }
}
