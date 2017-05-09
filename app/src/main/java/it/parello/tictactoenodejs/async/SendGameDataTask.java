package it.parello.tictactoenodejs.async;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Parello on 21/04/2017.
 */

public class SendGameDataTask extends AsyncTask<String,Void,String> {

    private static final String TAG = "SendGameDataTask";


    @Override
    protected String doInBackground(String... params) { //TODO UNDER DEVELOPMENT
        String data = "";

        HttpURLConnection httpURLConnection = null;
        try {

            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes("GameData=" + params[1]);
            wr.flush();
            wr.close();
            Log.d(TAG,"sending gamedata");
            InputStream in = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(in);

            int inputStreamData = inputStreamReader.read();
            while (inputStreamData != -1) {
                char current = (char) inputStreamData;
                inputStreamData = inputStreamReader.read();
                data += current;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return data;
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.e(TAG , result);
    }
}
/*
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
            gameData.put("player_id", MyFirebaseInstanceIdService.getRefreshedToken());
            gameData.put("game_id", gameId);
            gameData.put("board_data", tempBoardData );
            gameData.put("winner", false);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outputStreamWriter.write(gameData.toString());
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
* */