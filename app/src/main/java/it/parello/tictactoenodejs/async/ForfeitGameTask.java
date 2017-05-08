package it.parello.tictactoenodejs.async;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import it.parello.tictactoenodejs.service.AsyncResponse;

/**
 * Created by Parello on 05/05/2017.
 */

public class ForfeitGameTask extends AsyncTask<String,Void,String> {

    private static final String TAG = "ForfeitGameTask";
    public AsyncResponse responseListener;

    public ForfeitGameTask(AsyncResponse listener){
        this.responseListener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String data = "";

        HttpURLConnection httpURLConnection = null;
        try {

            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes("ForfeitRequest=" + params[1]);
            wr.flush();
            wr.close();
            Log.d(TAG, "sending forfeit");
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
        Log.e(TAG , "forfeit response: " + result);
        responseListener.onProcessFinished(result);
    }

}
