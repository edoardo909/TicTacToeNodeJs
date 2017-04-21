package it.parello.tictactoenodejs.service;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;

import it.parello.tictactoenodejs.firebase.MyFirebaseInstanceIdService;

/**
 * Created by Parello on 21/04/2017.
 */

public class SendGameDataTask extends AsyncTask<Void,Void,JSONObject> {

    private static final String TAG = "SendGameDataTask";
    private static final String URL = "http://192.168.1.220:8888/";

    @Override
    protected JSONObject doInBackground(Void... voids) { //TODO UNDER DEVELOPMENT
        HttpURLConnection connection = null;
        URL url;
        InputStream stream = null;
        try {
            url = new URL(URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            Log.d(TAG,"Connected to " + URL);
//            connection.connect();
            JSONObject gameData = new JSONObject();
//            gameData.put("player_id", MyFirebaseInstanceIdService.getRefreshedToken());
//            gameData.put("game_id",);
//            gameData.put("board_data", );
//            gameData.put("winner",);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outputStreamWriter.write("");
            outputStreamWriter.flush();
            outputStreamWriter.close();

            stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"), 8);
            String result = reader.readLine();
            Log.e(TAG,"RESULT " +result);
            return gameData;

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if (connection != null){
                connection.disconnect();
            }
        }
        return null;
    }
}
