package it.parello.tictactoenodejs.listeners;

import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import it.parello.tictactoenodejs.async.SendGameDataTask;
import it.parello.tictactoenodejs.firebase.MyFirebaseMessagingService;

import static it.parello.tictactoenodejs.activities.MultiPlayer.mpButtons;
import static it.parello.tictactoenodejs.activities.MultiPlayer.mpMark;
import static it.parello.tictactoenodejs.activities.MultiPlayer.mpTextView;

/**
 * Created by edoar on 22/04/2017.
 */
@Deprecated
public class MPClickListener implements View.OnClickListener {

    int x;
    int y;
    private static final String URL = "http://192.168.1.220:8888/async";

    private static String gameId;
    String [] tempBoardData = {"O","O","X"," ","X"," "," "," "," ",};
    private static final String TAG = "MPClickListener";

    public MPClickListener(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void onClick(View v) {
        //TODO check if player X or O

        Log.d(TAG,"button "+ mpButtons[x][y] + " clicked");
        if (mpButtons[x][y].isEnabled()) {
            mpButtons[x][y].setEnabled(false);
            mpButtons[x][y].setText("O");
            mpMark[x][y] = 0;
            sendGameDataJson();
            mpTextView.setText("");
        }
    }
    private void sendGameDataJson(){
        JSONObject gameData = new JSONObject();
        try {
            gameData.put("player_id", "123456");
            gameData.put("game_id", MyFirebaseMessagingService.instanceId);
            gameData.put("board_data", Arrays.toString(tempBoardData) );
            gameData.put("winner", false);
            Log.d(TAG,"Executing task: sending gamedata");
            new SendGameDataTask().execute(URL,gameData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
