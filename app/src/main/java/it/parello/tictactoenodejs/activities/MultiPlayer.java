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
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import it.parello.tictactoenodejs.R;
import it.parello.tictactoenodejs.async.ForfeitGameTask;
import it.parello.tictactoenodejs.async.OnlineGameRequestTask;
import it.parello.tictactoenodejs.async.RematchRequestTask;
import it.parello.tictactoenodejs.async.SendGameDataTask;
import it.parello.tictactoenodejs.firebase.MyFirebaseMessagingService;
import it.parello.tictactoenodejs.service.AsyncResponse;
import it.parello.tictactoenodejs.service.MyAppActivity;

import static android.view.View.VISIBLE;

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
    private static final String FORFEIT_URL = "http://192.168.1.220:8888/async/forfeit";
    private static final String GAME_URL = "http://192.168.1.220:8888/async/game";
    private static final String REMATCH_URL = "http://192.168.1.220:8888/async/rematch";
    SharedPreferences sharedPreferences;
    String playerMarker;
    static int mpWinCounter, mpLoseCounter, mpDrawCounter;
    boolean isGameOver;
    ProgressBar mpProgressBar;
    public static boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);
        isActive = true;
        setBoard();
        playGame();
        restart = (Button) findViewById(R.id.restart);
        if(!isGameOver){
            restart.setEnabled(false);
        }else{
            restart.setEnabled(true);
        }
        restart.setOnClickListener(l->{
//            resetBoard();
            rematchRequest();
            restart.setEnabled(false);
        });
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter intentFilter = new IntentFilter(MyFirebaseMessagingService.INTENT_FILTER_DATA);
        intentFilter.addAction(MyFirebaseMessagingService.INTENT_FILTER_GAME_END);
        intentFilter.addAction(MyFirebaseMessagingService.INTENT_FILTER_WINNER);
        intentFilter.addAction(MyFirebaseMessagingService.INTENT_FILTER_REMATCH);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        Log.e(TAG,"player Marker: " + playerMarker.toString());
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, intent.getAction().toString());
            if (intent.getAction().equals(MyFirebaseMessagingService.INTENT_FILTER_GAME_END)){
                Log.e(TAG, "Game forfeit from other player, YOU WIN =)");
                Toast.makeText(getApplicationContext(), "Game was forfeit", Toast.LENGTH_LONG).show();
                final Handler handler = new Handler();
                handler.postDelayed(()-> {}, Toast.LENGTH_LONG +10);
                finish();
            }else if(intent.getAction().equals(MyFirebaseMessagingService.INTENT_FILTER_DATA)){
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
            }else if(intent.getAction().equals(MyFirebaseMessagingService.INTENT_FILTER_WINNER)){
                for (i = 0; i < 3; i++) {
                    for (j = 0; j < 3; j++) {
                        mpButtons[i][j].setEnabled(false);
                    }
                }
                restart.setEnabled(true);
                String winner = intent.getStringExtra("winner");
                mpTextView.setText(winner + " has won the game");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(winner.equals("X") && playerMarker.equals("X")){
                    editor.putInt("mpWinCounter", mpWinCounter++);
                }else if(winner.equals("O") && playerMarker.equals("O")){
                    editor.putInt("mpWinCounter", mpWinCounter++);
                }else if(winner.equals("O") && playerMarker.equals("X")){
                    editor.putInt("mpLoseCounter", mpLoseCounter++);
                }else if(winner.equals("X") && playerMarker.equals("O")){
                    editor.putInt("mpLoseCounter", mpLoseCounter++);
                }else{
                    editor.putInt("mpDrawCounter", mpDrawCounter++);
                }
                editor.commit();
            }else if(intent.getAction().equals(MyFirebaseMessagingService.INTENT_FILTER_REMATCH)){
                finish();
            }
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
        mpProgressBar = (ProgressBar) findViewById(R.id.progress_bar_multiplayer);
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
                    restart.setEnabled(false);
                    if(mpButtons[x][y].getText().equals("X") || mpButtons[x][y].getText().equals("O")){
                        mpButtons[x][y].setEnabled(false);
                    }
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
        mpTextView.setText("Requesting Rematch...");
        resultArray = new int[]{3, 3, 3, 3, 3, 3, 3, 3, 3};
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                mpButtons[i][j].setText(" ");
                mpButtons[i][j].setEnabled(true);
                mpProgressBar.setVisibility(View.VISIBLE);
                RematchRequestTask rematchRequestTask = new RematchRequestTask(this);
                rematchRequestTask.execute(REMATCH_URL, String.valueOf(MyFirebaseMessagingService.instanceId));
            }
        }
        setBoard();
        playGame();
    }

    private void rematchRequest(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String myToken = prefs.getString("firebase-token","you fucked up somewhere");
        Log.e(TAG, myToken);
//        progressBar.setVisibility(VISIBLE);
        OnlineGameRequestTask sgr = new OnlineGameRequestTask(this);
        mpProgressBar = (ProgressBar) findViewById(R.id.progress_bar_multiplayer);
        Log.d(TAG,"Sending Rematch Request");
        sgr.execute(MainMenu.GAME_REQUEST_URL, myToken);
    }

    @Override
    public void onBackPressed(){
        if(mpTextView.getText().toString().contains("has won the game")){
            super.onBackPressed();
            //TODO delete game on server
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
                    forfeitGameTask.execute(FORFEIT_URL, String.valueOf(MyFirebaseMessagingService.instanceId));
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
//        if(response.contains("gamerequest")){
//            finish();
//        }
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
            new SendGameDataTask().execute(GAME_URL,gameData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        isActive = false;
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }
}
