package it.parello.tictactoenodejs.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import it.parello.tictactoenodejs.R;
import it.parello.tictactoenodejs.async.ForfeitGameTask;
import it.parello.tictactoenodejs.async.SendGameDataTask;
import it.parello.tictactoenodejs.firebase.MyFirebaseMessagingService;
import it.parello.tictactoenodejs.service.AsyncResponse;
import it.parello.tictactoenodejs.service.MyAppActivity;

public class MultiPlayer extends MyAppActivity implements AsyncResponse {

    Button restart;
    public static int mpMark[][];
    int resultArray[] = {3, 3, 3, 3, 3, 3, 3, 3, 3};;
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
        playGame();
        restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(l->{
            resetBoard();
        });
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter forfeitIntentFilter = new IntentFilter(MyFirebaseMessagingService.INTENT_FILTER_GAME_END);
        IntentFilter gameDataIntentFilter = new IntentFilter(MyFirebaseMessagingService.INTENT_FILTER_DATA);
        IntentFilter winnerIntentFilter = new IntentFilter(MyFirebaseMessagingService.INTENT_FILTER_WINNER);
        localBroadcastManager.registerReceiver(gameDataBroadcastReceiver, gameDataIntentFilter);
        localBroadcastManager.registerReceiver(winnerBroadCastReceiver, winnerIntentFilter);
        localBroadcastManager.registerReceiver(forfeitBroadcastReceiver, forfeitIntentFilter);
        Log.e(TAG,"player Marker: " + playerMarker.toString());
    }

    private final BroadcastReceiver forfeitBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "Game forfeit from other player, YOU WIN =)");
            Toast.makeText(getApplicationContext(), "Game was forfeit", Toast.LENGTH_LONG).show();
            final Handler handler = new Handler();
            handler.postDelayed(()-> {}, Toast.LENGTH_LONG +10);
            finish();
        }
    };

    private final BroadcastReceiver gameDataBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String[] opponentGameData = intent.getStringExtra("board_data").replaceAll("[\\]\\[|\\] ]", "").split(",");
            int [] odg = new int[9];
            for(int i = 0; i < 9; i++) {
                odg[i] = Integer.parseInt(opponentGameData[i]);
            }
            int count = 0;
            for (i = 0; i < 3; i++) {
                for (j = 0; j < 3; j++) {
                    mpMark[i][j] = odg[count];
                    count++;
                    if (playerMarker.equals("X") && mpMark[i][j] == 0) {
                        mpButtons[i][j].setText("O");
                    } else if (playerMarker.equals("O") && mpMark[i][j] == 1) {
                        mpButtons[i][j].setText("X");
                    }
                    mpButtons[i][j].setEnabled(true);
                    int resultArrayIndex = (3*i)+j;
                    resultArray[resultArrayIndex] = mpMark[i][j];
                }
            }
            mpTextView.setText("It's your turn");
        }
    };

    private final BroadcastReceiver winnerBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mpTextView.setText(intent.getStringExtra("winner") + " has won the game");
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


    }

    private void playGame(){
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                int x = i;
                int y = j;
                mpButtons[i][j].setOnClickListener(l-> {
                    Log.d(TAG,"button "+ mpButtons[x][y] + " clicked");
                    //x convertire in array: (modulo(3 xke 3x3)* riga) + posizione es: mpButtons(0,2) -> (3*0)+2 = 2
                    int resultArrayIndex = (3*x)+y;
                    if (mpButtons[x][y].isEnabled()) {
                        if(playerMarker.equals("O")) {
                            mpButtons[x][y].setText("O");
                            mpMark[x][y] = 0;
                            resultArray[resultArrayIndex] = mpMark[x][y];
                        }else if(playerMarker.equals("X")){
                            mpButtons[x][y].setText("X");
                            mpMark[x][y] = 1;
                            resultArray[resultArrayIndex] = mpMark[x][y];
                        }
                        mpTextView.setText("opponent turn");
                        sendGameDataJson(resultArray);
                        for (int a = 0; a < 3; a++) {
                            for (int b = 0; b < 3; b++) {
                                mpButtons[a][b].setEnabled(false);
                            }
                        }
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
                resultArray = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3};
                //TODO send game repeat request
            }
        }
    }
    @Override
    public void onBackPressed(){
        if(mpTextView.getText().toString().contains("has won the game")){
            super.onBackPressed();
        }else{
            forfeitGame();
        }
    }

    private void forfeitGame(){
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
            Log.d(TAG,"Executing task: sending gamedata");
            new SendGameDataTask().execute(URL,gameData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(forfeitBroadcastReceiver);
        localBroadcastManager.unregisterReceiver(gameDataBroadcastReceiver);
        localBroadcastManager.unregisterReceiver(winnerBroadCastReceiver);
    }
}
