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
    protected String doInBackground(String... params) {
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